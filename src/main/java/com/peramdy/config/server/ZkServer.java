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
     * 创建连接
     *
     * @return ZooKeeper
     */
    public ZooKeeper createDefault() {
        try {
            logger.info("zk 地址：" + ZkConstants.ADDRESS);
            logger.info("zk 超时：" + ZkConstants.TIMEOUT);
            zooKeeper = new ZooKeeper(ZkConstants.ADDRESS, ZkConstants.TIMEOUT, this);
            countDownLatch.await();
            logger.info("zk 连接成功");
        } catch (IOException e) {
            throw new PdException("zk 连接异常", e);
        } catch (InterruptedException e) {
            throw new PdException("countDownLatch 异常", e);
        }
        return zooKeeper;
    }

    /**
     * 关闭连接
     */
    public void closeDefault() {
        if (zooKeeper != null) {
            try {
                logger.info("zk 断开...");
                zooKeeper.close();
                logger.info("zk 断开成功");
            } catch (InterruptedException e) {
                throw new PdException("zk 关闭异常", e);
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
        logger.info("zk 监听事件开始：" + event.getState());
        logger.info("zk 监听事件类型：" + event.getType());
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
