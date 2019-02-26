package com.example.administrator.sample2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/19 21:55
 * pkn    : com.example.administrator.sample2
 * desc   : 事件接收函数的注解类,运用在函数上
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {

    /**
     * 事件的tag,类似于BroadcastReceiver中的Action,事件的标识符
     *
     * @return
     */
    String tag();

    /**
     * 事件执行的线程,默认为主线程
     *
     * @return
     */
    ThreadMode mode() default ThreadMode.MAIN;
}
