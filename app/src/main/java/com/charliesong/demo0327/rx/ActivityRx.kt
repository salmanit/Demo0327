package com.charliesong.demo0327.rx

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.showSnackBar
import kotlinx.android.synthetic.main.activity_rx.*
import java.io.File
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.v4.content.ContextCompat.startActivity
import android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
import android.view.View
import com.charliesong.demo0327.util.NormalUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by charlie.song on 2018/4/2.
 */
class ActivityRx : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)
        defaultSetTitle("apk down install")
        btn_ext.setOnClickListener {
            showSnackBar("hello") {
                showToast("my god")
                println("length==========${size}")
            }
        }
        btn_night_mode.setOnClickListener {
            var mode = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
            delegate.setLocalNightMode(mode)
//            AppCompatDelegate.setDefaultNightMode( mode)
        }
        btn_install.setOnClickListener {
//            val list = File(Environment.getExternalStorageDirectory(), "/").listFiles().filter {
//                return@filter it.name.endsWith(".apk")
//            }
//            println("apk count========${list.size}")
//            if (list.size > 0)
//               NormalUtil.installCheck(this,list[0])
            stop=!stop
        }


    }
    var stop=false
    @SuppressLint("CheckResult")
    fun observerCreate(v:View){
//       Observable.interval(1,TimeUnit.SECONDS) .subscribe(object :Observer<Long>{
//            var disposable:Disposable?=null
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable=d
//            }
//
//            override fun onNext(t: Long) {
//                println("first============$t")
//                if(stop){
//                    disposable?.dispose()
//                }
//            }
//
//            override fun onError(e: Throwable) {
//            }
//        })
//
//       val second= Observable.interval(2,1,TimeUnit.SECONDS).subscribe({
//            println("scenod==========$it")
//                if(stop){
//                    first.dispose()
//                }
//
//        }
//       ,{},{
//           first.dispose()
//       })
//        val third=Observable.intervalRange(10,25,0,1,TimeUnit.SECONDS)
//            .subscribe({
//                println("third==========$it")
//                if(stop){
//                    second.dispose()
//                }
//            },{},{
//                second.dispose()
//            })

//        val fourth=Observable.fromArray("today","tommorrow","next day").subscribe({
//            println("fourth======$it")
//        })
//
//        val fifth=Observable.just(1,2,4,5,6).subscribe({
//            println("fifth================$it")
//        })
        val  sixth=Observable.concat(
                Observable.intervalRange(10,25,0,1,TimeUnit.SECONDS),
                Observable.interval(2,1,TimeUnit.SECONDS))
                .subscribe({
                    println("sixth==========$it")
                    if(stop){
                        disposable?.dispose()
                    }
                },{},{

                })
        disposable=sixth
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
    var disposable:Disposable?=null
}