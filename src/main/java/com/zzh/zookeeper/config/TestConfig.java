package com.zzh.zookeeper.config;

import com.zzh.zookeeper.configzzh.WatchCallBack2;
import com.zzh.zookeeper.configzzh.ZKUtils2;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: 马士兵教育
 * @create: 2019-09-20 20:07
 */
public class TestConfig {


    ZooKeeper zk;


    @Before
    public void conn (){
        zk  = ZKUtils.getZK();
    }

    @After
    public void close (){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getConf(){

        WatchCallBack2 watchCallBack = new WatchCallBack2();
        MyConf myConf = new MyConf();
        watchCallBack.setZkCli(zk);
        watchCallBack.setConf(myConf);

        watchCallBack.watchConf();
        //1，节点不存在
        //2，节点存在

        while(true){

            if(myConf.getConf().equals("")){
                System.out.println("conf diu le ......");
                watchCallBack.watchConf();
            }else{
                System.out.println(myConf.getConf());

            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }





}
