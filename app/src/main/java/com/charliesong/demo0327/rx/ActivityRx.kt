package com.charliesong.demo0327.rx

import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.showSnackBar
import kotlinx.android.synthetic.main.activity_rx.*

/**
 * Created by charlie.song on 2018/4/2.
 */
class ActivityRx: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)
        btn_ext.setOnClickListener {
            showSnackBar("hello") {
                showToast("my god")
                println("length==========${size}")
            }
        }
        btn_night_mode.setOnClickListener {
            var mode=if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
            delegate.setLocalNightMode(mode)
//            AppCompatDelegate.setDefaultNightMode( mode)
        }
        var str="11111" .also {
            println("str also=====$it") }.apply {
            println("====$length==${this.length}")
        }
    }
}