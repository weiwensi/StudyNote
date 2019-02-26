package com.gysoft.convert.jacob.juc.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 16:39
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutorFactory.getThreadPoolExecutor();
        Runnable myRunnable = () -> {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "run");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        executor.execute(myRunnable);
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        System.out.println("---先开启三个线程---");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池线程数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        System.out.println("---再开启三个线程---");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池线程程数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());
        Thread.sleep(8000);
        System.out.println("----8秒之后----");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池线程线程池数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());
        executor.shutdown();
    }

}
