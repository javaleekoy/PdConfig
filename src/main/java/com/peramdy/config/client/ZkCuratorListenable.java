package com.peramdy.config.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author pd
 */
public class ZkCuratorListenable {


    /**
     * PathChildrenCache
     * 对指定路径节点的一级子目录监听，不对该节点的操作监听，对其子目录的增删改操作监听
     *
     * @param client
     */
    private static void registerPathchildrenCache(CuratorFramework client) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/hello", true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println(pathChildrenCache);
            }
        });
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            System.out.println("注册成功了！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * NodeCache
     * 对一个节点进行监听，监听事件包括指定路径的增删改操作
     *
     * @param client
     */
    private static void registerNodeCache(CuratorFramework client) {
        NodeCache nodeCache = new NodeCache(client, "/hello", false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("当前节点：" + nodeCache.getCurrentData());
            }
        });
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TreeCache
     * 综合NodeCache和PathChildrenCahce的特性，是对整个目录进行监听，可以设置监听深度。
     *
     * @param client
     */
    private static void registerTreeCache(CuratorFramework client) {
        TreeCache treeCache = TreeCache.newBuilder(client, "/hello").setMaxDepth(2).setCacheData(true).build();
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("treeCacheEvent: " + treeCacheEvent);
            }
        });
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.136.130:12181", retryPolicy);
        client.start();
        registerPathchildrenCache(client);
//        registerNodeCache(client);
//        registerTreeCache(client);
        System.out.println("register watcher end ...");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close();
    }
}
