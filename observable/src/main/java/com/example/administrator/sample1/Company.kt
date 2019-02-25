package com.example.administrator.sample1

import java.util.*

/**
 *    author : fenzili
 *    e-mail : 291924028@qq.com
 *    date   : 2019/2/19 20:43
 *    pkn    : com.example.administrator.sample1
 *    desc   :
 */
class Company : Observable() {
    fun postMessage(message: String) {
        setChanged()
        notifyObservers(message)
    }
}