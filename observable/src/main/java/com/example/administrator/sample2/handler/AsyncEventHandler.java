package com.example.administrator.sample2.handler;

import android.os.Handler;
import android.os.HandlerThread;

import com.example.administrator.sample2.Subscription;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 22:20
 * pkn    : com.example.administrator.sample2.handler
 * desc   : HandlerThread 该DispatcherThread会开启一个线程,在该线程中会创建一个Looper用来循环该线程中的消息队列
 */

public class AsyncEventHandler implements EventHandler {

    /**
     * 事件分发线程
     */
    DispatcherThread mDispatcherThread;

    /**
     * 事件处理器
     */
    EventHandler mEventHandler = new DefaultEventHandler();

    public AsyncEventHandler() {
        mDispatcherThread = new DispatcherThread(AsyncEventHandler.class.getName());
        mDispatcherThread.start();
    }

    @Override
    public void handleEvent(final Subscription subscription, final Object object) {
        mDispatcherThread.post(new Runnable() {
            @Override
            public void run() {
                mEventHandler.handleEvent(subscription, object);
            }
        });
    }


    class DispatcherThread extends HandlerThread {

        /**
         * 关联了AsyncExecutor消息队列的Handler
         */
        protected Handler mAsyncHandler;

        public DispatcherThread(String name) {
            super(name);
        }

        /**
         * @param runnable 使用handler将runnable添加到任务队列中,该任务将在handler所在的线程中执行
         */
        public void post(Runnable runnable) {
            if (mAsyncHandler == null) {
                throw new NullPointerException("mAsyncHandler == null, please call start() first.");
            }
            // 异步执行任务
            mAsyncHandler.post(runnable);
        }


        /**
         * synchronized:同步
         */
        @Override
        public synchronized void start() {
            super.start();
            mAsyncHandler = new Handler(this.getLooper());
        }
    }
}
