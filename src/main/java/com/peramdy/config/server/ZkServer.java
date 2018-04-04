package com.peramdy.config.server;

import com.peramdy.config.constant.ZkConstants;
import com.peramdy.config.execption.PdException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author pd
 */
public class ZkServer implements Watcher {

    private Logger logger = LoggerFactory.getLogger(ZkServer.class);

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    /**
     * create zk
     *
     * @return ZooKeeper
     */
    public ZooKeeper createDefault() {
        try {
            logger.info("zk default address：" + ZkConstants.ADDRESS);
            logger.info("zk default timeout：" + ZkConstants.TIMEOUT);
            zooKeeper = new ZooKeeper(ZkConstants.ADDRESS, Integer.parseInt(ZkConstants.TIMEOUT), this);
            countDownLatch.await();
            logger.info("zk default connect success");
        } catch (IOException e) {
            throw new PdException("zk default connect exception", e);
        } catch (InterruptedException e) {
            throw new PdException("countDownLatch exception", e);
        }
        return zooKeeper;
    }


    /**
     * create zk
     *
     * @param addresses
     * @param timeout
     * @return
     */
    public ZooKeeper createZk(String addresses, int timeout) {
        try {
            logger.info("zk address：" + addresses);
            logger.info("zk timeout：" + timeout);
            zooKeeper = new ZooKeeper(addresses, timeout, this);
            countDownLatch.await();
            logger.info("zk connect success");
        } catch (IOException e) {
            throw new PdException("zk connect exception", e);
        } catch (InterruptedException e) {
            throw new PdException("countDownLatch exception", e);
        }
        return zooKeeper;
    }

    /**
     * close zk
     */
    public void closeDefault() {
        if (zooKeeper != null) {
            try {
                logger.info("zk closing...");
                zooKeeper.close();
                logger.info("zk closed");
            } catch (InterruptedException e) {
                throw new PdException("zk close exception", e);
            }
        }
    }

    /**
     * 创建实体类
     *
     * @return
     */
    public static ZkServer getInstance() {
        return new ZkServer();
    }

    @Override
    public void process(WatchedEvent event) {
        logger.info("zk watch state：" + event.getState());
        logger.info("zk watch type：" + event.getType());

        if (event.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        ZkServer zkServer = ZkServer.getInstance();
        zkServer.createDefault();
        zkServer.closeDefault();
    }

}
