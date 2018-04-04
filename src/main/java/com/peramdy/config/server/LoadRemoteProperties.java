package com.peramdy.config.server;

import com.peramdy.config.client.ZkClient;
import com.peramdy.config.constant.PdEnv;
import com.peramdy.config.constant.ZkConstants;
import com.peramdy.config.execption.PdException;
import com.peramdy.config.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

/**
 * @author pd
 */
public class LoadRemoteProperties {

    private static Logger logger = LoggerFactory.getLogger(LoadRemoteProperties.class);

    public LoadRemoteProperties() {
        ZkClient zkClient = null;
        try {
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
            int timeout = Integer.parseInt(timeoutStr);
            zkClient = new ZkClient.Builder()
                    .addresses(addresses)
                    .timeout(timeout)
                    .build();
            /***init parent node***/
            zkClient.createNode(ZkConstants.PARENT_PATH, env + "/" + appName);
            /***init config value***/
            for (String key : keys) {
                if (!key.equals(ZkConstants.ZK_TIMEOUT) &&
                        !key.equals(ZkConstants.ZK_ADDRESSES) &&
                        !key.equals(ZkConstants.ENV) &&
                        !key.equals(ZkConstants.APP_NAME)) {
                    zkClient.createNode(ZkConstants.PARENT_PATH + "/" + env + "/" + appName + "/" + key, properties.getProperty(key));
                    logger.info("notePath：" + ZkConstants.PARENT_PATH + "/" + env + "/" + appName + "/" + key);
                    logger.info("pdConfig info：" + key + " --> " + properties.getProperty(key));
                }
            }
        } finally {
            if (zkClient != null) {
                zkClient.closeZk();
            }
        }
    }

    public static void main(String[] args) {
        LoadRemoteProperties loadRemoteProperties = new LoadRemoteProperties();
    }

}
