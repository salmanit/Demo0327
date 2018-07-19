package com.charliesong.demo0327.behavior

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.math.MathUtils
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import java.util.*
import android.widget.OverScroller
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.widget.Scroller


class ScaleRefreshBehavior : CoordinatorLayout.Behavior<View> {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private fun offsetChildAsNeeded(parent: CoordinatorLayout, child: View, dependency: View) {
        val behavior = (dependency.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is AppBarLayout.Behavior) {
            // Offset the child, pinning it to the bottom the header-dependency, maintaining
            // any vertical gap and overlap
            val ablBehavior = behavior as AppBarLayout.Behavior?
            ViewCompat.offsetTopAndBottom(child, dependency.height)
        }
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
//            offsetChildAsNeeded(parent,child,dependency)
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: View, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)

    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (TextUtils.equals("scale", dependency.getTag() as CharSequence?)) {
            println("layoutDependsOn==============$dependency")
            return true
        }
        if (dependency is AppBarLayout) {
            return true
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    private var mIsBeingDragged: Boolean = false
    private var mActivePointerId = -1
    private var mLastMotionY: Int = 0
    private var mTouchSlop = -1
    private var mVelocityTracker: VelocityTracker? = null
    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        println("onInterceptTouchEvent================${ev.action}")
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.context).scaledTouchSlop
        }
        if (ev.action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true
        }
        var scroll=OverScroller(child.context)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mIsBeingDragged = false
                var x = ev.getX().toInt()
                var y = ev.getY().toInt()
                if (parent.isPointInChildBounds(child, x, y)) {
                    mLastMotionY = y
                    mActivePointerId = ev.getPointerId(0)
                    ensureVelocityTracker()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerId = mActivePointerId
                if (activePointerId != -1) {
                    val index = ev.findPointerIndex(activePointerId)
                    if (index != -1) {
                        val y = ev.getY(index).toInt()
                        val yDiff = Math.abs(y - mLastMotionY)
                        if (yDiff > mTouchSlop) {
                            mIsBeingDragged = true
                            mLastMotionY = y
                        }
                    }

                }

            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsBeingDragged = false
                mActivePointerId = -1
                if (mVelocityTracker != null) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }
        }
        mVelocityTracker?.addMovement(ev)
        return mIsBeingDragged
    }

    private val INVALID_POINTER = -1
    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
