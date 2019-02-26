package com.gysoft.convert.jacob.data;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 12:04
 */
public class DataMain {
    public static void main(String[] args) throws IllegalAccessException {

        LinkedList<String> linkedList = new LinkedList<>();
        int size = linkedList.getSize();
        System.out.println(size);
        boolean empty = linkedList.isEmpty();
        System.out.println("是否为空"+empty);
        linkedList.add(0,"aaa");
        System.out.println(linkedList.toString());
    }
}
