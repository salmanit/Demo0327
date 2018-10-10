package com.charliesong.demo0327.article

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class CallbackScaleFirst: ItemTouchHelper.SimpleCallback {
    constructor(dragDirs: Int, swipeDirs: Int) : super(dragDirs, swipeDirs)

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        println("onChildDraw========${viewHolder.adapterPosition}/${viewHolder.layoutPosition}")
        if(recyclerView.childCount>1){
            println("onChildDraw=========${recyclerView.getChildAt(0).height}===${recyclerView.getChildAt(1).top}")
        }
    }
}