package com.example.administrator.sample2.handler;

import android.os.Handler;
import android.os.Looper;

import com.example.administrator.sample2.Subscription;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 22:09
 * pkn    : com.example.administrator.sample2.handler
 * desc   : 事件处理在UI线程,通过Handler将事件处理post到UI线程的消息队列
 */

public class UIThreadEventHandler implements EventHandler {
    DefaultEventHandler mEventHandler = new DefaultEventHandler();

    Handler mUIHandler = new Handler(Looper.getMainLooper());

    @Override
    public void handleEvent(final Subscription subscription, final Object object) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                // 在UI线程中处理事件
                mEventHandler.handleEvent(subscription, object);
            }
        });
    }
}
