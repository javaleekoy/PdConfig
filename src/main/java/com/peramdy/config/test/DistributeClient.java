package com.peramdy.config.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author peramdy on 2018/7/13.
 */
public class DistributeClient {


    private static String IP = "192.168.136.130:12181";
    private static String PARENT_NODE = "/parentServer";
    private static Integer TIMEOUT = 500000;
    private volatile Set<String> servers;
    private ZooKeeper zooKeeper;

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
                try {
                    getServerList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getServerList() throws Exception {
        List<String> children = zooKeeper.getChildren(PARENT_NODE, true);
        Set<String> set = new HashSet<>();
        children.stream().forEach(child -> {
            try {
                byte[] bytes = zooKeeper.getData(PARENT_NODE + "/" + child, false, null);
                set.add(new String(bytes, "UTF-8"));
                System.out.println();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        servers = set;
        servers.parallelStream().forEach(s -> System.out.println(s));
    }


    public void handleBusiness() throws Exception {
        System.out.println("client is working");
        Thread.sleep(Integer.MAX_VALUE);
    }


    public static void main(String[] args) throws Exception {
        DistributeClient client = new DistributeClient();
        client.initServer();
        client.getServerList();
        client.handleBusiness();
    }

}
