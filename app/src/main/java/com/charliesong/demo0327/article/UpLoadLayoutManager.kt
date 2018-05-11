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
        isAutoMeasureEnabled=true
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

        var child=recycler.getViewForPosition(0)
            addView(child)
            measureChildWithMargins(child,0,0)
                var height0=getDecoratedMeasuredHeight(child)
            layoutDecoratedWithMargins(child,0,0,width,height0)
        var scrollView: ScrollView = child as ScrollView
        println("29=============${child.findViewById<TextView>(R.id.tv_detail).text}===bottom=$height0====scroll=${scrollView.scrollY}")
        if(itemCount>1){
            var child=recycler.getViewForPosition(1)
            addView(child)
            measureChildWithMargins(child,0,0)
            layoutDecoratedWithMargins(child,0,height0,width,height0+getDecoratedMeasuredHeight(child))
            println("38=============${child.findViewById<TextView>(R.id.tv_detail).text}====height=${getDecoratedMeasuredHeight(child)}====scroll=${scrollView.scrollY}")
        }

    }

}