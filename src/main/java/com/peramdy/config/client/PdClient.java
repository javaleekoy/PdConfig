package com.peramdy.config.client;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * @author pd
 */
public class PdClient {

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.136.130:12181", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.getType() + "----" + event.getPath());
                }
            });
//            String path = zooKeeper.create("/hello", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            System.out.println(path);
            List<String> list = zooKeeper.getChildren("/hello", true);
            list.stream().forEach(str -> System.out.println(str));
            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


    }
}
