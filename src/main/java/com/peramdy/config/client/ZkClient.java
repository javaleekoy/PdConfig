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

    /**
     * 创建node
     *
     * @param path
     * @param value
     * @return
     */
    public String createNode(String path, String value) {
        if (StringUtils.isBlank(path) || StringUtils.isBlank(value)) {
            logger.error("zk 创建node的path 或 value 为空了");
            logger.info("zk 创建node-path ：" + path);
            logger.info("zk 创建 node-value：" + value);
            return null;
        }
        String rePath;
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        try {
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                logger.info("zk 创建的node：" + path + "已经存在了");
                return path;
            }
            logger.info("zk 创建node-path ：" + path);
            logger.info("zk 创建node-value：" + value);
            rePath = zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("zk 创建node成功：" + rePath);
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
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
            logger.error("zk 查询node的path 为空了");
            return null;
        }
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        String value;
        try {
            logger.info("zk 查询node的path为：" + path);
            byte[] bVal = zooKeeper.getData(path, true, null);
            value = new String(bVal);
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
        }
        logger.info("zk 查询node值为：" + value);
        return value;
    }


    public String createTempChildNode(String parentPath, String path, String value) {
        if (StringUtils.isBlank(parentPath) || StringUtils.isBlank(path) || StringUtils.isBlank(value)) {
            logger.error("zk 创建临时子节点 parentPath 或 path 或 value 为空了");
            logger.info("zk 创建临时子节点 parentPath ：" + parentPath);
            logger.info("zk 创建临时子节点 path ：" + path);
            logger.info("zk 创建临时子节点 value：" + value);
            return null;
        }
        String rePath;
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        try {
            Stat stat = zooKeeper.exists(parentPath, true);
            if (stat == null) {
                logger.info("zk 创建临时子节点，父节点" + parentPath + "不存在了");
                return null;
            }
            logger.info("zk 创建临时子节点 parentPath ：" + parentPath);
            logger.info("zk 创建临时子节点 path ：" + path);
            logger.info("zk 创建临时子节点 value：" + value);
            rePath = zooKeeper.create(parentPath + path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("zk 创建临时子节点成功：" + rePath);
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
        }
        return rePath;
    }


    public String createChildNode(String parentPath, String path, String value) {
        if (StringUtils.isBlank(parentPath) || StringUtils.isBlank(path) || StringUtils.isBlank(value)) {
            logger.error("zk 创建子节点 parentPath 或 path 或 value 为空了");
            logger.info("zk 创建子节点 parentPath ：" + parentPath);
            logger.info("zk 创建子节点 path ：" + path);
            logger.info("zk 创建子节点 value：" + value);
            return null;
        }
        String rePath;
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        try {
            Stat parentStat = zooKeeper.exists(parentPath, true);
            if (parentStat == null) {
                logger.info("zk 创建子节点，父节点" + parentPath + "不存在了");
                return null;
            }

            Stat childStat = zooKeeper.exists(parentPath + path, true);
            if (childStat == null) {
                logger.info("zk 创建子节点" + parentPath + path + "不存在了");
                return null;
            }
            logger.info("zk 创建子节点 parentPath ：" + parentPath);
            logger.info("zk 创建子节点 path ：" + path);
            logger.info("zk 创建子节点 value：" + value);
            rePath = zooKeeper.create(parentPath + path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("zk 创建子节点成功：" + rePath);
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
        }
        return rePath;
    }

    /**
     * @param path
     * @return
     */
    public List<String> getChildrenNodeValue(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("zk 查询children的path 为空了");
            return null;
        }
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        List<String> list;
        try {
            logger.info("zk 查询children的path为：" + path);
            list = zooKeeper.getChildren(path, true);
            if (list == null || list.size() == 0) {
                logger.info("zk 查询children的list为：null");
            }
        } catch (KeeperException e) {
            throw new PdException(e);
        } catch (InterruptedException e) {
            throw new PdException(e);
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
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
            logger.error("zk 更新node的path　或　value 为空了");
            return null;
        }
        Stat stat = null;
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        try {
            logger.info("zk 更新node的path为：" + path);
            logger.info("zk 更新node的value为：" + value);
            logger.info("zk 更新node的version为：" + version);
            stat = zooKeeper.setData(path, value.getBytes(), version);
            logger.info("zk 更新node的sate：" + stat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } finally {
            if (zooKeeper != null) {
                zkServer.closeDefault();
            }
        }
        return stat;
    }

    /**
     * @param path
     * @param version
     */
    public void deleteNode(String path, int version) {
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
        try {
            zooKeeper.delete(path, version);
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
        ZkServer zkServer = ZkServer.getInstance();
        ZooKeeper zooKeeper = zkServer.createDefault();
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


    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient();
        zkClient.createNode("/peramdy", "zk today is thursday");
        zkClient.getNodeValue("/peramdy");
        zkClient.setNodeValue("/peramdy", "zk hello", -1);
        zkClient.createTempChildNode("/peramdy", "/one", "hello one");
        zkClient.createChildNode("/peramdy", "/two", "hello two");
        zkClient.getChildrenNodeValue("/peramdy");
        zkClient.getNodeValue("/peramdy");
        zkClient.exists("/peramdy");
        zkClient.deleteNode("/peramdy/two", -1);
        zkClient.deleteNode("/peramdy", -1);

    }

}
