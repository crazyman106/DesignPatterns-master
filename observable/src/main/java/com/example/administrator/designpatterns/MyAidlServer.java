package com.example.administrator.designpatterns;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/6/5 20:29
 * pkn    : com.example.administrator.designpatterns
 * desc   :
 */

public class MyAidlServer extends Service {

    IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public int add(int arg1, int arg2) throws RemoteException {
            return arg1 + arg2;
        }

        @Override
        public String inStudentInfo(Students student) throws RemoteException {
            Log.e("inStudentInfo",student.toString());
            String msg = "table1" + "\n" + "----------------------------------------------" + "\n" + "|" + " id " + "|" + " " + "age " + "|" + " name " + "|" + " className " + "|" + "\n" + "----------------------------------------------" + "\n" + "|  " + student.getId() + " " + "|  " + student.getAge() + "  |  " + student.getName() + "   |     " + student.getClassName() + "   | " + "\n" + "----------------------------------------------";
            return msg;
        }

        @Override
        public String outStudentInfo(Students student) throws RemoteException {
            Log.e("outStudentInfo",student.toString());
            String msg = "table2" + "\n" + "----------------------------------------------" + "\n" + "|" + " id " + "|" + " " + "age " + "|" + " name " + "|" + " className " + "|" + "\n" + "----------------------------------------------" + "\n" + "|  " + student.getId() + " " + "|  " + student.getAge() + "  |  " + student.getName() + "   |     " + student.getClassName() + "   | " + "\n" + "----------------------------------------------";
            return msg;
        }

        @Override
        public String inOutStudentInfo(Students student) throws RemoteException {
            Log.e("inOutStudentInfo",student.toString());
            student.setClassName("090411");
            student.setAge(22);
            String msg = "table3" + "\n" + "----------------------------------------------" + "\n" + "|" + " id " + "|" + " " + "age " + "|" + " name " + "|" + " className " + "|" + "\n" + "----------------------------------------------" + "\n" + "|  " + student.getId() + " " + "|  " + student.getAge() + "  |  " + student.getName() + "   |     " + student.getClassName() + "   | " + "\n" + "----------------------------------------------";
            return msg;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName + "-" + processInfo.pid;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyAidlServer", getCurrentProcessName(this));
    }
}
