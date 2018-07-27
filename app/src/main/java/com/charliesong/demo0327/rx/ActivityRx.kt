package com.charliesong.demo0327.rx

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
import com.charliesong.demo0327.util.NormalUtil


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
            val list = File(Environment.getExternalStorageDirectory(), "/").listFiles().filter {
                return@filter it.name.endsWith(".apk")
            }
            println("apk count========${list.size}")
            if (list.size > 0)
               NormalUtil.installCheck(this,list[0])
        }


    }



}