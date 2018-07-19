package com.charliesong.demo0327.draghelper

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_drag_helper.view.*

class ConstraintLayoutDrag : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var draghelper: ViewDragHelper? = null
    private fun makesureHelper() {
        if (draghelper == null) {
            draghelper = ViewDragHelper.create(this, 1f, CallbackBottom2(this))
        }
       draghelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT or ViewDragHelper.EDGE_RIGHT)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        makesureHelper()
        return draghelper!!.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        makesureHelper()
        draghelper!!.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        draghelper?.apply {
            if (this.continueSettling(true)) {
                postInvalidate()
            }
        }
    }

    inner class CallbackBottom2 : ViewDragHelper.Callback {

        var viewGroup: ViewGroup
        var childTop: View

        constructor(viewGroup: ViewGroup) : super() {
            this.viewGroup = viewGroup
            childTop = viewGroup.getChildAt(0)
        }

        var point = Point()
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            //返回true表示这个child允许捕获，才会有后边的操作，返回false也就没有后边的操作了，这里可以根据child来决定哪个需要移动
            println("tryCaptureView======$child======$pointerId")
            if(child is RecyclerView){
                if(child.canScrollVertically(-10)){
                    return false
                }
            }
            return true
        }
        //tryCaptureView返回true就会走这里,或者在edge的时候draghelper?.captureChildView里传一个child也会走这里
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            point.x = capturedChild.left
            point.y = capturedChild.top
            println("onViewCaptured================$capturedChild======$activePointerId")
        }
        //这个就是你如果上边返回true，那么就成了dragging状态，
        // 手指离开屏幕onViewReleased如果啥也不操作就成了idle状态了。
        //如果这时候我们settleCapturedViewAt让它回到原始位置，肯定需要时间的，这个时候的状态就是setting了。
        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            println("onViewDragStateChanged=========$state") //STATE_IDLE STATE_DRAGGING STATE_SETTLING

        }

        //手指离开屏幕会走这里，当然前提是有captured的view
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            println("onViewReleased============$xvel==$yvel")
//            draghelper?.settleCapturedViewAt(point.x, point.y)
//            postInvalidate()

        }

        //需要draghelper设置支持的边界才能生效draghelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT or ViewDragHelper.EDGE_RIGHT)
        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
            super.onEdgeTouched(edgeFlags, pointerId)//1 4 2 8
            println("onEdgeTouched====================$edgeFlags========$pointerId")
            if(edgeFlags==ViewDragHelper.EDGE_RIGHT){
                //边界触摸的时候要操作那个child就把它传进去即可
                draghelper?.captureChildView(findViewById(R.id.tv_right),pointerId)
            }
        }

        override fun onEdgeLock(edgeFlags: Int): Boolean {
            println("onEdgeLock================$edgeFlags")
            return super.onEdgeLock(edgeFlags)
        }

        //也不知道啥用，onEdgeTouched以后，这时候触摸的地方如果没有capturedview的就会走这里
        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)
            println("onEdgeDragStarted===============$edgeFlags=======$pointerId")
        }

        override fun getOrderedChildIndex(index: Int): Int {
            println("getOrderedChildIndex===============$index")
            return super.getOrderedChildIndex(index)
        }

        //返回0的话就不能垂直移动
        override fun getViewVerticalDragRange(child: View): Int {
            println("getViewVerticalDragRange=========$child=======${child.top}")
            return 0
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            println("getViewHorizontalDragRange=========${child.left}")
            return 0
        }
        //capture view以后继续移动手指，就会走这里，返回0表示不移动，返回其他值表示view新的left位置
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            println("clampViewPositionHorizontal============left/dx====$left/$dx")
//        return super.clampViewPositionHorizontal(child, left, dx)
            return if (left + dx <= 0) 0 else left + dx
        }

        //capture view以后继续移动手指，就会走这里，返回0表示不移动，返回其他值表示view新的top位置
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            println("clampViewPositionVertical==============top/dy=$top=$dy")
            if(child is RecyclerView){

            }
            return if (top + dy < 0) 0 else top + dy
        }
        //执行了上边clamp的方法就会走这里
        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            println("onViewPositionChanged===============$left/$top/==dx/dy===$dx/$dy")
        }
    }


}