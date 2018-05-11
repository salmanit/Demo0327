package com.charliesong.demo0327.layoutmanager

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by charlie.song on 2018/5/11.
 */
class CallBackTest:ItemTouchHelper.Callback(){
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
       return  0
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var rect= Rect()
        viewHolder.itemView.getLocalVisibleRect(rect)
        println("==============on swiped  direction=$direction  ${rect}")
    }


    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return super.getMoveThreshold(viewHolder)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return super.getSwipeThreshold(viewHolder)
    }

}