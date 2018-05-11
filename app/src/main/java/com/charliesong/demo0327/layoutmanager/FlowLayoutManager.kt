package com.charliesong.demo0327.layoutmanager

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by charlie.song on 2018/5/2.
 */
class FlowLayoutManager:RecyclerView.LayoutManager(){
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
         return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private var mVerticalOffset: Int = 0//竖直偏移量 每次换行时，要根据这个offset判断
    private var mFirstVisiPos: Int = 0//屏幕可见的第一个View的Position
    private var mLastVisiPos: Int = 0//屏幕可见的最后一个View的Position
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler);
        //初始化
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();

        //初始化时调用 填充childView
        fill(recycler, state);

    }
    private fun fill(recycler: RecyclerView.Recycler, state: RecyclerView.State){
        var topOffset = paddingTop//布局时的上偏移
        var leftOffset = paddingLeft//布局时的左偏移
        var lineMaxHeight = 0//每一行最大的高度
        var minPos = mFirstVisiPos//初始化时，我们不清楚究竟要layout多少个子View，所以就假设从0~itemcount-1
        mLastVisiPos = itemCount - 1
        for(i in minPos ..mLastVisiPos){
            var child=recycler.getViewForPosition(i)

            addView(child)
            measureChildWithMargins(child,0,0)
            var childHorizontalSpace=getDecoratedMeasurementHorizontal(child)
            var childVerticalSpace=getDecoratedMeasurementVertical(child)
            println("$i=========$childHorizontalSpace / $childVerticalSpace / ${child.height}/${child.measuredHeight}  total space==${getHorizontalSpace()}  lastVisible=$mLastVisiPos")
            if (leftOffset +  childHorizontalSpace<= getHorizontalSpace()){

                layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + childHorizontalSpace, topOffset +childVerticalSpace );
                //改变 left  lineHeight
                leftOffset += childHorizontalSpace;
                lineMaxHeight = Math.max(lineMaxHeight, childVerticalSpace);
                println("if=============$i")
            }else{
                //改变top  left  lineHeight
                leftOffset = getPaddingLeft();
                topOffset += lineMaxHeight;
                lineMaxHeight = 0;
                //新起一行的时候要判断一下边界
                if (topOffset  > height - paddingBottom) {
                    //越界了 就回收
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos = i - 1;
                    println("else if========$topOffset /${height}")
                } else {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child));
                    println("else else=============$i")
                    //改变 left  lineHeight
                    leftOffset += getDecoratedMeasurementHorizontal(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
                }
            }
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.getLayoutParams() as RecyclerView.LayoutParams
        return (getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin)
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.getLayoutParams() as RecyclerView.LayoutParams
        return (getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin)
    }

    fun getVerticalSpace(): Int {
        return height - paddingTop - paddingBottom
    }

    fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }
}