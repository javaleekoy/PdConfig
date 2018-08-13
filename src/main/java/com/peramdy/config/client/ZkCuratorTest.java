package com.peramdy.config.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.UUID;

/**
 * Created by peramdy on 2018/5/14.
 */
public class ZkCuratorTest {

    public void connection() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.136.130:12181", retryPolicy);
        client.start();
        try {
            Stat stat = client.checkExists().forPath("/hello");
            if (stat == null) {
                String str = client.create().creatingParentContainersIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath("/hello/hi", UUID.randomUUID().toString().getBytes());
                System.out.println(str);
            } else {
                client.delete().forPath("/hello/hi");
//                client.setData().forPath("/hello", UUID.randomUUID().toString().getBytes());
//                byte[] bytes = client.getData().forPath("/hello");
//                System.out.println(new String(bytes, "utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }


    public static void main(String[] args) {
        ZkCuratorTest test = new ZkCuratorTest();
        test.connection();
    }

}
