package com.peramdy.config.client;

import com.peramdy.config.execption.PdException;
import com.peramdy.config.server.ZkServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author pd
 */
public class ZkClient {

    private Logger logger = LoggerFactory.getLogger(ZkClient.class);

    private ZooKeeper zooKeeper;

    public ZkClient(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }


    /**
     * 创建node
     *
     * @param path
     * @param value
     * @return
     */
    public String createNode(String path, String value) {
        if (StringUtils.isBlank(path) || StringUtils.isBlank(value)) {
            logger.error("zk create node params : path or value is null");
            logger.info("zk path ：" + path);
            logger.info("zk value：" + value);
            return null;
        }
        String rePath;
        try {
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                List<String> list = zooKeeper.getChildren(path, true);
                if (list.size() > 0) {
                    if (list.contains(value)) {
                        logger.info("zk node :" + path + ",value：" + value + " are existed");
                        return path;
                    }
                } else {
                    Stat stat2 = zooKeeper.setData(path, value.getBytes(), stat.getVersion());
                    return path;
                }
            }
            logger.info("zk path ：" + path);
            logger.info("zk value：" + value);
            rePath = zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("zk create node success：" + rePath);
        } catch (KeeperException e) {
            throw new PdException("zk exception", e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        }
        return rePath;
    }


    /**
     * 查询node值
     *
     * @param path
     * @return
     */
    public String getNodeValue(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("zk node path is null");
            return null;
        }
        String value;
        try {
            logger.info("zk node path ：" + path);
            byte[] bVal = zooKeeper.getData(path, true, null);
            value = new String(bVal);
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        }
        logger.info("zk node value：" + value);
        return value;
    }

    /**
     * @param path
     * @return
     */
    public List<String> getChildrenNodeValue(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("zk child path is null");
            return null;
        }
        List<String> list;
        try {
            logger.info("zk child path ：" + path);
            list = zooKeeper.getChildren(path, false);
            if (list == null || list.size() == 0) {
                logger.info("zk child node is null");
            }
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        }
        return list;
    }

    /**
     * @param path
     * @param value
     * @param version
     * @return
     */
    public Stat setNodeValue(String path, String value, int version) {
        if (StringUtils.isBlank(path) || StringUtils.isBlank(value)) {
            logger.error("zk node path or value is null");
            return null;
        }
        Stat stat;
        try {
            logger.info("zk path：" + path);
            logger.info("zk value：" + value);
            logger.info("zk version：" + version);
            stat = zooKeeper.setData(path, value.getBytes(), version);
            logger.info("zk sate：" + stat);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } catch (KeeperException e) {
            throw new PdException(e);
        }
        return stat;
    }

    /**
     * @param path
     * @param version
     */
    public void deleteNode(String path, int version) {
        if (StringUtils.isBlank(path)) {
            logger.error("zk node path is null");
            return;
        }
        try {
            zooKeeper.delete(path, version);
            logger.info("zk node is deleted，path:" + path);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } catch (KeeperException e) {
            throw new PdException(e);
        }
    }

    /**
     * @param path
     * @return
     */
    public Stat exists(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("zk node path is null");
            return null;
        }
        Stat stat;
        try {
            stat = zooKeeper.exists(path, true);
            logger.info("zk exists stat:" + stat);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } catch (KeeperException e) {
            throw new PdException(e);
        }
        return stat;
    }

    /**
     *
     */
    public void closeZk() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                throw new PdException("关闭zk异常", e);
            }
        }
    }

    /**
     * builder
     */
    public static class Builder {

        private String addresses;
        private int timeout;

        public Builder addresses(String addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public ZkClient build() {
            ZkServer zkServer = ZkServer.getInstance();
            ZooKeeper zooKeeper = zkServer.createZk(addresses, timeout);
            return new ZkClient(zooKeeper);
        }

    }


}
