package com.example.administrator.sample2.matchpolicy;

import com.example.administrator.sample2.EventType;

import java.util.LinkedList;
import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 22:00
 * pkn    : com.example.administrator.sample2.matchpolicy
 * desc   :
 */

public class StrictMatchPolicy implements MatchPolicy {
    @Override
    public List<EventType> findMatchEventTypes(EventType type, Object aEvent) {
        List<EventType> result = new LinkedList<EventType>();
        result.add(type);
        return result;
    }
}
