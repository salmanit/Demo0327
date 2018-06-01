package com.charliesong.demo0327.classify

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by charlie.song on 2018/5/16.
 */
class RecyclerViewTest:RecyclerView{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if(e.action==MotionEvent.ACTION_DOWN && findChildViewUnder(e.getX(),e.getY())==null){
            return false
        }
        return super.onInterceptTouchEvent(e)
    }
}