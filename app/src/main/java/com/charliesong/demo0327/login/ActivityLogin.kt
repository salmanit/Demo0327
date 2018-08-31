package com.charliesong.demo0327.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

@Deprecated("")
class ActivityLogin:BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       SoftKeyBoardListener.setListener(this,object :SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
           override fun keyBoardShow(height: Int) {
            startUIAnimator(true)
           }

           override fun keyBoardHide(height: Int) {
               tv_title.visibility=View.GONE
               startUIAnimator(false)
           }
       })
        tv_title.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tv_title.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                tv_title.setHeight(dp2px(60).toInt() + getStatusHeight(this@ActivityLogin))
            }
        })
    }

    private fun startUIAnimator(softInputShow:Boolean){
        ValueAnimator.ofFloat(0f,1f)
                .apply {
                    duration = 500
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            if(softInputShow){
                                tv_title.visibility=View.VISIBLE
                            }
                        }
                    })
                    addUpdateListener {
                        var value = it.animatedValue as Float
                        if(!softInputShow){
                            value=1-value
                        }
                        rv_root.translationY =( -dp2px(241)) * value
                        rg.alpha=1-value
                    }
                    start()
                }
    }

    fun dp2px(dp:Int): Float {
        var displayMetrics=DisplayMetrics()
         windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.density*dp
    }
}