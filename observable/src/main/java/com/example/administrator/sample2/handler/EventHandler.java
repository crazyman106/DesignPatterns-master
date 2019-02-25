package com.example.administrator.sample2.handler;

import com.example.administrator.sample2.Subscription;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 22:07
 * pkn    : com.example.administrator.sample2
 * desc   :
 * <p>
 * 事件处理接口
 */

public interface EventHandler {

    /**
     * @param subscription 订阅对象
     * @param object       待处理的事件
     */
    void handleEvent(Subscription subscription, Object object);
}
