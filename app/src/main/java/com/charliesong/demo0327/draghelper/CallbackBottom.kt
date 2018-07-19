package com.charliesong.demo0327.draghelper

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.NestedScrollingChild
import android.support.v4.widget.ViewDragHelper
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup

class CallbackBottom:ViewDragHelper.Callback{

    var viewGroup:ViewGroup
    var childTop:View
    constructor(viewGroup: ViewGroup) : super() {
        this.viewGroup = viewGroup
        childTop=viewGroup.getChildAt(0)
    }

    override fun tryCaptureView(child: View, pointerId: Int): Boolean {

        println("tryCaptureView======$child======$pointerId")
        return TextUtils.equals("bottom_scale",child.getTag() as CharSequence?)
    }

    override fun onViewDragStateChanged(state: Int) {
        super.onViewDragStateChanged(state)
        println("onViewDragStateChanged=========$state") //STATE_IDLE STATE_DRAGGING STATE_SETTLING
    }

    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
        super.onViewPositionChanged(changedView, left, top, dx, dy)
        println("onViewPositionChanged===============$left/$top/==dx/dy===$dx/$dy")
    }

    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
        super.onViewCaptured(capturedChild, activePointerId)
        println("onViewCaptured================$capturedChild======$activePointerId")
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
        super.onViewReleased(releasedChild, xvel, yvel)
        println("onViewReleased============$xvel==$yvel")
    }

    override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
        super.onEdgeTouched(edgeFlags, pointerId)// 4 2 8
        println("onEdgeTouched====================$edgeFlags========$pointerId")
    }

    override fun onEdgeLock(edgeFlags: Int): Boolean {
        println("onEdgeLock================$edgeFlags")
        return super.onEdgeLock(edgeFlags)
    }

    override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
        super.onEdgeDragStarted(edgeFlags, pointerId)
        println("onEdgeDragStarted===============$edgeFlags=======$pointerId")
    }

    override fun getOrderedChildIndex(index: Int): Int {
        println("getOrderedChildIndex===============$index")
        return super.getOrderedChildIndex(index)
    }

    override fun getViewVerticalDragRange(child: View): Int {
        println("getViewVerticalDragRange=========$child=======${child.top}")
        return if(child.top>400 || child.top<200) 0 else 10
    }

    override fun getViewHorizontalDragRange(child: View): Int {
        println("getViewHorizontalDragRange=========${child.left}")
        return 33
    }

    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
        println("clampViewPositionHorizontal============left/dx====$left/$dx")
        return super.clampViewPositionHorizontal(child, left, dx)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
        println("clampViewPositionVertical==============top/dy=$top=$dy")
        var result=top+dy
            if(result<0){
                result=0
            }
        if(child.canScrollVertically(dy)){
            if(child is NestedScrollingChild){
                child.dispatchNestedScroll(0,0,0,dy, IntArray(2))
            }
            return top
        }

        return result
    }
}