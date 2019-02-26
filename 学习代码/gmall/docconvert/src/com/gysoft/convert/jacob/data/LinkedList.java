package com.gysoft.convert.jacob.data;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 11:49
 */
public class LinkedList<E> {


private   class  Node{
    public   E e;
    public  Node  next;
    public Node(E e,Node next){
        this.e=e;
        this.next=next;
    }
    public Node(E e){
        this(e, null);
    }
    public Node(){
        this(null, null);
    }

}

    private   Node dummyHead;
    private  int  size;
    public  LinkedList(){

        dummyHead=new Node();
        size=0;
    }
    //获取链表中元素的个数
    public int getSize(){
        return size;
    }

    /**
     * 判断链表是否为空
     * @return
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     * 在链表中添加新的元素
     */
    public void add(int index,E e) throws IllegalAccessException {
        if(index<0||index>size){
            throw new IllegalAccessException("add faild illegal index.");
        }
        Node prev=dummyHead;
        for(int i=0;i<index;i++){
            prev = prev.next;

            prev.next = new Node(e, prev.next);
            size ++;
        }

    }

    @Test
    public  void test(){
        ArrayList list = new ArrayList();

    }
}
