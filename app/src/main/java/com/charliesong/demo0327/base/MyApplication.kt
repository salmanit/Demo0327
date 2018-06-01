package com.charliesong.demo0327.base

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Created by charlie.song on 2018/4/8.
 */
class  MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        myApplication =this
        initLeakCanary()
    }

    fun initLeakCanary(){
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    companion object {
         lateinit var  myApplication: MyApplication
        fun getInstance(): MyApplication {
            return myApplication
        }
    }

}