package com.example.administrator.sample2.matchpolicy;


import com.example.administrator.sample2.EventType;

import java.util.List;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/21 21:33
 * pkn    : com.example.administrator.sample2
 * desc   :
 */

public interface MatchPolicy {
    List<EventType> findMatchEventTypes(EventType type, Object aEvent);
}
