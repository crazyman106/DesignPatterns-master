package com.example.administrator.sample2;

import android.util.Log;

import com.example.administrator.sample2.handler.AsyncEventHandler;
import com.example.administrator.sample2.handler.DefaultEventHandler;
import com.example.administrator.sample2.handler.EventHandler;
import com.example.administrator.sample2.handler.UIThreadEventHandler;
import com.example.administrator.sample2.matchpolicy.DefaultMatchPolicy;
import com.example.administrator.sample2.matchpolicy.MatchPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/19 22:02
 * pkn    : com.example.administrator.sample2
 * desc   :
 * <p>
 * EventBus是AndroidEventBus框架的核心类,也是用户的入口,它存储了用户注册的订阅者信息和函数,
 * 事件类型和该事件对应的tag标识一个种类的事件 {@see EventType},每一种事件对应有一个或者多个订阅者{@seeSubscription}
 * 订阅者中的订阅函数通过(@see Subscriber)注解来标识tag和事件线程模型,这样使得用户体验较为友好,代码也更加整洁
 * <p>
 * 用户需要在发布事件时通过{@register(Object)}方法将订阅者注册到EventBus中,EventBus会自动解析该订阅者中使用了{@see Subscriber}标识的函数,并且将它们以{@see EventType}为key,以{@see Subscription}列表为value存储在map中,当用户post一个事件时,通过事件到map中查到对应的订阅者,然后按照订阅函数的线程模型将函数执行到对应的线程中.
 * <p>
 * 最后在不需要订阅事件时,调用{@see unregister(object)}函数注销该对象,避免内存泄露
 * <p>
 * 例如在Activity或Fragment中的onDestroy()函数中注销对该对象的订阅
 * <p>
 * 注意:如果发布的事件的参数类型是订阅事件参数的子类,订阅函数默认也会被执行.例如你在订阅函数中订阅的是List<String>类型的事件,但是在发布时发布的是ArrayList<String>事件,因此List<String>是一个泛型抽象,而ArrayList<String>才是具体实现,因此这种情况下,订阅函数也会被执行.如果你需要订阅函数能够接受到的事件类型必须严格匹配,你可以构造一个EventBusConfig对象,然后设置MatchPolicy,然后在使用事件总线之前使用该EventBugConfig来初始化事件总线.
 * <p>
 * EventBusConfig config = new EventBusConfig();
 * config.setMatchPlicy(new StrictMatchPolicy())
 * EventBus.getDefault().initWithConfig(config)
 */

public final class EventBus {

    /**
     * default descriptor
     */
    private static final String DESCRIPTOR = EventBus.class.getSimpleName();

    /**
     * 事件总线描述符描述符
     */
    private String mDesc = DESCRIPTOR;

    /**
     * EventType-Subcriptions map
     * ConcurrentHashMap:线程安全，效率提升
     * CopyOnWriteArrayList:线程安全
     * <p>
     * mSubcriberMap(函数唯一性(事件的参数类型和tag标签):订阅者对象(包含订阅者和目标方法)集合)
     */
    private Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap = new ConcurrentHashMap<>();

    /**
     * 事件类型:同步安全
     */
    private List<EventType> mStickyEvents = Collections.synchronizedList(new LinkedList<EventType>());

    /**
     * the thread local event queue,every single thread has it's own queue
     */
    ThreadLocal<Queue<EventType>> mLocalEvents = new ThreadLocal<Queue<EventType>>() {
        @Override
        protected Queue<EventType> initialValue() {
            return new ConcurrentLinkedQueue<EventType>();
        }
    };
    /**
     * the event dispatcher
     */
    private EventDispatcher mDispatcher = new EventDispatcher();
    /**
     * the subscriber method hunter, find all of the subscriber's methods
     * annotated with @Subcriber
     */
    SubscriberMethodHunter mMethodHunter = new SubscriberMethodHunter(mSubscriberMap);

    /**
     * The Default EventBus instance
     */
    private static EventBus sDefaultBus;

    /**
     * private Constructor
     */
    private EventBus() {
        this(DESCRIPTOR);
    }

    /**
     * constructor with desc
     *
     * @param desc the descriptor of eventbus
     */
    public EventBus(String desc) {
        mDesc = desc;
    }

    public static EventBus getsDefault() {
        if (sDefaultBus == null) {
            synchronized (EventBus.class) {
                if (sDefaultBus == null) {
                    sDefaultBus = new EventBus();
                }
            }
        }
        return sDefaultBus;
    }

