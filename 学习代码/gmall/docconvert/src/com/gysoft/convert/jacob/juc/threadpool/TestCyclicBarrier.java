package com.gysoft.convert.jacob.juc.threadpool;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author DELL
 * @description: 要求：十个运动员要进行百米赛跑，在赛前运动员各自进行自己的热身运动，然后统一起跑，裁判才开始记时。
 * @date       : 2014年9月17日 下午4:22:15
 */
public class TestCyclicBarrier {

    public static void main(String[] args) throws InterruptedException {

        String[] children = {"小明（1号）","小强（2号）","小海（3号）","小龙（4号）","小武（5号）","小勇（6号）","小鹏（7号）","小飞（8号）","小峰（9号）","小涛（10号）"};

        final Random random = new Random(System.currentTimeMillis());

        final CyclicBarrier barrier = new CyclicBarrier(10,new Runnable() {
            public void run() {
                Thread.currentThread().setName("裁判");
                doing("各就各位，预备，跑");

            }
        });


        //让10个运动员动起来
        for(String child : children){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        doing("我要开始做我的热身运动！");
                        int time = random.nextInt(20000);
                        Thread.sleep(time);
                        doing("我已经做完了热身运动！共耗时"+Long.parseLong(time+"")/1000+"秒!");
                        //等待其他线程执行
                        barrier.await();
                        doing("我已经准备好，当前时间是:"+System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            },child).start();
        }
    }

    /**
     * @Description:打印出线程的名字
     * @param msg
     * @returType:void
     */
    public static void doing(String msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }

}