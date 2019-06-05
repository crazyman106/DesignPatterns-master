package com.example.administrator.demo

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

import com.example.administrator.designpatterns.IMyAidlInterface

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/6/5 20:48
 * pkn    : com.example.administrator.demo
 * desc   :
 */

class Test {
    private var mStub: IMyAidlInterface? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mStub = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("xjp", "the onServiceDisconnected")
            mStub = null
        }
    }

}
