package com.charliesong.demo0327.assitservice

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.os.Build.VERSION.SDK
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit


/**
 * Created by charlie.song on 2018/3/30.
 */
open class AssistService : AccessibilityService() {
    override fun onInterrupt() {
        sysout("onInterrupt")
    }

    override fun onCreate() {
        super.onCreate()
        sysout("onCreate")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        sysout("onServiceConnected")

        //下边是手动设置监听的信息，也可以xml里配置
//        val serverInfo1=AccessibilityServiceInfo();
//        serverInfo1.eventTypes=AccessibilityEvent.TYPES_ALL_MASK
//        serverInfo1.feedbackType=AccessibilityServiceInfo.FEEDBACK_GENERIC
//        serverInfo1.notificationTimeout=100
//        serverInfo1.packageNames= arrayOf("com.charlie.demo0108","com.magellangps.SmartGPS")
//        serviceInfo=serverInfo1
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        sysout("event=" + event.toString())
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        val pkgName = event.getPackageName().toString()
        var info = event.source

        if (info != null) {
            println("info==========${info.toString()}")
            handleDemo0108(info,event)
            handleRTR(info,event)
        }

        //com.charlie.demo0108:id/tv_index             com.charlie.demo0108:id/lv2
    }


    fun handleRTR(info: AccessibilityNodeInfo, event: AccessibilityEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            println("rtr==${info.windowId}===${info.viewIdResourceName}==${info.isContentInvalid}")
        }
    }

    fun handleDemo0108(info: AccessibilityNodeInfo,event: AccessibilityEvent) {
        val eventType = event.getEventType()
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                println("click========")
                if (TextUtils.equals(Button::class.java.name, info.className)) {
                    if (TextUtils.equals("all", info.text)) {
                        val count = info.parent.childCount;
                        for (i in 0..count - 1) {
                            var childInfo = info.parent.getChild(i);
                            println("$i=========${childInfo.className}")
                        }
                        info.parent.getChild(4).performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }

                var nodes: List<AccessibilityNodeInfo>
                if (Build.VERSION.SDK_INT >= 18) {
                    nodes = info.findAccessibilityNodeInfosByViewId(if (TextUtils.equals(info.className, Button::class.java.name)) "com.charlie.demo0108:id/btn_all" else "com.charlie.demo0108:id/tv_index")
                } else {
                    nodes = info.findAccessibilityNodeInfosByText("on")
                }
                if (nodes != null) {
                    println("nodes size==" + nodes.size)
                    nodes.map {
                        if (Build.VERSION.SDK_INT >= 18) {
                            println("node=" + it.viewIdResourceName)
                            if (TextUtils.equals(it.text, "all")) {

                            } else if (TextUtils.equals(it.text, "off")) {

                            }
                        }
                    }
                }

                //全局事件模拟
//                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)
            }

            else->{
//                println("class name====${info.className}")
//                if(TextUtils.equals(info.className,"android.widget.FrameLayout")){
//                    println("========${info.childCount}====${info.parent?.childCount}")
//                    var childCount=info.childCount;
//
//                    for (i in 0 until childCount) {
//                        var childInfo = info.getChild(i);
//                        println("$i====child=====${childInfo?.className}")
//                    }
//                    var parentChildCount=info.parent?.childCount?:0;
//                    for (i in 0 until parentChildCount) {
//                        var childInfo = info.parent.getChild(i);
//                        println("$i=====parent child====${childInfo?.className}")
//                    }
//                    Single.timer(2000  ,TimeUnit.MICROSECONDS).subscribe {
//                        t: Long? ->
//
//                        println("timer ==============")
//                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    }
//
//                }
            }
        }
    }

    fun sysout(msg: String) {
        println("server=====" + msg)
    }


}