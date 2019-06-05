// IMyAidlInterface.aidl
package com.example.administrator.designpatterns;

import com.example.administrator.designpatterns.Students;
import com.example.administrator.designpatterns.IAidlCallback;

// Declare any non-default types here with import statements
//
// in：参数由客户端设置，或者理解成客户端传入参数值。
// out：参数由服务端设置，或者理解成由服务端返回值。
// inout：客户端输入端都可以设置，或者理解成可以双向通信。
// https://img2018.cnblogs.com/blog/472002/201901/472002-20190108172947611-155545959.png

interface IMyAidlInterface {
    int add(int arg1, int arg2);
    String inStudentInfo(in Students student);
    String outStudentInfo(out Students student);
    String inOutStudentInfo(inout Students student);
    void registerCallback(IAidlCallback cb);
    void unregisterCallback(IAidlCallback cb);
}
