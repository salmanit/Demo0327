package com.charliesong.demo0327.layoutmanager

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by charlie.song on 2018/5/10.
 */
class SwipCardCallBack:ItemTouchHelper.SimpleCallback{
    var datas= arrayListOf<String>()
    constructor(dragDirs: Int, swipeDirs: Int,data:ArrayList<String>) : super(dragDirs, swipeDirs){
        datas=data
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }
    var rv:RecyclerView?=null;
    //拖动结束以后会走这里
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        datas.add(datas.removeAt(0))//这里数据不删除，就是把滑动的数据再放到最后。有具体需求再改
        rv?.adapter?.notifyDataSetChanged()
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        rv=recyclerView;
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val z=Math.sqrt((dX*dX+dY*dY).toDouble()).toFloat();
        var factor=z/(recyclerView.width/2f);//移动的距离和宽度的一半 做比较
        if(factor>1){
            factor= 1f
        }
        for(i in 0 until  recyclerView.childCount-1){
            val child=recyclerView.getChildAt(i)
            val realPosition=recyclerView.getChildAdapterPosition(child)
            child.translationY=17f*(realPosition-factor)
            child.scaleY=1f-(realPosition-factor)*0.05f
            child.scaleX=1f-(realPosition-factor)*0.05f
        }
    }
}