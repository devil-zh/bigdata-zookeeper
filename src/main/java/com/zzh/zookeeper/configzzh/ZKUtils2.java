package com.zzh.zookeeper.configzzh;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: zzh
 * @Date: 2021/6/21 18:01
 * @Description:
 */
public class ZKUtils2 {
    private static ZooKeeper zk;

    private static String address = "192.168.79.3:2181/testConf";

    private static DefaultWatch2 watch = new DefaultWatch2();

    private static CountDownLatch init  =  new CountDownLatch(1);

    public static ZooKeeper getZk(){

        try {
            zk = new ZooKeeper(address,2000,watch);
            watch.setCountDownLatch(init);
            init.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
