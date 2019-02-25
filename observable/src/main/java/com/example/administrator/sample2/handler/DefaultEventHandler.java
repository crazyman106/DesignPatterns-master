package com.example.administrator.sample2.handler;

import com.example.administrator.sample2.Subscription;

import java.lang.reflect.InvocationTargetException;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 22:11
 * pkn    : com.example.administrator.sample2.handler
 * desc   :
 */

public class DefaultEventHandler implements EventHandler {
    @Override
    public void handleEvent(Subscription subscription, Object object) {
        if (subscription == null || subscription.subscriber.get() == null) {
            return;
        }
        try {
            // 调用订阅者中的函数
            subscription.targetMethod.invoke(subscription.subscriber.get(),object);
            // invoke(Object obj, Object... args) obj:调用方法的对象(即方法所在类或者子类的对象),args:方法的参数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
