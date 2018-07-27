package com.charliesong.demo0327.layoutmanager

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by charlie.song on 2018/5/10.
 */
class SwipCardLayoutManger:RecyclerView.LayoutManager(){
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    var  MaxShow=5;//最多展示几个item
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)
        if(itemCount==0){
            return
        }
        var end=MaxShow;
        if(itemCount<MaxShow){
            end=itemCount
        }
        for (i in 0 until end){
            val position=end-1-i;//我们的数据0，1，2，3，4 ，我们需要的是0在最上方，那么肯定先add那个index是4的child拉。
            var child=recycler.getViewForPosition(position)
            addView(child)
            measureChildWithMargins(child,0,0)
            val childWidth=getDecoratedMeasuredWidth(child)
            val childHeight=getDecoratedMeasuredHeight(child)
            layoutDecoratedWithMargins(child,(width-childWidth)/2,(height-childHeight)/2,(width+childWidth)/2,(height+childHeight)/2)
            child.translationY=17f*position
            child.scaleY=1f-position*0.05f
            child.scaleX=1f-position*0.05f

        }
    }
}