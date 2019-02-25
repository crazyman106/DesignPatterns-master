package com.example.administrator.designpatterns

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.administrator.sample1.Coder
import com.example.administrator.sample1.Company

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        test()
    }

    fun test() {
        // 被观察者
        var company: Company = Company()

        // 观察者
        var coder1: Coder = Coder("coder1")
        var coder2: Coder = Coder("coder2")
        var coder3: Coder = Coder("coder3")

        company.apply {
            addObserver(coder1)
            addObserver(coder2)
            addObserver(coder3)
        }.postMessage("公司发布放假通知了!")
    }
}
