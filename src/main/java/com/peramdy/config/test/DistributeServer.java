package com.peramdy.config.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @author peramdy on 2018/7/13.
 */
public class DistributeServer {

    private static String IP = "192.168.136.130:12181";
    private static String PARENT_NODE = "/parentServer";
    private static Integer TIMEOUT = 5000;
    private ZooKeeper zooKeeper = null;

    /**
     * 初始化数据
     *
     * @throws Exception
     */
    public void initServer() throws Exception {
        zooKeeper = new ZooKeeper(IP, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "----" + event.getPath());
            }
        });
    }

    /**
     * 注册
     *
     * @param hostName
     * @throws Exception
     */
    public void register(String hostName) throws Exception {
        Stat stat = zooKeeper.exists(PARENT_NODE, true);
        if (stat == null) {
            zooKeeper.create(PARENT_NODE, hostName.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        zooKeeper.create(PARENT_NODE + "/servers", hostName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("register server : " + hostName);
    }

    /**
     * 业务处理
     *
     * @param hostName
     * @throws InterruptedException
     */
    public void handleBusiness(String hostName) throws InterruptedException {
        System.out.println(hostName + " is working");
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initServer();
        distributeServer.register(args[0]);
        distributeServer.handleBusiness(args[0]);
    }

}
