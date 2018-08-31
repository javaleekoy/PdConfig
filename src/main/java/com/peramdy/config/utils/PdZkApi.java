package com.peramdy.config.utils;

import com.peramdy.config.config.PdGlobal;
import com.peramdy.config.execption.PdException;
import com.peramdy.config.execption.PdNoNodeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.retry.RetryNTimes;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.*;

import static com.peramdy.config.constant.PdZkElement.LOCAL_CONFIG_PATH;

/**
 * @author peramdy on 2018/8/31.
 */
public class PdZkApi {

    private static CuratorFramework client;
    private static String localPrivatePath;
    private static String localPublicPath;

    private static void reloadConfig() {
        if (client == null) {
            try {
                String cfgPath = PdGlobal.getConfig(LOCAL_CONFIG_PATH);
                File filePath = new File(cfgPath);
                if (!filePath.isDirectory()) {
                    return;
                }
                String[] files = filePath.list();
                if (files != null && files.length > 0) {
                    Arrays.stream(files).forEach(file -> {
                        String fp = filePath.getPath() + "/" + file;
                        try {
                            File mServerTarget = new File(fp);
                            Element root = XMLParser.getInstance().getRoot(mServerTarget);
                            if (root != null) {
                                NodeList list = root.getElementsByTagName("zk_server");
                                for (int i = 0; i < list.getLength(); ++i) {
                                    Element n = (Element) list.item(i);
                                    String ip = n.getElementsByTagName("ip").item(0).getFirstChild().getNodeValue();
                                    String port = n.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
                                    if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                                        String conStr = ip + ":" + port;
                                        client = CuratorFrameworkFactory.builder().connectString(conStr).namespace("").retryPolicy(new RetryNTimes(2147483647, 5000)).connectionTimeoutMs(500).build();
                                    }

                                    if (client != null) {
                                        client.start();
                                        break;
                                    }
                                }
                            }
                            if (client != null) {
                                return;
                            }
                        } catch (PdException e) {
                            e.printStackTrace();
                        }
                    });
                }

            } catch (PdException e) {
                e.printStackTrace();
            }
        }

    }


    private static String getLocalPrivatePath() {
        if (localPrivatePath != null) {
            return localPrivatePath;
        } else {
            String basepath = PdGlobal.getConfig("zk.cfg.private.path");
            String version = PdGlobal.getConfig("zk.version");
            localPrivatePath = basepath + "<" + version + ">";
            return localPrivatePath;
        }
    }


    public static String getLocalPublicPath() {
        if (localPublicPath != null) {
            return localPublicPath;
        } else {
            localPublicPath = PdGlobal.getConfig("zk.cfg.public.path");
            return localPublicPath;
        }
    }

    public static List<String> getCfgChildren(String cfgType, String flg) {
        if (!cfgType.startsWith("/")) {
            cfgType = "/" + cfgType;
        }

        reloadConfig();
        if (client == null) {
            return null;
        } else {
            String path = "";

            try {
                if ("private".equals(flg)) {
                    path = getLocalPrivatePath() + cfgType;
                }

                if ("public".equals(flg)) {
                    path = getLocalPublicPath() + cfgType;
                }

                if (StringUtils.isNotBlank(path)) {
                    GetChildrenBuilder children = client.getChildren();
                    if (children != null) {
                        return (List) children.forPath(path);
                    }
                }

                return null;
            } catch (PdNoNodeException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Map<String, String> getPrivateCfgData(String cfgType) {
        if (!cfgType.startsWith("/")) {
            cfgType = "/" + cfgType;
        }

        reloadConfig();
        if (client == null) {
            return null;
        } else {
            try {
                Map<String, String> dataMap = new HashMap();
                List<String> childNodes = getCfgChildren(cfgType, "private");
                if (childNodes != null) {
                    Iterator var3 = childNodes.iterator();

                    while (var3.hasNext()) {
                        String node = (String) var3.next();
                        String data = new String((byte[]) client.getData().forPath(getLocalPrivatePath() + cfgType + "/" + node));
                        dataMap.put(node, data);
                    }
                }

                return dataMap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Map<String, String> getPublicCfgData(String cfgType) {
        if (!cfgType.startsWith("/")) {
            cfgType = "/" + cfgType;
        }

        reloadConfig();
        if (client == null) {
            return null;
        } else {
            try {
                Map<String, String> dataMap = new HashMap();
                List<String> childNodes = getCfgChildren(cfgType, "public");
                Iterator var3 = childNodes.iterator();

                while (var3.hasNext()) {
                    String node = (String) var3.next();
                    String data = new String(client.getData().forPath(getLocalPublicPath() + cfgType + "/" + node));
                    dataMap.put(node, data);
                }

                return dataMap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
