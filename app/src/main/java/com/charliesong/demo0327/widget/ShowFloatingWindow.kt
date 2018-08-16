package com.charliesong.demo0327.widget

import android.app.AppOpsManager
import android.content.Context
import android.graphics.Point
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.view.*
import com.charliesong.demo0327.R
import android.view.Gravity
import android.widget.OverScroller
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.support.annotation.RequiresApi
import com.charliesong.demo0327.base.MyApplication


class ShowFloatingWindow(){

   private var context:Context= MyApplication.getInstance()
    private  var windowManager:WindowManager= MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    companion object {
        var showView:View?=null
    }
    var params=WindowManager.LayoutParams()
    val point=Point()//记录手指down的时候params的 x和y值
    //权限检查，不检查的话会崩掉的。
    private fun checkPermission():Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //AppOpsManager添加于API 19
            return checkOps();
        } else {
            //4.4以下一般都可以直接添加悬浮窗
            return true;
        }
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private  fun  checkOps():Boolean {
        try {
            val obj = context.getSystemService(Context.APP_OPS_SERVICE);
            if (obj == null) {
                return false;
            }
            val localClass = obj.javaClass
            val method = localClass.getMethod("checkOp", Integer.TYPE,Integer.TYPE, String::class.java);
            if (method == null) {
                return false;
            }
            val m = method.invoke(obj, 24,Binder.getCallingUid(),context.getPackageName()) as Int
            //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED ;
        } catch ( e:Exception) {
            e.printStackTrace()
        }
        return false;
    }

    private fun applyCommonPermission(context: Context) {
        try {
            val clazz = Settings::class.java
            val field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION")
            val intent = Intent(field.get(null).toString())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show()
        }

    }

    fun showView(){
        if(!checkPermission()){
            applyCommonPermission(context)
            return
        }
        showView?.apply {
            if(this.parent!=null)
                windowManager.removeViewImmediate(showView)
        }
        showView=LayoutInflater.from(context).inflate(R.layout.floating_view,null).apply {
            findViewById<View>(R.id.btn_close).setOnClickListener {
              windowManager.removeView(showView)
                showView=null
            }
            handleTouch(this)
        }

        var tp=WindowManager.LayoutParams.TYPE_TOAST
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            tp=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
         params=WindowManager.LayoutParams().apply {
            x=110
            y=0
            flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            gravity= Gravity.END or Gravity.CENTER;
            width=WindowManager.LayoutParams.WRAP_CONTENT
            height=WindowManager.LayoutParams.WRAP_CONTENT
            type=tp
        }

        windowManager.addView(showView,params)

    }


    private fun handleTouch(view: View){
        val listener=object :GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
//                println("=======onScroll=========$distanceX===$distanceY=======${params.y}===${e1.rawX}==${e2.rawX}")
                params.x=point.x+(e1.rawX-e2.rawX).toInt()
                params.y=point.y-(e1.rawY-e2.rawY).toInt()
                windowManager.updateViewLayout(view,params)
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                point.x=params.x
                point.y=params.y
                return super.onDown(e)
            }
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val overScroller=OverScroller(view.context)
                overScroller.fling(e2.rawX.toInt(),e2.rawY.toInt(),velocityX.toInt(),velocityY.toInt(),-10000,10000,-10000,10000)
                overScroller.abortAnimation()
                params.x=point.x+(e1.rawX-overScroller.currX).toInt()
                params.y=point.y-(e1.rawY-overScroller.currY).toInt()
                windowManager.updateViewLayout(view,params)
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        }
        val guesture=GestureDetector(context,listener)
        view.setOnTouchListener { v, event ->
            guesture.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }
}