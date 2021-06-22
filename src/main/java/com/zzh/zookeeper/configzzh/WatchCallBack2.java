package com.zzh.zookeeper.configzzh;

import com.zzh.zookeeper.config.MyConf;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: zzh
 * @Date: 2021/6/22 10:15
 * @Description:
 */
public class WatchCallBack2 implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    ZooKeeper zkCli;
    MyConf conf;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZooKeeper getZkCli() {
        return zkCli;
    }

    public void setZkCli(ZooKeeper zkCli) {
        this.zkCli = zkCli;
    }

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public void watchConf() {
        //查看文件是否存在
        zkCli.exists("/AppConf", this, this, "exists???");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //getData回调
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if(data != null ){
            String config = new String(data);
            conf.setConf(config);
            countDownLatch.countDown();
        }
    }

    //exists回调，存在取数据
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat != null){
            zkCli.getData("/AppConf",this,this,"数据存在吗");
        }else {
            System.out.println(path+"配置文件丢失");
        }
       /* System.out.println("path: "+path);
        System.out.println("ctx: "+ctx.toString());
        System.out.println("rc: "+rc);
        System.out.println("stat: "+stat);*/

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zkCli.getData("/AppConf",this,this,"sdfs");
                break;
            case NodeDeleted:
                //容忍性
                conf.setConf("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zkCli.getData("/AppConf",this,this,"文件修改了");
                break;
            case NodeChildrenChanged:
                break;
        }
    }
}
