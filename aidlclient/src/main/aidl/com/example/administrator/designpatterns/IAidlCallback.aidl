// IAidlCallback.aidl
package com.example.administrator.designpatterns;

import com.example.administrator.designpatterns.Students;

// Declare any non-default types here with import statements

interface IAidlCallback {
    void callback(inout Students student);
}
