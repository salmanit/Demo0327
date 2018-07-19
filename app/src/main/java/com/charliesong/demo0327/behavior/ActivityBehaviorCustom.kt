package com.charliesong.demo0327.behavior

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.NestedScrollingChild
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.OverScroller
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.util.UtilNormal
import kotlinx.android.synthetic.main.activity_behavor_custom.*
import kotlin.concurrent.thread

class ActivityBehaviorCustom : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavor_custom)


        rv_bottom.apply {
            layoutManager=LinearLayoutManager(this@ActivityBehaviorCustom)

            adapter=object :BaseRvAdapter<String>(){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_text
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                   holder.setText(R.id.tv_content,"$position  test data")
                }

                override fun getItemCount(): Int {
                    return 30
                }
            }

        }

        button3.setOnClickListener {
            countDownTimer?.cancel()
            countDownTimer?.onFinish()
            countDownTimer = UtilNormal.startCountDown(button3, 5000 + 50)
        }
        //scroll 简单测试
        button4.setOnClickListener {
            showToast("click====${button4.left}=====${button4.translationX}")
            button4.startScroll(move, 2000)
            move = -move
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    var move = 400
    var countDownTimer: CountDownTimer? = null
}