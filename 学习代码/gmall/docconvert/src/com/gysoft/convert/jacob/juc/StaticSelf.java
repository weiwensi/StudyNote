package com.gysoft.convert.jacob.juc;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/27 13:37
 */
public class StaticSelf  extends   StaticParent {

    public StaticSelf(){
        System.out.println("子类构造器");
    }
    static{

        System.out.println("子类静态代码块");
    }
    {

        System.out.println("子类普通代码块");
    }

}
