package com.gysoft.convert.jacob.juc;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/27 13:36
 */
public class StaticParent {

    public   StaticParent(){
        System.out.println("父类加载器");
    }
    static{
        System.out.println("父类静态代码块");
    }
    {
        System.out.println("父类普通代码块");
    }
    public static void test1(){
        System.out.println("父类静态方法");
    }
}
