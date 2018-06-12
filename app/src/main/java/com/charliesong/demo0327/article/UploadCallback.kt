package com.charliesong.demo0327.article

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter

/**
 * Created by charlie.song on 2018/5/11.
 */
class UploadCallback : ItemTouchHelper.Callback {
    var articles = arrayListOf<String>()

    constructor(articles: ArrayList<String>) : super() {
        this.articles = articles
    }


    //这个方法只在手指触摸下的时候执行一次，再次执行需要手指离开后再点，所以效果不太好，滑动到底部无法继续，得离开屏幕再次点击才能继续。
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var down=viewHolder.itemView.canScrollVertically(1)
        println("24 line====$down==${viewHolder.itemView.scrollY}==============${viewHolder.itemView.height}===position=${viewHolder.adapterPosition}")
        if(recyclerView.adapter.itemCount<2||down){
            return 0
        }
        return makeMovementFlags(0, ItemTouchHelper.UP)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            articles.removeAt(0)
            rv?.adapter?.apply {
                (this as BaseRvAdapter<String>).datas.removeAt(0)
                notifyDataSetChanged()
                notifyItemRemoved(1)
            }

        (rv?.context as BaseActivity).defaultSetTitle(articles.get(0))

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