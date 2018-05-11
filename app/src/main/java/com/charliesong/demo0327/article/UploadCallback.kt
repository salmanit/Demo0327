package com.charliesong.demo0327.article

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.ScrollView
import android.widget.TextView
import com.charliesong.demo0327.R

/**
 * Created by charlie.song on 2018/5/11.
 */
class UploadCallback : ItemTouchHelper.Callback {
    var articles = arrayListOf<String>()

    constructor(articles: ArrayList<String>) : super() {
        this.articles = articles
    }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var scrollView:ScrollView= viewHolder.itemView as ScrollView
        println("24 line===============${scrollView.maxScrollAmount}=======${scrollView.scrollY}=====${scrollView.height}===position=${viewHolder.adapterPosition}")
        if(recyclerView.adapter.itemCount<2||scrollView.maxScrollAmount>scrollView.scrollY){
            return 0
        }
        return makeMovementFlags(0, ItemTouchHelper.UP)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            articles.removeAt(0)
            rv?.adapter?.notifyDataSetChanged()
        println("35==========notifyDataSetChanged==zize=${articles.size}")
    }

    var rv:RecyclerView?=null
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return 0.5f
    }
    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        rv=recyclerView
        var child=recyclerView.getChildAt(1)
        println("36=========${recyclerView.childCount}=======$dX/$dY=====${child.findViewById<TextView>(R.id.tv_detail).text}")
        child.translationY=dY
    }
}