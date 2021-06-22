package com.zzh.zookeeper.lockzzh;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author：zzh
 * @Date: 2021/06/22/ 15:30
 * @Description
 */

public class WatchCallBack2 implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback,AsyncCallback.StatCallback {
    ZooKeeper zkCli;
    String threadName;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String pathName;

    public ZooKeeper getZkCli() {
        return zkCli;
    }

    public void setZkCli(ZooKeeper zkCli) {
        this.zkCli = zkCli;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void tryLock(){
        try {
            zkCli.create("/lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,"tryLock");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zkCli.delete(pathName,-1);
            System.out.println(threadName + " over work....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        //如果第一个哥们，那个锁释放了，其实只有第二个收到了回调事件！！
        //如果，不是第一个哥们，某一个，挂了，也能造成他后边的收到这个通知，从而让他后边那个跟去watch挂掉这个哥们前边的。。。
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zkCli.getChildren("/",false,this ,"挂了");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        Collections.sort(children);
        int index = children.indexOf(pathName.substring(1));
        //是不是第一个
        if(index == 0){
            //yes
            System.out.println(threadName +" i am first....");
            try {
                zkCli.setData("/",threadName.getBytes(),-1);
                countDownLatch.countDown();

            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            //no
            zkCli.exists("/"+children.get(index-1),this,this,"监控前一个");
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name!=null){
            System.out.println(threadName  +"  create node : " +  name );
            pathName = name ;
            zkCli.getChildren("/",false,this ,"createNode");
        }

    }


    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }
}
