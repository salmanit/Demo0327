package com.charliesong.demo0327.article

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.charliesong.demo0327.R

/**
 * Created by charlie.song on 2018/5/11.
 */
class UpLoadLayoutManager:RecyclerView.LayoutManager{
    constructor() : super(){
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        if(itemCount==0){
            detachAndScrapAttachedViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)

        var top=0
        repeat(Math.min(itemCount,3)){
            var child=recycler.getViewForPosition(it)
            child.translationY=0f
            addView(child)
            measureChildWithMargins(child,0,0)
            var height0=getDecoratedMeasuredHeight(child)
            layoutDecoratedWithMargins(child,0,top,width,top+height0)
            top+=height0
            println("51====${it}=========${child.findViewById<TextView>(R.id.tv_detail).text}===bottom=$height0====scroll=${(child).scrollY}")
        }
    }

}