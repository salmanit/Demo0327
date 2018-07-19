package com.charliesong.demo0327.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class BehaviorScale:AppBarLayout.ScrollingViewBehavior{
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    var oldHeight=0;
    var oldY=-100f;
//    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
//        println("onInterceptTouchEvent==========================${ev.action}")
//        var dependencys=parent.getDependencies(child)
//        if(dependencys.size==0){
//            return super.onInterceptTouchEvent(parent, child, ev)
//        }
//        var dependency=dependencys[0]
//        if(oldHeight==0){
//            oldHeight=dependency.height
//        }
//        if(child.top<oldHeight){
//            return super.onInterceptTouchEvent(parent, child, ev)
//        }
//        when(ev.action and MotionEvent.ACTION_MASK){
//            MotionEvent.ACTION_MOVE->{
//                if(oldY<0){
//                    oldY=ev.getY()
//                }else{
//                    var k=oldY-ev.getY()
//                    oldY=ev.getY()
//                    if(k<-3){
//                        return true
//                    }
//                }
//            }
//            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
//                oldY=-100f
//            }
//        }
//        return super.onInterceptTouchEvent(parent, child, ev)
//    }
    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        var dependencys=parent.getDependencies(child)
        if(dependencys.size==0){
            return super.onTouchEvent(parent, child, ev)
        }
        var dependency=dependencys[0]
        println("top/bottom=======onTouchEvent===${ev.action}===========${child.top}===========${dependency.bottom}===${dependency.height}")
        if(child.top<oldHeight){
            return super.onTouchEvent(parent, child, ev)
        }

        when(ev.action and MotionEvent.ACTION_MASK){
            MotionEvent.ACTION_MOVE->{
                if(oldY<0){
                    oldY=ev.getY()
                }else{
                    var k=oldY-ev.getY()

                    if(k<-3){
                        dependency.translationY=-k
                        println("trans y========== $k====$oldY===${ev.getY()}")
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                oldY=-100f
            }
        }
        return super.onTouchEvent(parent, child, ev)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        println("onNestedPreScroll==============$target===$dy===${Arrays.toString(consumed)}======$type")
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        println("onNestedScroll==============$target===$dyConsumed===${dxUnconsumed}======$type")
    }

    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
        println("onNestedScroll==============$target===$axes========$type")
    }

}