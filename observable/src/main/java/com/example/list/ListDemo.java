package com.example.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 20:39
 * pkn    : com.example
 * desc   :
 * <p>
 * ArrayList:内部是基于动态数组的数据结构,对于get和set，ArrayList觉得优于LinkedList，因为LinkedList要移动指针来查询数据。
 * LinkedList:是基于链表的数据结构,对于新增和删除操作add和remove，LinedList比较占优势，因为ArrayList要改变数组和数据的移动.
 */

public class ListDemo {
    static final int N = 50000;

    static long timeList(List list) {
        long start = System.currentTimeMillis();
        Object o = new Object();
        for (int i = 0; i < N; i++) {
            list.add(0, o);
        }
        return System.currentTimeMillis() - start;
    }

    static long readList(List list) {
        long start = System.currentTimeMillis();
        for (int i =0;i<N;i++){

        }
        return System.currentTimeMillis() - start;
    }

    static List addList(List list) {
        Object o = new Object();
        for (int i = 0; i < N; i++) {
            list.add(0, o);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println("ArrayList添加" + N + "条耗时：" + timeList(new ArrayList()));
        System.out.println("LinkedList添加" + N + "条耗时：" + timeList(new LinkedList()));

        List list1 = addList(new ArrayList<>());
        List list2 = addList(new LinkedList<>());
        System.out.println("ArrayList查找" + N + "条耗时：" + readList(list1));
        System.out.println("LinkedList查找" + N + "条耗时：" + readList(list2));
    }
}
