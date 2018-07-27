package com.charliesong.demo0327.draghelper

import android.support.v4.widget.ViewDragHelper
import android.view.View
import android.view.ViewGroup

class CallbackBottom:ViewDragHelper.Callback{

    var viewGroup:ViewGroup
    var dragHelper:ViewDragHelper
    constructor(viewGroup: ViewGroup,dragHelper: ViewDragHelper) : super() {
        this.viewGroup = viewGroup
        this.dragHelper=dragHelper
        this.dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }

    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
        println("tryCaptureView======$child======$pointerId")
        return false
    }

    override fun onViewDragStateChanged(state: Int) {
        super.onViewDragStateChanged(state)
        println("onViewDragStateChanged=========$state") //STATE_IDLE STATE_DRAGGING STATE_SETTLING
        if(state==ViewDragHelper.STATE_IDLE){
            if(viewGroup.childCount>0){
                if(viewGroup.getChildAt(0).left>0){
                    //关闭页面
                }
            }
        }
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
        if(xvel>500|| releasedChild.left>viewGroup.width/2){
            dragHelper.settleCapturedViewAt(viewGroup.width,releasedChild.top)
        }else{
            dragHelper.settleCapturedViewAt(0,releasedChild.top)
        }
        viewGroup.postInvalidate()
    }

    override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
        super.onEdgeTouched(edgeFlags, pointerId)// 4 2 8
        println("onEdgeTouched====================$edgeFlags========$pointerId")
        if(edgeFlags==ViewDragHelper.EDGE_LEFT&&viewGroup.childCount>0){
            dragHelper.captureChildView(viewGroup.getChildAt(0),pointerId)
        }
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
        return 0
    }

    override fun getViewHorizontalDragRange(child: View): Int {
        println("getViewHorizontalDragRange=========${child.left}")
        return 0
    }

    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
        println("clampViewPositionHorizontal============left/dx====$left/$dx")
        return if(left+dx<0) 0 else left+dx
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
        println("clampViewPositionVertical==============top/dy=$top=$dy")

        return 0
    }
}