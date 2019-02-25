package com.example.administrator.sample2;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/20 23:02
 * pkn    : com.example.administrator.sample2
 * desc   : 订阅者方法搜索器
 * <p>
 * 查找订阅者中所有被@Subscriber注解的函数
 * Hunter:猎人,神探,搜寻者
 * annotated:注解的
 */

public class SubscriberMethodHunter {

    /**
     * the event bus's subscriber's map
     * 事件总线的订阅者集合
     * <p>
     * EventType:函数唯一性的对象,通过该对象来查找注册了含有相应类型和tag函数的所有订阅者,并且在接到消息时调用所有订阅者对应的函数.
     * Subscription:订阅者对象,包含订阅者和目标方法
     */
    Map<EventType, CopyOnWriteArrayList<Subscription>> mSubcriberMap;

    /**
     * @param subscriberMap
     */
    public SubscriberMethodHunter(Map<EventType, CopyOnWriteArrayList<Subscription>> subscriberMap) {
        mSubcriberMap = subscriberMap;
    }

    /**
     * 查找订阅对象中的所有订阅函数,订阅函数的参数只能有一个.找到订阅函数之后构建Subscription存储到Map中
     *
     * @param subscriber 订阅对象
     * @return
     */
    public void findSubcribeMethods(Object subscriber) {
        if (mSubcriberMap == null) {
            throw new NullPointerException("the mSubcriberMap is null. ");
        }
        // 获取订阅者类的对象
        Class<?> clazz = subscriber.getClass();
        // 查找类中符合要求的注册方法,直到Object类
        while (!isSystemCalss(clazz.getName())) {
            // 可以得到此 Class 对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法
            Method[] methods = clazz.getDeclaredMethods(); // getMethods只能拿到public方法（包括继承的类或接口的方法）
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                // 根据注解来解析函数
                Subscriber annotation = method.getAnnotation(Subscriber.class);
                if (annotation != null) {
                    // 获取方法参数
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes != null && parameterTypes.length == 1) {
                        Class<?> paramType = convertType(parameterTypes[0]);
                        EventType eventType = new EventType(paramType, annotation.tag());
                        TargetMethod subscribeMethod = new TargetMethod(method, eventType,
                                annotation.mode());
                        subscibe(eventType, subscribeMethod, subscriber);
                    }
                }
            }
            // end for
            // 获取父类,以继续查找父类中符合要求的方法
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 按照EventType存储订阅者列表,这里的EventType就是事件类型,一个事件对应0到多个订阅者.
     *
     * @param event      事件
     * @param method     订阅方法对象
     * @param subscriber 订阅者
     */
    private void subscibe(EventType event, TargetMethod method, Object subscriber) {
        CopyOnWriteArrayList<Subscription> subscriptions = mSubcriberMap.get(event);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<Subscription>();
        }
        Subscription newSubscription = new Subscription(subscriber, method);
        if (subscriptions.contains(newSubscription)) {
            return;
        }
        subscriptions.add(newSubscription);
        // 将事件类型key和订阅者信息存储到map中
        mSubcriberMap.put(event, subscriptions);
    }


    /**
     * remove subscriber's methods from map
     *
     * @param subscriber 订阅者
     */
    public void removeMethodsFromMap(Object subscriber) {
        //从map中查找订阅者,找到之后remove
        Iterator<CopyOnWriteArrayList<Subscription>> iterator = mSubcriberMap.values().iterator();
        while (iterator.hasNext()) {
            CopyOnWriteArrayList<Subscription> subscriptions = iterator.next();
            if (subscriptions != null) {
                List<Subscription> foundSubscriptions = new LinkedList<>();
                Iterator<Subscription> subIterator = subscriptions.iterator();
                while (subIterator.hasNext()) {
                    Subscription subscription = subIterator.next();
                    Object object = subscription.subscriber.get();
                    if (isObjectsEqual(object, subscriber) || object == null) {
                        Log.d("", "### 存储到临时List中 " + subscriber.getClass().getName());
                        foundSubscriptions.add(subscription);
                    }
                }
                // 移除该subscriber的相关的Subscription
                subscriptions.removeAll(foundSubscriptions);
            }
            if (subscriptions == null||subscriptions.size() ==0){
                iterator.remove();
            }
        }
    }

    /**
     * if the subscriber method's paramter type is primitive, convert it to corresponding
     * Object type. for example, int to Integer.
     *
     * @param clazz origin MethodParamter Type
     * @return
     */
    private Class<?> convertType(Class<?> clazz) {
        if (clazz.equals(boolean.class)) {
            clazz = Boolean.class;
        } else if (clazz.equals(int.class)) {
            clazz = Integer.class;
        } else if (clazz.equals(float.class)) {
            clazz = Float.class;
        } else if (clazz.equals(double.class)) {
            clazz = Double.class;
        }
        return clazz;
    }

    private boolean isObjectsEqual(Object cachedObj, Object subscriber) {
        return cachedObj != null
                && cachedObj.equals(subscriber);
    }

    // 系统类名可以以java.或javax.或android.开头,自定义的不行
    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }
}
