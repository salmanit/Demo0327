package com.charliesong.demo0327.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.widget.OverScroller

class MyButton:AppCompatButton{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    var scroller:OverScroller?=null
    fun startScroll(moveX:Int,duration:Int){
        if(scroller==null){
            scroller=OverScroller(context)
        }else{
            scroller?.abortAnimation()
        }
        scroller!!.startScroll(translationX.toInt(),top,moveX,0,duration)
        postInvalidateDelayed( ValueAnimator.getFrameDelay())
    }

    override fun computeScroll() {
        super.computeScroll()
        if(scroller?.computeScrollOffset() == true){
            translationX=scroller!!.currX*1.0f
            postInvalidateDelayed( ValueAnimator.getFrameDelay())
        }

    }
}