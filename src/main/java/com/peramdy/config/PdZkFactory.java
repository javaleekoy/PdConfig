package com.peramdy.config;

import com.peramdy.config.config.PdGlobal;
import com.peramdy.config.execption.PdException;
import com.peramdy.config.utils.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Arrays;

import static com.peramdy.config.constant.PdZkConstants.*;
import static com.peramdy.config.constant.PdZkElement.*;

/**
 * @author peramdy on 2018/8/16.
 */
public class PdZkFactory {

    public static CuratorFramework get() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(MAX_RETRIES, SLEEP_TIME);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(configString())
                .retryPolicy(retryPolicy).namespace(PARENT).build();
        client.start();
        return client;
    }


    /**
     * parse config
     *
     * @return
     */
    private static String configString() {
        String path = PdGlobal.getConfig(LOCAL_CONFIG_PATH);
        File directory = new File(path);
        if (!directory.isDirectory()) {
            throw new PdException();
        } else {
            StringBuffer sb = new StringBuffer();
            String[] files = directory.list();
            Arrays.stream(files).forEach(file -> {
                String fp = directory.getPath() + File.separator + file;
                File serverFile = new File(fp);
                Element element = XmlUtil.root(serverFile);
                NodeList nodeList = element.getElementsByTagName(ZK_SERVER);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element n = (Element) nodeList.item(i);
                    String ip = n.getElementsByTagName(IP).item(0).getFirstChild().getNodeValue();
                    String port = n.getElementsByTagName(PORT).item(0).getFirstChild().getNodeValue();
                    if (StringUtils.isBlank(ip)) {
                        throw new PdException("zookeeper config error ");
                    }
                    if (StringUtils.isBlank(port)) {
                        port = DEFAULT_PORT.toString();
                    }
                    sb.append(ip).append(":").append(port).append(",");
                }
            });
            String address = sb.toString();
            if (StringUtils.isBlank(address)) {
                throw new PdException("zookeeper config is null ");
            } else {
                address = address.substring(0, address.length() - 1);
                if (StringUtils.isBlank(address)) {
                    throw new PdException("zookeeper config is null ");
                } else {
                    return address;
                }
            }

        }
    }


}
