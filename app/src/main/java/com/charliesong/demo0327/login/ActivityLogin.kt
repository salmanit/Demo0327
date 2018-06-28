package com.charliesong.demo0327.login

import android.graphics.Color
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.util.Log
import android.widget.LinearLayout

class ActivityLogin:BaseActivity(){
    private var oldState=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dp260=dp2px(260).toInt()
        dp100=dp2px(100).toInt()
        layout_login.minimumHeight= (windowManager.defaultDisplay.height-dp260)
        layout_root.viewTreeObserver.addOnGlobalLayoutListener(listener)

    }
    var dp260=260
    var dp100=100
    fun dp2px(dp:Int): Float {
        var displayMetrics=DisplayMetrics()
         windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.density*dp
    }
    override fun onDestroy() {
        super.onDestroy()
        layout_root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
    val listener=object :ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            val screenHeight = layout_root.getRootView().getHeight()
            val myHeight = layout_root.getHeight()
            val heightDiff = screenHeight - myHeight
            Log.e("onGlobalLayout", "screenHeight=$screenHeight")
            Log.e("onGlobalLayout", "myHeight=$myHeight")

            if (heightDiff > dp100) {
                Log.e("onGlobalLayout", "Soft keyboard showing")
                if(oldState){
                    return
                }
                layout_root.smoothScrollBy(0,dp260)
                if(rb1.isChecked){
                    updateState(rb1,rb2,true)
                }else{
                    updateState(rb2,rb1,true)
                }
                oldState=true
            } else {
                Log.e("onGlobalLayout", "Soft keyboard hidden")
                if(!oldState){
                    return
                }
                layout_root.smoothScrollBy(0,-dp260)
                if(rb1.isChecked){
                    updateState(rb1,rb2,false)
                }else{
                    updateState(rb2,rb1,false)
                }
                oldState=false
            }
        }
    }

    //v1是当前选中的那个，v2是另一个没选中的，keyBoardShow表示当前输入法是否是显示状态
    private fun updateState(v1:View,v2:View, keyBoardShow:Boolean){
//        var param= v2.layoutParams as LinearLayout.LayoutParams
//        param.weight=if(keyBoardShow) 0f else 1f
//        v2.layoutParams=param;
        v2.visibility=if(keyBoardShow) View.GONE else View.VISIBLE
        v1.setBackgroundColor(if(keyBoardShow) Color.BLUE else Color.TRANSPARENT)
    }
}