package com.peramdy.config.server;

import com.peramdy.config.client.ZkClient;
import com.peramdy.config.constant.PdEnv;
import com.peramdy.config.constant.ZkConstants;
import com.peramdy.config.execption.PdException;
import com.peramdy.config.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author pd
 */
public class RemoveRemoteProperties {

    private static Logger logger = LoggerFactory.getLogger(RemoveRemoteProperties.class);

    public RemoveRemoteProperties() {
        Properties properties = PropertiesUtils.getProperties("config.properties");
        Set<String> keys = properties.stringPropertyNames();
        if (keys == null || keys.size() == 0) {
            throw new PdException("config.properties is empty", new IllegalArgumentException());
        }
        String addresses = properties.getProperty(ZkConstants.ZK_ADDRESSES);
        String timeoutStr = properties.getProperty(ZkConstants.ZK_TIMEOUT);
        String appName = properties.getProperty(ZkConstants.APP_NAME);
        String env = properties.getProperty(ZkConstants.ENV);
        if (StringUtils.isBlank(appName)) {
            throw new PdException("appName is null", new NoSuchFieldException());
        }
        if (!PdEnv.isContainEnv(env)) {
            throw new PdException("env in local | dev | test | pro ", new NoSuchFieldException());
        }
        if (StringUtils.isBlank(addresses)) {
            addresses = ZkConstants.ADDRESS;
        }
        if (StringUtils.isBlank(timeoutStr)) {
            timeoutStr = ZkConstants.TIMEOUT;
        }
        Integer timeout = Integer.parseInt(timeoutStr);
        rm(ZkConstants.PARENT_PATH, addresses, timeout);
    }


    /**
     * delete all node
     *
     * @param path
     * @param addresses
     * @param timeout
     */
    private void rm(String path, String addresses, Integer timeout) {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient.Builder().addresses(addresses).timeout(timeout).build();
            Stat stat = zkClient.exists(path);
            if (stat == null) {
                logger.info("[{}] node is not exists", path);
                return;
            }
            List<String> childrenNode = zkClient.getChildrenNodeValue(path);
            for (String childNode : childrenNode) {
                String newPath;
                if (path.equals("/")) {
                    newPath = "/" + childNode;
                } else {
                    newPath = path + "/" + childNode;
                }
                rm(newPath, addresses, timeout);
            }
            if (StringUtils.isNotBlank(path) && !path.equals(ZkConstants.PARENT_PATH) && !path.equals("/")) {
                zkClient.deleteNode(path, -1);
                logger.info("[{}] node is deleted", path);
            }
        } finally {
            if (zkClient != null) {
                zkClient.closeZk();
            }
        }
    }

    public static void main(String[] args) {
        RemoveRemoteProperties removeRemoteProperties = new RemoveRemoteProperties();
    }

}
