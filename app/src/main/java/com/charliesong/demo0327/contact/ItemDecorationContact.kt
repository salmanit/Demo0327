package com.charliesong.demo0327.contact

import android.graphics.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.ArrayList

/**
 * Created by charlie.song on 2018/4/3.
 * 此类只支持垂直方向的layoutmanager，不做验证，非此LayoutManager会挂掉。
 */
 abstract class ItemDecorationContact<T>:RecyclerView.ItemDecoration(){

    var datas= ArrayList<T>()
    abstract fun getDrawText(t:T):CharSequence//要画什么东西
    abstract fun indexEqual(pre:T,t:T):Boolean//返回两者的索引是否一样

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
            val i=parent.getChildAdapterPosition(view)
            outRect.top=if(needDraw(i)) indexHeight else 0
    }
    var indexHeight=60;//要画的分组索引的高度
    var paintFloatBg=Paint()//背景
    val paintFloatBgText=Paint()//文字
    var floatingTextLeft=20f;//要画的文字距离左边的间距
    var textHeight=0;//测量的索引字母的高度
    var floatRect=Rect()//用来画索引的布局方位
    open fun initSomeThing(){
        paintFloatBgText.textSize=30f
        val bounds=Rect()
        paintFloatBgText.getTextBounds("G",0,1,bounds)
        textHeight=bounds.height();
        paintFloatBgText.color=Color.RED
        paintFloatBg.color=Color.parseColor("#888888")
    }
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(datas.size==0){return}
        if(textHeight==0){
            initSomeThing()
        }
        for(i in 0 until parent.childCount){
            val child=parent.getChildAt(i);
            if(child!=null){
                val position=parent.getChildAdapterPosition(child)
                if(needDraw(position) ){
                    val rect=Rect(child.left, (child.top-indexHeight), child.right, child.top)
                    drawFloatingBg(c, rect,paintFloatBg)
                    drawFloatingBgText(c,getDrawText(datas.get(position)),child.left+floatingTextLeft,child.top-(indexHeight-textHeight)/2f,paintFloatBgText,rect)
                }
            }
        }
    }

    //画分组背景颜色
    open fun drawFloatingBg(c:Canvas,rect: Rect,paint: Paint){
        c.drawRect(rect,paint)
    }
    //话分组条上的文字,rect是分组背景的范围
    open fun drawFloatingBgText(c:Canvas,text:CharSequence,x:Float,y:Float,paint: Paint,rect: Rect){
        c.drawText(text,0,1,x,y,paintFloatBgText)
    }
    //第一条or和自己上一条的首字母不一样，那么肯定是个新的首字母，就画出来
    private fun needDraw(position:Int):Boolean{
        if(datas.size==0){
            return false
        }
        if(position==0){
            return true
        }
        return !indexEqual(datas.get(position-1),datas.get(position))
    }
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if(datas.size==0){return}
        floatRect.set(0,0,parent.width,indexHeight)
        val first=(parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if(first<0){
            return
        }
        var moveY=0;
        val next=findNextIndex(first,parent)
        if(next!=first){
            val holder=parent.findViewHolderForAdapterPosition(next)
            if(holder.itemView.top<indexHeight*2){
                moveY=holder.itemView.top-indexHeight*2;
            }
        }
        floatRect.offset(0,moveY)
        drawFloatingBg(c,floatRect,paintFloatBg)
        drawFloatingBgText(c,getDrawText(datas.get(first)),floatingTextLeft,floatRect.bottom-(indexHeight-textHeight)/2f,paintFloatBgText,floatRect)
    }

    //找到下一个带索引的item的position
    fun findNextIndex(position: Int,parent: RecyclerView):Int{
        for(i in position+1 until parent.adapter.itemCount){
            if(!indexEqual(datas.get(i-1),datas.get(i))){
                return i;
            }
        }
        return position
    }
}