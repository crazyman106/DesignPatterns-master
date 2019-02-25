package com.example.administrator.sample2;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/20 21:53
 * pkn    : com.example.administrator.sample2
 * desc   : 订阅者对象,包含订阅者和目标方法
 */

public class Subscription {
    /**
     * 订阅者对象
     */
    public Reference<Object> subscriber;
    /**
     * 接收者(订阅者)的方法
     */
    public Method targetMethod;
    /**
     * 执行事件的线程模型
     */
    public ThreadMode threadMode;
    /**
     * 事件类型
     */
    public EventType eventType;

    public Subscription(Object subscriber, TargetMethod targetMethod) {
        this.subscriber = new WeakReference<Object>(subscriber);
        this.targetMethod = targetMethod.method;
        this.threadMode = targetMethod.threadMode;
        this.eventType = targetMethod.eventType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subscriber == null) ? 0 : subscriber.hashCode());
        result = prime * result + ((targetMethod == null) ? 0 : targetMethod.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subscription other = (Subscription) obj;
        if (subscriber.get() == null) {
            if (other.subscriber.get() != null)
                return false;
        } else if (!subscriber.get().equals(other.subscriber.get()))
            return false;
        if (targetMethod == null) {
            if (other.targetMethod != null)
                return false;
        } else if (!targetMethod.equals(other.targetMethod))
            return false;
        return true;
    }

}
