package com.charliesong.demo0327.linkage

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
    var edgeShow=20 //凸出来的那部分距离
    var smallScroll=0//rv1每移动一个item滚动的距离
    var whoScroll=0;//正在触摸哪个rv，1和2分别表示rv1和rv2
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
//    method2()
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
                if(whoScroll==1){//等于1表示当前手指触摸的是rv1，用这里的滚动来操作rv2
                   val rv2preScroll= screenWidth*rv1.computeHorizontalScrollOffset()/smallScroll//根据rv1滚动的距离来计算rv2应该滚动的距离，2者的比列就是
                    rv2.scrollBy(rv2preScroll-rv2.computeHorizontalScrollOffset(),0)
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

                if(whoScroll==2){//等于2表示当前触摸的是rv2，用这里的滚动来处理rv1的滚动
                    val rv1preScroll=smallScroll *rv2.computeHorizontalScrollOffset()/screenWidth
                    rv1.scrollBy(rv1preScroll-rv1.computeHorizontalScrollOffset(),0)
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

    private fun method2(){
        PagerSnapHelper().apply {  attachToRecyclerView(rv3)}
        rv3.apply {
            layoutManager = LinearLayoutManager(this@ActivityLinkageRecyeclerViews, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    var position = parent.getChildAdapterPosition(view)
                    var count = parent.adapter.itemCount
                    outRect.left = 10
                    outRect.right = 10
                    if (position == 0) {
                        outRect.left = 40
                    } else if (position == count - 1) {
                        outRect.right = 40
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
        vp.adapter=object :PagerAdapter(){
            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view==obj
            }

            override fun getCount(): Int {
               return testDatas.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                var view=LayoutInflater.from(container.context).inflate(R.layout.item_linkage_bottom,null)
                view.setBackgroundColor(testDatas[position])
                 container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                container.removeView(obj as View)
            }
        }
        rv3.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        vp.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

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