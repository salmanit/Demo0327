package com.charliesong.demo0327.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.ViewGroup

/**
 * Created by charlie.song on 2018/4/26.
 */
class  RippleAnimation : View {

    private  constructor(context: Context) : super(context){

    }
    private constructor(view: View):super(view.context){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        this.view=view;
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        decorView=(view.context as Activity).window.decorView as ViewGroup;
        calculateMaxRadius()
        getCacheBitmap()
    }
    private lateinit var view:View;
    private  var duration=1000L;
    private var maxRadius=1f;
    private var currentRadius=1f;
    private var mStartX=0f
    private var mStartY=0f
    private var paint=Paint(Paint.ANTI_ALIAS_FLAG);
    private lateinit var decorView:ViewGroup;
    private var bgBitmap:Bitmap?=null
    companion object {
        fun anchor(view:View):RippleAnimation{
            return  RippleAnimation(view);
        }
    }

    fun setDuration(duration:Long):RippleAnimation{
        this.duration=duration;
        return  this;
    }

    fun startRipple(){
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bgBitmap?.run{
            canvas.drawBitmap(this,0f,0f,null)
            canvas.drawCircle(mStartX, mStartY, currentRadius, paint)
        }
    }
    private fun calculateMaxRadius(){
        var location= IntArray(2)
        view.getLocationOnScreen(location);
        var display=(view.context as Activity).windowManager.defaultDisplay;
        maxRadius=Math.hypot(display.width.toDouble(),display.height.toDouble()).toFloat()//偷懒一下，直接用手机的对角线长度肯定足够拉。
        mStartX=location[0]+view.width/2f
        mStartY=location[1]+view.height/2f;//起始位置可以自己处理，你可以从view的最右边开始，而不是中心
    }
    private fun getCacheBitmap(){
        recycleBP()
        decorView.isDrawingCacheEnabled=true
        bgBitmap=Bitmap.createBitmap(decorView.drawingCache);
        decorView.isDrawingCacheEnabled=false;
    }
    private fun addtoRoot(){
        var layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        decorView.addView(this,layoutParams);
    }
    private fun deleteFromRoot(){
       decorView.removeView(this)
       recycleBP()
    }
    private fun recycleBP(){
        bgBitmap?.run {
            if(!isRecycled){
                recycle()
            }
            bgBitmap=null
        }
    }
    private var isStart=false;
    private fun startAnimation(){

        addtoRoot()
        isStart=true;
        var animator=ValueAnimator.ofFloat(0f,maxRadius)
        animator.setDuration(duration)
        animator.addUpdateListener {
            currentRadius=it.animatedValue as Float
            postInvalidate()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                deleteFromRoot()
                isStart=false
            }
        })
        animator.start()
    }

}