//        println("onTouchEvent=================${ev.action}")
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).scaledTouchSlop
        }

        when (ev.getActionMasked()) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.getX().toInt()
                val y = ev.getY().toInt()

                if (parent.isPointInChildBounds(child, x, y) ) {
                    mLastMotionY = y
                    mActivePointerId = ev.getPointerId(0)
                    ensureVelocityTracker()
                } else {
                    return false
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = ev.findPointerIndex(mActivePointerId)
                if (activePointerIndex == -1) {
                    return false
                }

                val y = ev.getY(activePointerIndex).toInt()
                var dy = mLastMotionY - y

                if (!mIsBeingDragged && Math.abs(dy) > mTouchSlop) {
                    mIsBeingDragged = true
                    if (dy > 0) {
                        dy -= mTouchSlop
                    } else {
                        dy += mTouchSlop
                    }
                }

                if (mIsBeingDragged) {
                    mLastMotionY = y
                    // We're being dragged so scroll the ABL
                    scroll(parent, child,dy, 0, 0)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (mVelocityTracker != null) {
                    mVelocityTracker?.addMovement(ev)
                    mVelocityTracker?.computeCurrentVelocity(1000)
                    val yvel = mVelocityTracker?.getYVelocity(mActivePointerId)?:0f
                    fling(parent, child, -child.height, 0, yvel)
                }
                run {
                    mIsBeingDragged = false
                    mActivePointerId = INVALID_POINTER
                    if (mVelocityTracker != null) {
                        mVelocityTracker?.recycle()
                        mVelocityTracker = null
                    }
                }
            }
        // $FALLTHROUGH
            MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                if (mVelocityTracker != null) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }
        }

        if (mVelocityTracker != null) {
            mVelocityTracker?.addMovement(ev)
        }

        return true
    }
    private fun ensureVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }
    val factor=0.5f
    fun scroll(coordinatorLayout: CoordinatorLayout, child: View,
               dy: Int, minOffset: Int, maxOffset: Int): Int {
        var header=coordinatorLayout.getDependencies(child).get(0)
        if(child.top-dy<0){
            child.offsetTopAndBottom(-child.top)
            return 0
        }
        child.offsetTopAndBottom(-dy)
        header.pivotX=header.width/2f
        var scale=(child.top)/header.height.toFloat()
        if(scale>=1){
            header.scaleY=scale
            header.scaleX=scale
        }
        println("scroll y=========$dy===header height=${header.height}======child top=${child.top}===${header.scaleY}")
        return 0
    }

    private var mFlingRunnable: Runnable? = null
    var mScroller: OverScroller? = null
    fun fling(coordinatorLayout: CoordinatorLayout, layout: View, minOffset: Int,
              maxOffset: Int, velocityY: Float): Boolean{
        if (mFlingRunnable != null) {
            layout.removeCallbacks(mFlingRunnable)
            mFlingRunnable = null
        }

        if (mScroller == null) {
            mScroller = OverScroller(layout.context)
        }
        return false
        mScroller?.fling(
                0, 0, // curr
                0, Math.round(velocityY), // velocity.
                0, 0, // x
                minOffset, maxOffset) // y
//
//        if (mScroller!!.computeScrollOffset()) {
//            mFlingRunnable = FlingRunnable(coordinatorLayout, layout)
//            ViewCompat.postOnAnimation(layout, mFlingRunnable)
//            return true
//        } else {
//            onFlingFinished(coordinatorLayout, layout)
//            return false
//        }
    }

    override fun onRequestChildRectangleOnScreen(coordinatorLayout: CoordinatorLayout?, child: View?, rectangle: Rect?, immediate: Boolean): Boolean {
        println("onRequestChildRectangleOnScreen====================$rectangle====$immediate")
        return super.onRequestChildRectangleOnScreen(coordinatorLayout, child, rectangle, immediate)
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        var dependency = parent.getDependencies(child)?.get(0)
        println("onLayoutChild==================$child===${dependency?.height}====${dependency?.bottom}")
        layoutChild(parent, child, layoutDirection)
        return true
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        println("onMeasureChild=====================")
        val childLpHeight = child.layoutParams.height
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            val dependencies = parent.getDependencies(child)
            val header = parent.getDependencies(child)?.get(0)
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header) && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true)

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout()
                        return true
                    }
                }

                var availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.height
                }

                val height = availableHeight - header!!.getMeasuredHeight() + getScrollRange(header)
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT)
                            View.MeasureSpec.EXACTLY
                        else
                            View.MeasureSpec.AT_MOST)

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed)
                println("onMeasureChild=====================${child.height}")
                return true
            }
        }
        return false
    }


    protected fun layoutChild(parent: CoordinatorLayout, child: View,
                              layoutDirection: Int) {
        val dependencies = parent.getDependencies(child)
        val header = parent.getDependencies(child)?.get(0)

        if (header != null) {
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            val available = mTempRect1
            available.set(parent.paddingLeft + lp.leftMargin,
                    header!!.getBottom() + lp.topMargin,
                    parent.width - parent.paddingRight - lp.rightMargin,
                    parent.height + header!!.getBottom()
                            - parent.paddingBottom - lp.bottomMargin)

//            val parentInsets = parent.lastWindowInsets
//            if ((parentInsets != null && ViewCompat.getFitsSystemWindows(parent)
//                            && !ViewCompat.getFitsSystemWindows(child))) {
//                // If we're set to handle insets but this child isn't, then it has been measured as
//                // if there are no insets. We need to lay it out to match horizontally.
//                // Top and bottom and already handled in the logic above
//                available.left += parentInsets!!.systemWindowInsetLeft
//                available.right -= parentInsets!!.systemWindowInsetRight
//            }

            val out = mTempRect2
            GravityCompat.apply(resolveGravity(lp.gravity), child.measuredWidth,
                    child.measuredHeight, available, out, layoutDirection)

            val overlap = getOverlapPixelsForOffset(header)
            println("out====================${out.toShortString()}===$overlap")
            child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap - out.top)
            mVerticalLayoutGap = out.top - header!!.getBottom()
        } else {
            // If we don't have a dependency, let super handle it
            parent.onLayoutChild(child, layoutDirection)
            mVerticalLayoutGap = 0
        }
    }

    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
        println("onAttachedToLayoutParams============${params.height}")
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        println("onStartNestedScroll=================$type=========$target==========$axes")
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        println("onStopNestedScroll=================$type=====$target")
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        println("onNestedFling=================$velocityY====$consumed")
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float): Boolean {
        println("onNestedPreFling=================$velocityY")
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        println("onNestedPreScroll=================$dy===${Arrays.toString(consumed)}=============$type")
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        println("onNestedScroll=================$dyConsumed===$dyUnconsumed===$type")
    }

    override fun onNestedScrollAccepted(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type)
        println("onNestedScrollAccepted=================")
    }

    override fun blocksInteractionBelow(parent: CoordinatorLayout?, child: View?): Boolean {
        println("blocksInteractionBelow=================")
        return super.blocksInteractionBelow(parent, child)
    }


    fun getScrollRange(v: View): Int {
        return v.measuredHeight
    }

    val mTempRect1 = Rect()
    val mTempRect2 = Rect()
    private var mVerticalLayoutGap = 0
    private var mOverlayTop: Int = 0
    private fun resolveGravity(gravity: Int): Int {
        return if (gravity == Gravity.NO_GRAVITY) GravityCompat.START or Gravity.TOP else gravity
    }

    fun getOverlapPixelsForOffset(header: View): Int {
        return if (mOverlayTop == 0)
            0
        else
            MathUtils.clamp(
                    (getOverlapRatioForOffset(header) * mOverlayTop).toInt(), 0, mOverlayTop)
    }

    fun getOverlapRatioForOffset(header: View): Float {
        return 1f
    }

    private inner class FlingRunnable internal constructor(private val mParent: CoordinatorLayout, private val mLayout: View?) : Runnable {

        override fun run() {
            if (mLayout != null && mScroller != null) {
                if (mScroller!!.computeScrollOffset()) {
//                    setHeaderTopBottomOffset(mParent, mLayout, mScroller.getCurrY())
                    // Post ourselves so that we run on the next animation
                    ViewCompat.postOnAnimation(mLayout, this)
                } else {
//                    onFlingFinished(mParent, mLayout)
                }
            }
        }
    }
}
