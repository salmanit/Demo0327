package com.charliesong.demo0327.assitservice

import android.os.Bundle
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.access_activity.*

/**
 * Created by charlie.song on 2018/3/30.
 */
class AccessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.access_activity)

        tv_check.setOnClickListener {
        var result=AssistUtil.isAccessibilitySettingsOn(this,AssistService::class.java)
            showToast("$result")
        }

        tv_go_set.setOnClickListener {
            AssistUtil.goSetService(this)
        }
    }
}