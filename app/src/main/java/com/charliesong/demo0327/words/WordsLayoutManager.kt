package com.charliesong.demo0327.words

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class WordsLayoutManager:RecyclerView.LayoutManager(){
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        if(state.itemCount==0){
            removeAndRecycleAllViews(recycler)
        }
        if (childCount == 0 && state.isPreLayout) {
            return
        }
        detachAndScrapAttachedViews(recycler)
        var left=paddingLeft
        var top=paddingTop
        repeat(state.itemCount){
            val child=recycler.getViewForPosition(it)
            addView(child)
            measureChildWithMargins(child,0,0)
            val childWidth=getDecoratedMeasuredWidth(child)
            val childHeight=getDecoratedMeasuredHeight(child)
            if(it==0){
                left=(width-childWidth)/2//第一个item居中显示
                top=paddingTop+childHeight*2//默认第一个item上下的间距都是item高度的2倍
            }else if(it==1){
                left=paddingLeft
                top=paddingTop+childHeight*5
            }else{
                if(left+childWidth>width-paddingRight){//开始添加之前得先判断下添加以后是否跑到屏幕外边了，如果超出了，那就换行展示
                    left=paddingLeft
                    top+=childHeight //我们这里文字是单行的，所以一行多个元素，高度是一样的，就不处理了。
                }
            }
            layoutDecoratedWithMargins(child,left,top,left+childWidth,top+childHeight)
            if(it>0){
                left+=childWidth //添加完child以后，计算新的left位置
            }
        }

    }

}