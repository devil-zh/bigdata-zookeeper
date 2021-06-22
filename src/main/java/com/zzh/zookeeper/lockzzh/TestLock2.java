package com.zzh.zookeeper.lockzzh;

import com.zzh.zookeeper.config.ZKUtils;
import com.zzh.zookeeper.lock.WatchCallBack;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author：zzh
 * @Date: 2021/06/22/ 15:28
 * @Description
 */

public class TestLock2 {
    ZooKeeper zkCli ;

    @Before
    public void conn (){
        zkCli  = ZKUtils.getZK();
    }

    @After
    public void close (){
        try {
            zkCli.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getLock() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    WatchCallBack2 watchCallBack = new WatchCallBack2();
                    watchCallBack.setZkCli(zkCli);
                    String threadName = Thread.currentThread().getName();
                    watchCallBack.setThreadName(threadName);
                    //每一个线程：
                    //抢锁
                    watchCallBack.tryLock();
                    //干活
                    System.out.println(threadName+" working...");
                    //释放锁
                    watchCallBack.unLock();


                }
            }.start();

        }
        Thread.sleep(11000);
    }
}
