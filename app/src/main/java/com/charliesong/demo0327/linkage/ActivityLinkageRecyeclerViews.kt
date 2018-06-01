package com.charliesong.demo0327.linkage

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
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
    var fromTop=-1;
    var testDatas = arrayListOf<Int>()
    var screenWidth = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linkage_recyclerviews)
        testDatas.add(Color.RED)
        testDatas.add(Color.BLUE)
        testDatas.add(Color.GREEN)
//        testDatas.add(Color.YELLOW)
//        testDatas.add(Color.LTGRAY)
        screenWidth = windowManager.defaultDisplay.width - 80
        rv1.apply {
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
                    params.width = screenWidth
                    params.height = 120
                    holder.itemView.setBackgroundColor(testDatas[position])
                }
            }
        }

        rv2.apply {
            layoutManager =object :LinearLayoutManager(this@ActivityLinkageRecyeclerViews, LinearLayoutManager.HORIZONTAL, false){
//                override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
//                    return 10
//                }
            }
                    .apply {

            }
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

      var help1=  PagerSnapHelper().apply {  attachToRecyclerView(rv1)}
       var help2 =PagerSnapHelper().apply {  attachToRecyclerView(rv2)}

        rv1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(fromTop==-1||fromTop==1){
                    fromTop=1
                    rv2.scrollBy(dx,dy)
                }
                if(dx==0){
                    fromTop=-1
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                   var child= help1.findSnapView(rv1.layoutManager)
                    var position=rv1.getChildAdapterPosition(child)
                    fromTop=-1
                    (rv2.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,0)
                }
            }
        })
        rv2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
               if(fromTop==-1||fromTop==2){
                   fromTop=2
                   rv1.scrollBy(dx,dy)

               }
                if(dx==0){
                    fromTop=-1
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                   var child= help2.findSnapView(rv1.layoutManager)
                    var position=rv2.getChildAdapterPosition(child)
                    fromTop=-1
                    (rv1.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,30)
                }
            }
        })


//        rv2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                rv1.scrollBy(dx,dy)
//            }
//        })





//        PagerSnapHelper().attachToRecyclerView(rv3)

        rv3.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    var position = parent.getChildAdapterPosition(view)
                    var count = parent.adapter.itemCount
//                    outRect.left = 10
//                    outRect.right = 10
//                    if (position == 0) {
//                        outRect.left = 40
//                    } else if (position == count - 1) {
//                        outRect.right = 40
//                    }
                }
            })

            adapter = object : BaseRvAdapter<Int>() {
                override fun getLayoutID(viewType: Int): Int {
                    when (viewType) {
                        0 -> {
                            return R.layout.item_linkage_top
                        }
                        else -> {
                            return R.layout.item_linkage_bottom
                        }
                    }
                }

                override fun getItemViewType(position: Int): Int {

                    when (position % 2) {
                        0 -> {
                            return 0
                        }
                        else -> {
                            return 1
                        }
                    }
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    when(getItemViewType(position)){
                        0->{
                            holder.setText(R.id.tv_num, "$position")
                            var params = holder.itemView.layoutParams
                            params.width = screenWidth
                            params.height = 100
                            holder.itemView.setBackgroundColor(testDatas[position])
                        }
                        1->{
                            holder.setText(R.id.tv_bottom, "$position")
                            holder.itemView.setBackgroundColor(testDatas[ position])
                            var params = holder.itemView.layoutParams
                            params.width = screenWidth
                            params.height = 300
                        }
                    }

                }
            }
        }

    }


}