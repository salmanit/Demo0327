package com.charliesong.demo0327.java

import android.os.Bundle
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_java_knowledge.*
import kotlin.concurrent.thread

class JavaKnowledgeActivity : BaseActivity() {

    val obj = Object()
    var loop = true;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_java_knowledge)

        defaultSetTitle("java action learn")

        btn_start_thread.setOnClickListener {
            thread {
                synchronized(obj) {
                    while (loop) {
                        println("thread=======${Thread.currentThread().name}")
                        obj.wait()
                    }
                }
            }
        }
        btn_notify.setOnClickListener {
            synchronized(obj) {

                obj.notify()
            }
        }
        btn_notify_all.setOnClickListener {
            synchronized(obj) {
                obj.notifyAll()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loop=false
        synchronized(obj) {
            obj.notifyAll()
        }
    }
}
