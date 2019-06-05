package com.example.aidlclient

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.administrator.designpatterns.IMyAidlInterface
import com.example.administrator.designpatterns.Students
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mStub: IMyAidlInterface? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mStub = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("MainActivity", "the onServiceDisconnected")
            mStub = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService()
        aidl.setOnClickListener {
            Log.e("content:", "onClick");
            var msg1 = mStub?.inStudentInfo(Students(1, "Jim", "090415", 18));
            var msg2 = mStub?.outStudentInfo(Students(2, "Lida", "090416", 17));
            var msg3 = mStub?.inOutStudentInfo(Students(3, "Tom", "090417", 16));
            Log.e("content:" + getCurrentProcessName(), msg1 + "\n" + msg2 + "\n" + msg3);
        }
    }

    public fun bindService() {
        var intent: Intent = Intent();
        intent.action = "com.example.administrator.MyServer"
        intent.component = ComponentName("com.example.administrator.designpatterns", "com.example.administrator.designpatterns.MyAidlServer")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private fun getCurrentProcessName(): String? {
        val pid = android.os.Process.myPid()
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in activityManager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName+"-"+processInfo.pid
            }
        }
        return null
    }
}
