package com.gysoft.convert.jacob.juc.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 16:37
 */
public class ThreadPoolExecutorFactory {

    /**
     * corePoolSize 池中所保存的线程数，包括空闲线程。
     */
    private static final int corePoolSize = 3;
    /**
     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。
     */
    private static final int maximumPoolSize = 6;
    /**
     * keepAliveTime -当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间
     */
    private static final int keepAliveTime = 60;

    /**
     * 执行前用于保持任务的队列（缓冲队列）
     */
    private static final int capacity = 300;

    /**
     * 线程池对象
     */
    private static ThreadPoolExecutor threadPoolExecutor = null;

    //构造方法私有化
    private ThreadPoolExecutorFactory(){}

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        if(null == threadPoolExecutor){
            ThreadPoolExecutor t;
            synchronized (ThreadPoolExecutor.class) {
                t = threadPoolExecutor;
                if(null == t){
                    synchronized (ThreadPoolExecutor.class) {
                        t = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.DiscardOldestPolicy());
                    }
                    threadPoolExecutor = t;
                }
            }
        }
        return threadPoolExecutor;
    }

}
