package com.charliesong.demo0327.words

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.BaseRvAdapter
import com.charliesong.demo0327.BaseRvHolder
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_words.*
import java.util.*

/**
 * Created by charlie.song on 2018/4/28.
 */
class ActivityWords:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)


        var wordsOriginal= arrayListOf<WordBean>()
        wordsOriginal.add(WordBean("He"))
        wordsOriginal.add(WordBean("must"))
        wordsOriginal.add(WordBean("been"))
        wordsOriginal.add(WordBean("single"))
        rv_words.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(ItemDecorationSpace())
            adapter=object :BaseRvAdapter<WordBean>(wordsOriginal){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_word
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    var bean=getItemData(position)
                    holder.setText(R.id.tv_word,getItemData(position).word)
                    if(bean.inserted){
                        holder.getView<TextView>(R.id.tv_word).apply {
                            visibility=View.INVISIBLE
                        }
                    }
                }
            }
        }

    var helper=ItemTouchHelper(object :ItemTouchHelper.Callback(){
        //这里用来判断处理哪些情况
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            var position=viewHolder.adapterPosition
            if(position==0){
                return 0   //这里模拟，第一个位置不允许拖动
            }
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END  //这个是拖动的flag
            val swipeFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN //这个是滑动的flag，比如滑动删除，这里说明下，如果是HORIZONTAL方向的layoutManager,那滑动删除只有上下，垂直方向的话是左右。
            return makeMovementFlags(dragFlags,swipeFlags)
        }
        //拖拽的时候会不停的回掉这个方法，我们在这里做的就是交换对应的数据
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
           var oldPositioon= viewHolder.adapterPosition
            var newPosition=target.adapterPosition
            if(newPosition==0){
                return false
            }
            Collections.swap(wordsOriginal,oldPositioon,newPosition)
            recyclerView.adapter.notifyItemMoved(oldPositioon,newPosition)
            return true
        }
        //使用kotlin的时候得注意，这个viewHolder是可能为空的，当state为Idle的时候
        //滑动或者拖拽的时候都会走这里的
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            viewHolder?.run {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    itemView.setBackgroundColor(Color.RED);
                    var vibrator=getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(55)
                }
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.setBackgroundColor(0);
        }
        //这个就是滑动删除的时候用的，方向就是getMovementFlags（）里返回的swipeFlags，看它支持哪个方向滑动，这里返回其中一个滑动方向
        //当划出屏幕以后会回掉这里，我们要做的就是把数据删除掉。
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var rect=Rect()
            viewHolder.itemView.getLocalVisibleRect(rect)
            println("==============on swiped  direction=$direction  ${rect}")
        }

    })
        helper.attachToRecyclerView(rv_words)



//        tv_word_insert.setOnTouchListener { v, event ->
//            when(event.action){
//                MotionEvent.ACTION_DOWN ->{
//                   downX=event.rawX
//                    downY=event.rawY
//                    v.getLocationOnScreen(oldLocation)
//                    rv_words.getLocationOnScreen(currentLocation)
//                    println("down x/y==$downX/$downY --${event.x}/${event.y}--- v location=${Arrays.toString(oldLocation)}  rv location==${Arrays.toString(currentLocation)}")
//
//                    var decorView=window.decorView as ViewGroup
//                     copyView=ImageView(v.context).apply {
//                        layoutParams= ViewGroup.LayoutParams(v.width,v.height)
//                        v.isDrawingCacheEnabled=true
//                        setImageBitmap(Bitmap.createBitmap(v.drawingCache))
//                        v.isDrawingCacheEnabled=false
//                        decorView.addView(this)
//                         left=oldLocation[0]
//                         top=oldLocation[1]
//                    }
//                    v.visibility=View.INVISIBLE
//
//
//                }
//                MotionEvent.ACTION_MOVE->{
//                    copyView?.translationX=event.rawX-downX
//                    copyView?.translationY=event.rawY-downY
//                }
//                MotionEvent.ACTION_UP , MotionEvent.ACTION_CANCEL
//                ->{
//                    var decorView=window.decorView as ViewGroup
//                    decorView.removeView(copyView)
//                    v.visibility=View.VISIBLE
//                }
//
//            }
//            return@setOnTouchListener true
//        }

    }
    var downX=0f
    var downY=0f
    var moveX=0f
    var moveY=0f
    var oldLocation= IntArray(2)
    var currentLocation= IntArray(2)
    var copyView:ImageView?=null
}