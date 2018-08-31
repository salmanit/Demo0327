package com.charliesong.demo0327.linkage

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_linkage_recyclerviews.*

/**
 * Created by charlie.song on 2018/5/21.
 */
class ActivityLinkageRecyeclerViews : BaseActivity() {
    var testDatas = arrayListOf<Int>()
    var smallWidth = 1//上边那个的item宽
    var screenWidth=1;//屏幕宽，也就是rv2的item宽
    var edgeShow=30 //凸出来的那部分距离
    var smallScroll=0//rv1每移动一个item滚动的距离
    var whoScroll=0;//正在触摸哪个rv，1和2分别表示rv1和rv2
    var offset1=0;//rv1的offset
    var offset2=0;//rv2的offset
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linkage_recyclerviews)
        testDatas.add(Color.RED)
        testDatas.add(Color.BLUE)
        testDatas.add(Color.GREEN)
        testDatas.add(Color.YELLOW)
        testDatas.add(Color.LTGRAY)
        screenWidth=windowManager.defaultDisplay.width

        smallWidth = screenWidth - edgeShow*4
        smallScroll=screenWidth-edgeShow*3

    method1()
    }

    private fun method1() {
        rv1.apply {
            layoutManager = LinearLayoutManager(this@ActivityLinkageRecyeclerViews, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {

                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    var position = parent.getChildAdapterPosition(view)
                    var count = parent.adapter.itemCount
                    outRect.left = edgeShow/2
                    outRect.right = edgeShow/2
                    if (position == 0) {
                        outRect.left = edgeShow*2
                    } else if (position == count - 1) {
                        outRect.right = edgeShow*2
                    }
                }
            })

            adapter = object : BaseRvAdapter<Int>(testDatas) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_linkage_top
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_num, "$position")
                    var params = holder.itemView.layoutParams
                    params.width = smallWidth
                    holder.itemView.setBackgroundColor(testDatas[position])
                }
            }
        }

        rv2.apply {
            layoutManager =LinearLayoutManager(this@ActivityLinkageRecyeclerViews, LinearLayoutManager.HORIZONTAL, false)
            adapter = object : BaseRvAdapter<Int>(testDatas) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_linkage_bottom
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_bottom, "$position")
                    holder.itemView.setBackgroundColor(testDatas[ position])
                }
            }
        }
        //2个helper 让rv的item自动居中显示
        PagerSnapHelper().apply {  attachToRecyclerView(rv1)}
        PagerSnapHelper().apply {  attachToRecyclerView(rv2)}


        rv1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                offset1+=dx;
                if(whoScroll==1){//等于1表示当前手指触摸的是rv1，用这里的滚动来操作rv2
                   val rv2preScroll= screenWidth*offset1/(smallScroll)//根据rv1滚动的距离来计算rv2应该滚动的距离，2者的比列就是
                    rv2.scrollBy(rv2preScroll-offset2,0)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    whoScroll=0
                }else if(newState==RecyclerView.SCROLL_STATE_DRAGGING||newState==RecyclerView.SCROLL_STATE_SETTLING){
                    whoScroll=1
                }
            }
        })
        rv2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                offset2+=dx;
                if(whoScroll==2){//等于2表示当前触摸的是rv2，用这里的滚动来处理rv1的滚动
                    val rv1preScroll=(smallScroll) *offset2/screenWidth
                    rv1.scrollBy(rv1preScroll-offset1,0)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    whoScroll=0
                }else if(newState==RecyclerView.SCROLL_STATE_DRAGGING||newState==RecyclerView.SCROLL_STATE_SETTLING){
                    whoScroll=2
                }
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if((ev.action and MotionEvent.ACTION_MASK)==MotionEvent.ACTION_POINTER_DOWN){
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}