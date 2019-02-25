package com.example.administrator.sample1

import java.util.Observable
import java.util.Observer

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/19 20:40
 * pkn    : com.example.administrator.sample1
 * desc   :
 */

class Coder(var name: String) : Observer {

    override fun toString(): String {
        return "码农: $name"
    }

    override fun update(observable: Observable, o: Any) {
        System.out.println("coder: " + name + " 内容: " + o.toString())
    }
}
