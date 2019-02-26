package com.gysoft.convert.jacob.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @Description  countdoenlatch测试 模拟一家人去吃饭
 * @Author DJZ-WWS
 * @Date 2019/2/26 16:21
 */
public class CountDownLatchTest
{

    private  static CountDownLatch  latch=new CountDownLatch(3);

    public static void main(String[] args) throws InterruptedException {

        new  Thread(){
            public void run(){
                try {
                    father();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.start();

        new  Thread(){
            public void run(){
                try {
                    mother();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.start();

        new  Thread(){
            public void run(){
                try {
                    meROle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.start();
      latch.await();

    togetherToEat();



    }
    public static  void father() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("爸爸在路上需要3个小时");

    }
    public static void mother() throws InterruptedException {

    Thread.sleep(2000);
        System.out.println("妈妈在路上需要两个小时");
    }
    public static void meROle() throws InterruptedException {

    Thread.sleep(1000);
        System.out.println("我去路上需要1个小时");
    }
    public static void togetherToEat(){
        System.out.println("家人到齐了 ，开饭吧");
    }
}
