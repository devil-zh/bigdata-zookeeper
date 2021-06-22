package com.zzh.zookeeper.configzzh;

import com.zzh.zookeeper.config.MyConf;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zzh
 * @Date: 2021/6/21 18:01
 * @Description:
 */
public class TestConfig2 {
    ZooKeeper zkCli;
    @Before
    public void getZK() {
        zkCli = ZKUtils2.getZk();
    }
    @After
    public void closeZK(){
        try {
            zkCli.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf2() throws InterruptedException {
        WatchCallBack2 watchCallBack = new WatchCallBack2();
        MyConf myConf = new MyConf();
        watchCallBack.setZkCli(zkCli);
        watchCallBack.setConf(myConf);

        /*
        watchCallBack.watchConf();
        if(myConf.getConf()==null||myConf.getConf().equals("")){
            watchCallBack.watchConf();
        }else{
            System.out.println(myConf.getConf());
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i=0;i<3;i++)
        service.submit(()->{
            watchCallBack.watchConf();
            if(myConf.getConf()==null||myConf.getConf().equals("")){
                watchCallBack.watchConf();
            }else{
                System.out.println(Thread.currentThread().getName()+myConf.getConf());
            }
        });

        Thread.sleep(2000);

        service.shutdown();*/

        new Thread(()->{
            watchCallBack.watchConf();
            if(myConf.getConf()==null||myConf.getConf().equals("")){
                watchCallBack.watchConf();
            }else{
                System.out.println(Thread.currentThread().getName()+myConf.getConf());
            }
        },"t1").start();
        new Thread(()->{
            watchCallBack.watchConf();
            if(myConf.getConf()==null||myConf.getConf().equals("")){
                watchCallBack.watchConf();
            }else{
                System.out.println(Thread.currentThread().getName()+myConf.getConf());
            }
        },"t2").start();
        new Thread(()->{
            watchCallBack.watchConf();
            if(myConf.getConf()==null||myConf.getConf().equals("")){
                watchCallBack.watchConf();
            }else{
                System.out.println(Thread.currentThread().getName()+myConf.getConf());
            }
        },"t3").start();

        Thread.sleep(10000);


    }
}
