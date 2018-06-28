package com.charliesong.demo0327.room

import android.arch.lifecycle.Observer
import android.arch.paging.PagedListAdapter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.activity_room_learn.*
import kotlin.concurrent.thread

class ActivityRoomLearn:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_learn)
        defaultSetTitle("room component learn")
        initRv()

        initBtn()
    }

    private fun initBtn() {
        btn_add.setOnClickListener {

//            thread {
//                var user=User(0,et_first_name.text.toString(),et_last_name.text.toString(),et_age.text.toString().toInt())
//                UtilRoomDB.getInstanceDB().userDao().insertAll(user)
//            }
        }

        btn_check_all.setOnClickListener {
//            thread {
//                var users=UtilRoomDB.getInstanceDB().userDao().getAllUser()
//                (rv_show.adapter as BaseRvAdapter<User>).initData(users)
//            }
            var lists= arrayListOf<User>()
            (0..10000).forEach {
                lists.add(User(firstName = "f$it",lastName = "a$it"))
            }
            (rv_show.adapter as BaseRvAdapter<User>).setData(lists)
        }
    }
//    fun getMyAdapter():PagedListAdapter<User,BaseRvHolder>{
//        return  rv_show.adapter as PagedListAdapter<User,BaseRvHolder>
//    }

    var firstVisible=0
    var firstOffset:Int=0
    var start=false
    fun move(rv:RecyclerView){
        var layoutManager=rv.layoutManager as LinearLayoutManager
        firstVisible=layoutManager.findFirstVisibleItemPosition()
        firstOffset=layoutManager.findViewByPosition(firstVisible).left
        println("get===========index=${firstVisible}===${firstOffset}=")
        (0 until rv_show.childCount).forEach {
            rv_show.getChildAt(it)?.findViewById<RecyclerView>(R.id.rv_data)?.apply {
                var layoutManager=this.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(firstVisible,firstOffset)
            }
        }
    }
    private fun initRv(){
        rv_show.apply {
            layoutManager=LinearLayoutManager(this@ActivityRoomLearn).apply {
                recycleChildrenOnDetach=true
            }
               addItemDecoration(object :RecyclerView.ItemDecoration(){
                   override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                       outRect.bottom=1
                   }
                   val  paint=Paint()
                   override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                       super.onDraw(c, parent, state)
                       (0 until parent.childCount).forEach {
                           parent.getChildAt(it)?.apply {
                               c.drawRect(0f,this.bottom.toFloat(),width.toFloat(),this.bottom+1f,paint)
                           }
                       }
                   }
               })
            setHasFixedSize(true)
            adapter=object :BaseRvAdapter<User>(){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_room_learn
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    getItemData(position)?.apply {
                                                holder.setText(R.id.tv_name,"$uid")

                    }
                    holder.itemView.setOnClickListener {
                        showToast("click ${position}")
                    }
                    holder.getView<RecyclerView>(R.id.rv_data).apply {
                        this.clearOnScrollListeners()

                        this.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)
                                if(start){
                                    move(recyclerView)
                                }
                            }

                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if(newState==RecyclerView.SCROLL_STATE_DRAGGING||newState==RecyclerView.SCROLL_STATE_SETTLING){
                                    start=true
                                }
                                if(start&&newState==RecyclerView.SCROLL_STATE_IDLE){
                                    start=false
                                    move(recyclerView)
                                }
                            }
                        })
                        setHasFixedSize(true)
                        if(adapter==null){
                            layoutManager=LinearLayoutManager(this@ActivityRoomLearn,LinearLayoutManager.HORIZONTAL,false)
                            adapter=object :BaseRvAdapter<String>(){
                                override fun getLayoutID(viewType: Int): Int {
                                    return R.layout.item_just_text
                                }

                                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                                            holder.setText(R.id.tv_text,"$position")
                                }

                                override fun getItemCount(): Int {
                                    return 30
                                }

                            }
                            this.addOnItemTouchListener(ItemTouchListener(this))
                        }else{
                            (adapter.notifyDataSetChanged())
                        }
                        var layoutManager=this.layoutManager as LinearLayoutManager
                        layoutManager.scrollToPositionWithOffset(firstVisible,firstOffset)
                    }
                }
            }
        }
    }


//    var diffCallback=object :DiffUtil.ItemCallback<User>(){
//        override fun areItemsTheSame(oldItem: User?, newItem: User?): Boolean {
//            return  oldItem?.uid==newItem?.uid
//        }
//
//        override fun areContentsTheSame(oldItem: User?, newItem: User?): Boolean {
//            return TextUtils.equals(oldItem?.firstName,newItem?.firstName) and TextUtils.equals(oldItem?.lastName,newItem?.lastName)
//        }
//    }


    inner class ItemTouchListener:RecyclerView.SimpleOnItemTouchListener{
         var gestureDetector: GestureDetector
          var rrv: RecyclerView

        constructor(rv: RecyclerView) : super() {
            this.rrv = rv
            gestureDetector=GestureDetector(rv.getContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    println("onSingleTapUp=================")
                    (rrv.parent as ViewGroup).apply {
                        performClick()
                        isPressed=true
                    }
                    return false
                }

                override fun onLongPress(e: MotionEvent) {
                    super.onLongPress(e)
                    (rrv.parent as ViewGroup).performClick()
                    println("onLongPress===================")
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
            rrv.scrollTo(1,1)
            return gestureDetector.onTouchEvent(e)
        }

        override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
            super.onTouchEvent(rv, e)
            gestureDetector.onTouchEvent(e)
        }

    }
}