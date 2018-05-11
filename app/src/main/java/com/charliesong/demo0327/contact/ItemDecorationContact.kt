package com.charliesong.demo0327.contact

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import java.util.ArrayList

/**
 * Created by charlie.song on 2018/4/3.
 */
class ItemDecorationContact:RecyclerView.ItemDecoration(){

    var datas= ArrayList<Contact>()
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
            var i=parent.getChildAdapterPosition(view)
            outRect.top=if(needDraw(i)) indexHeight else 0

    }
    var colorGroup=Color.parseColor("#888888")
    var colorGroupText=Color.RED
    var indexHeight=40;//索引布局的高度
    var paint=Paint()
    var textHeight=0;//测量的索引字母的高度
    var floatRect=Rect()//用来画索引的布局方位
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(textHeight==0){
            paint.textSize=30f
            var bounds=Rect()
            paint.getTextBounds("G",0,1,bounds)
            textHeight=bounds.height();

        }
        var count=parent.childCount;
        for(i in 0 until count){
            var child=parent.getChildAt(i);
            if(child!=null){
                var position=parent.getChildAdapterPosition(child)
                if(needDraw(position) ){
                    paint.color=colorGroup
                    c.drawRect(child.left.toFloat(), (child.top-indexHeight).toFloat(), child.right.toFloat(), child.top.toFloat(),paint)
                    var contact=datas.get(position)
                    paint.color=colorGroupText
                    c.drawText(contact.index,0,1,child.left+10f,child.top-(indexHeight-textHeight)/2f,paint)
                }
            }
        }
    }
    //第一条，或者是和自己上一条的首字母不一样，那么肯定是个新的首字母，就画出来
    private fun needDraw(position:Int):Boolean{
        if(position==0){
            return true
        }
        var contactPre=datas.get(position-1)
        var contact=datas.get(position)
        return !TextUtils.equals(contact.index.substring(0,1),contactPre.index.substring(0,1))
    }
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        floatRect.set(0,0,parent.width,indexHeight)
        var layoutmanager=(parent.layoutManager as LinearLayoutManager)
        var first=layoutmanager.findFirstVisibleItemPosition()
        if(first<0){
            return
        }
        var contact=datas.get(first)
        var moveY=0;
        var next=findNextIndex(first,parent)
        if(next!=first){
            var holder=parent.findViewHolderForAdapterPosition(next)
            if(holder.itemView.top<indexHeight*2){
                moveY=holder.itemView.top-indexHeight*2;
            }
        }
        floatRect.offset(0,moveY)
        paint.color=colorGroup
        c.drawRect(floatRect,paint)
        paint.color=Color.RED
        c.drawText(contact.index,0,1,10f,floatRect.bottom-(indexHeight-textHeight)/2f,paint)
    }

    fun findNextIndex(position: Int,parent: RecyclerView):Int{
        for(i in position+1 until parent.adapter.itemCount){
            if(!TextUtils.equals(datas.get(i-1).index.substring(0,1),datas.get(i).index.substring(0,1))){
                return i;
            }
        }
        return position
    }
}