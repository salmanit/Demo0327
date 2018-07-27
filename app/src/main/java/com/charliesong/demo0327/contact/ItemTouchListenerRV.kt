package com.charliesong.demo0327.contact

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import com.charliesong.demo0327.util.RippleAnimation

/**
 * Created by charlie.song on 2018/4/8.
 */
class ItemTouchListenerRV:RecyclerView.OnItemTouchListener,GestureDetector.SimpleOnGestureListener{
    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
       var child= rv.findChildViewUnder(e.getX(),e.getY())
        if(child!=null){
            var adapterPosition=rv.getChildAdapterPosition(child)
            var layoutPosition=rv.getChildLayoutPosition(child)
            rv.adapter.notifyItemChanged(adapterPosition,"$adapterPosition/$layoutPosition")
            return true
        }
        return false
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }
    lateinit var rv:RecyclerView
    lateinit var detector:GestureDetector
    constructor(rv: RecyclerView){
        this.rv=rv;
        detector= GestureDetector(rv.context,this)
    }
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        detector.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return  detector.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}