    /**
     * register a subscriber into the mSubcriberMap, the key is subscriber's
     * method's name and tag which annotated with {@see Subcriber}, the value is
     * a list of Subscription.
     *
     * @param subscriber the target subscriber
     */
    public void register(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        synchronized (this) {
            mMethodHunter.findSubcribeMethods(subscriber);
        }
    }

    /**
     * 以sticky的形式注册,则会在注册成功之后迭代所有的sticky事件
     *
     * @param subscriber
     */
    public void registerSticky(Object subscriber) {
        this.register(subscriber);
        // 处理sticky事件
        mDispatcher.dispatchStickyEvents(subscriber);
    }

    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        synchronized (this) {
            mMethodHunter.removeMethodsFromMap(subscriber);
        }
    }

    public void post(Object event) {
        post(event, EventType.DEFAULT_TAG);
    }

    /**
     * 发布事件
     *
     * @param event 要发布的事件
     * @param tag   事件的tag, 类似于BroadcastReceiver的action
     */
    public void post(Object event, String tag) {
        if (event == null) {
            Log.e(this.getClass().getSimpleName(), "The event object is null");
            return;
        }
        // 将该EventType添加到Queue中
        mLocalEvents.get().offer(new EventType(event.getClass(), tag));
        mDispatcher.dispatchEvent(event);
    }

    /**
     * 发布Sticky事件,tag为EventType.DEFAULT_TAG
     *
     * @param event
     */
    public void postSticky(Object event) {
        postSticky(event, EventType.DEFAULT_TAG);
    }

    public void postSticky(Object event, String tag) {
        if (event == null) {
            Log.e(this.getClass().getSimpleName(), "The event object is null");
            return;
        }
        EventType eventType = new EventType(event.getClass(), tag);
        eventType.event = event;
        mStickyEvents.add(eventType);
    }

    public void removeStickyEvent(Class<?> eventClass) {
        removeStickyEvent(eventClass, EventType.DEFAULT_TAG);
    }

    public void removeStickyEvent(Class<?> eventClass, String tag) {
        Iterator<EventType> iterator = mStickyEvents.iterator();
        while (iterator.hasNext()) {
            EventType eventType = iterator.next();
            if (eventType.paramClass.equals(eventClass)
                    && eventType.tag.equals(tag)) {
                iterator.remove();
            }
        }
    }

    public List<EventType> getStickyEvents() {
        return mStickyEvents;
    }

    /**
     * 设置订阅函数匹配策略
     *
     * @param policy 匹配策略
     */
    public void setMatchPolicy(MatchPolicy policy) {
        mDispatcher.mMatchPolicy = policy;
    }

    /**
     * 设置执行在UI线程的事件处理器
     *
     * @param handler
     */
    public void setUIThreadEventHandler(EventHandler handler) {
        mDispatcher.mUIThreadEventHandler = handler;
    }

    /**
     * 设置执行在post线程的事件处理器
     *
     * @param handler
     */
    public void setPostThreadHandler(EventHandler handler) {
        mDispatcher.mPostThreadHandler = handler;
    }

    /**
     * 设置执行在异步线程的事件处理器
     *
     * @param handler
     */
    public void setAsyncEventHandler(EventHandler handler) {
        mDispatcher.mAsyncEventThreadHandler = handler;
    }

    /**
     * 返回订阅map
     *
     * @return
     */
    public Map<EventType, CopyOnWriteArrayList<Subscription>> getSubscriberMap() {
        return mSubscriberMap;
    }

    /**
     * 获取等待处理的事件队列
     *
     * @return
     */
    public Queue<EventType> getEventQueue() {
        return mLocalEvents.get();
    }

    /**
     * clear the events and subcribers map
     */
    public void clear() {
        mLocalEvents.get().clear();
        mSubscriberMap.clear();
    }

    /**
     * get the descriptor of EventBus
     *
     * @return the descriptor of EventBus
     */
    public String getDescriptor() {
        return mDesc;
    }

    public EventDispatcher getDispatcher() {
        return mDispatcher;
    }

    /**
     * 事件分发器
     */
    private class EventDispatcher {

        EventHandler mUIThreadEventHandler = new UIThreadEventHandler();

        EventHandler mAsyncEventThreadHandler = new AsyncEventHandler();
        /**
         * 哪个线程执行的post,接收方法就执行在哪个线程
         */
        EventHandler mPostThreadHandler = new DefaultEventHandler();

        /**
         * 缓存一个事件类型对应的可EventType列表
         */
        private Map<EventType, List<EventType>> mCacheEventTypes = new ConcurrentHashMap<>();

        /**
         * 事件匹配策略,根据策略来查找对应的EventType集合
         */
        MatchPolicy mMatchPolicy = new DefaultMatchPolicy();

        /**
         * 分发事件
         *
         * @param object
         */
        void dispatchEvent(Object object) {
            Queue<EventType> eventsQueue = mLocalEvents.get();
            while (eventsQueue.size() > 0) {
                deliveryEvent(eventsQueue.poll(), object);// Queue.poll():将队列的头部删除,并且该函数返回该头部对象;如果改队列为空返回null
            }
        }

        /**
         * 根据aEvent查找到所有匹配的集合,然后处理事件
         *
         * @param eventType
         * @param object
         */
        void deliveryEvent(EventType eventType, Object object) {
            // 如果有缓存则直接从缓存中取
            List<EventType> eventTypes = getMatchedEventType(eventType, object);
            for (EventType eventType1 : eventTypes) {
                handleEvent(eventType1, object);
            }
        }


        private List<EventType> getMatchedEventType(EventType eventType, Object object) {
            List<EventType> eventTypes = null;
            // 如果有缓存则直接从缓存中取
            if (mCacheEventTypes.containsKey(eventType)) {
                eventTypes = mCacheEventTypes.get(eventType);
            } else {
                eventTypes = mMatchPolicy.findMatchEventTypes(eventType, object);
            }
            return eventTypes != null ? eventTypes : new ArrayList<EventType>();
        }

        /**
         * 处理单个事件
         *
         * @param eventType
         * @param aEvent
         */
        private void handleEvent(EventType eventType, Object aEvent) {
            List<Subscription> subscriptions = mSubscriberMap.get(eventType);
            if (subscriptions == null) {
                return;
            }
            for (Subscription subscription : subscriptions) {
                ThreadMode threadMode = subscription.threadMode;
                EventHandler eventHandler = getEventHandler(threadMode);
                eventHandler.handleEvent(subscription, aEvent);
            }
        }

        void dispatchStickyEvents(Object subscriber) {
            for (EventType eventType : mStickyEvents) {
                handleStickyEvent(eventType, subscriber);
            }
        }

        /**
         * 处理单个Sticky事件
         *
         * @param eventType
         * @param subscriber
         */
        private void handleStickyEvent(EventType eventType, Object subscriber) {
            List<EventType> matchedEventType = getMatchedEventType(eventType, subscriber);
            // 事件
            Object event = eventType.event;
            for (EventType foundEventType : matchedEventType) {
                Log.e("", "### 找到的类型 : " + foundEventType.paramClass.getSimpleName()
                        + ", event class : " + event.getClass().getSimpleName());
                List<Subscription> subscriptions = mSubscriberMap.get(foundEventType);
                if (subscriptions == null) {
                    continue;
                }
                for (Subscription subscription : subscriptions) {
                    ThreadMode threadMode = subscription.threadMode;
                    EventHandler eventHandler = getEventHandler(threadMode);
                    // 如果订阅者为空,那么该sticky事件分发给所有订阅者.否则只分发给该订阅者'
                    /**
                     * 有两个Class类型的类象，一个是调用isAssignableFrom方法的类对象（后称对象a），以及方法中作为参数的这个类对象（称之为对象b），这两个对象如果满足以下条件则返回true，否则返回false：
                     * a对象所对应类信息是b对象所对应的类信息的父类或者是父接口，简单理解即a是b的父类或接口
                     * a对象所对应类信息与b对象所对应的类信息相同，简单理解即a和b为同一个类或同一个接口
                     */
                    if (isTarget(subscription, subscriber)
                            && (subscription.eventType.equals(foundEventType)
                            || subscription.eventType.paramClass
                            .isAssignableFrom(foundEventType.paramClass))) {
                        // 处理事件
                        eventHandler.handleEvent(subscription, event);
                    }
                }
            }
        }
        /**
         * 如果传递进来的订阅者不为空,那么该Sticky事件只传递给该订阅者(注册时),否则所有订阅者都传递(发布时).
         *
         * @param item
         * @param subscriber
         * @return
         */
        private boolean isTarget(Subscription item, Object subscriber) {
            Object cacheObject = item.subscriber != null ? item.subscriber.get() : null;
            return subscriber == null || (subscriber != null
                    && cacheObject != null && cacheObject.equals(subscriber));
        }

        private EventHandler getEventHandler(ThreadMode mode) {
            if (mode == ThreadMode.ASYNC) {
                return mAsyncEventThreadHandler;
            }
            if (mode == ThreadMode.POST) {
                return mPostThreadHandler;
            }
            return mUIThreadEventHandler;
        }
    }
}
