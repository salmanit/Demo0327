package com.charliesong.demo0327.widget

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.charliesong.demo0327.R

class ShowFloatingWindow{

   private var context:Activity

    constructor(context: Activity) {
        this.context = context
    }

    //权限检查，不检查的话会崩掉的。
    private fun checkPermission():Boolean{

        return true
    }
    fun showView(){
        if(!checkPermission()){
            return
        }
        val view=LayoutInflater.from(context).inflate(R.layout.floating_view,null)
        view.findViewById<View>(R.id.btn_close).setOnClickListener {
            context.windowManager.removeView(view)
        }
        var tp=WindowManager.LayoutParams.TYPE_TOAST
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            tp=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        val params=WindowManager.LayoutParams().apply {
            x=200
            y=200
            width=WindowManager.LayoutParams.WRAP_CONTENT
            height=WindowManager.LayoutParams.WRAP_CONTENT
            type=tp
        }
        context.windowManager.addView(view,params)
    }
}