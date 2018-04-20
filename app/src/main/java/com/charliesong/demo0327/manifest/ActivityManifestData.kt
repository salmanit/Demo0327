package com.charliesong.demo0327.manifest

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.R
import java.lang.Exception



/**
 * Created by charlie.song on 2018/4/3.
 */
class ActivityManifestData : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manifest)
        getManifest()
        getApplicationManifest()
        testNothing()


        val leakThread = LeakThread()
        leakThread.start()
    }

    fun getManifest() {
        var packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        if (packageInfo != null) {
            println("packageInfo===============  ${packageInfo.activities} ++ ${packageInfo.applicationInfo} ++${packageInfo.firstInstallTime}++${packageInfo.services}")
        }
    }

    fun getApplicationManifest() {
        var appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        if (appInfo.metaData != null) {
            for (key in appInfo.metaData.keySet()) {

                println("application====== $key===${appInfo.metaData.get(key)}")
            }
        }
    }
companion object {
    class LeakThread : Thread() {
        override fun run() {
            try {
                Thread.sleep((6 * 60 * 1000).toLong())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}

    private var handler=Handler()

    private fun testNothing(){
        handler.postDelayed(object :Runnable{
            override fun run() {
                try {
                    Thread.sleep(2000)
                    println("=====================end")
                }catch (e:Exception){

                }

            }
        },4000)
        println("=====================start")
    }
}
