package com.gysoft.convert.jacob.juc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 17:03
 */
public class ThreadPoolTest2 {
    public static void main(String[] args) {

        /**
         * 线程池实际开发通过线程池工厂的方式去创建，这种方式是不推荐使用的
         */
        ExecutorService service = Executors.newCachedThreadPool();
        Executors.newCachedThreadPool();

    }

}
