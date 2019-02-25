package com.example.administrator.sample2.matchpolicy;

import com.example.administrator.sample2.EventType;

import java.util.LinkedList;
import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 21:36
 * pkn    : com.example.administrator.sample2.matchpolicy
 * desc   :
 */

public class DefaultMatchPolicy implements MatchPolicy {
    @Override
    public List<EventType> findMatchEventTypes(EventType type, Object aEvent) {
        Class<?> aEventClass = aEvent.getClass();
        List<EventType> result = new LinkedList<EventType>();
        while (aEventClass != null) {
            result.add(new EventType(aEventClass));
            addInterfaces(result, aEventClass, type.tag);
            aEventClass = aEventClass.getSuperclass();
        }

        return result;
    }

    /**
     * 获取该事件的所有类型
     *
     * @param eventTypes 存储列表
     * @param eventClass 事件实现的所有类
     */
    private void addInterfaces(List<EventType> eventTypes, Class<?> eventClass, String tag) {
        Class<?>[] interfacesClasses = eventClass.getInterfaces();
        for (Class<?> interfaceClass : interfacesClasses) {
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(new EventType(interfaceClass, tag));
                addInterfaces(eventTypes, interfaceClass, tag);
            }
        }
    }
}
