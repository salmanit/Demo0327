package com.charliesong.demo0327.layoutmanager

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import com.charliesong.demo0327.*
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.activity_layout_manager_custom.*
import android.support.v7.widget.helper.ItemTouchHelper
import com.charliesong.demo0327.RenRenCallback



/**
 * Created by charlie.song on 2018/5/2.
 */
class ActivityLayoutManagerCustom:BaseActivity(){

    var datas= arrayListOf<String>()
    var pics= intArrayOf(R.mipmap.pic11,R.mipmap.sjzg)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager_custom)

        initData()



        rv_test.apply {
//            layoutManager=FlowGravityLayoutManager()
            btn_overlaylayout.performClick()
            addItemDecoration(ItemDecorationSpace())
            adapter=object :BaseRvAdapter<String>(datas){
                override fun getLayoutID(viewType: Int): Int {
                    if(layoutManager is OverLayCardLayoutManager){
                        return R.layout.item_simple_pic
                    }
                    return R.layout.item_simple_word
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_word,getItemData(position)+position)
//                    println("===============bind view hoder======$position")
                }
            }
            addOnItemTouchListener(object :RvItemTouchListener(rv_test){}.apply {
                listener=object :RvItemTouchListener.RvItemClickListener{
                    override fun singleTab(position: Int, viewHolder: RecyclerView.ViewHolder?) {
                        showToast(datas[position])
                    }

                    override fun longPress(position: Int) {

                    }
                }
            })
        }

        rv_pic.apply {
            layoutManager=SwipCardLayoutManger()
            adapter=object :BaseRvAdapter<String>(datas){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_pic
                }
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_word,getItemData(position)+position)
                    holder.setImageRes(R.id.iv_bg,pics[position%pics.size])
                }
            }
        }
        ItemTouchHelper(SwipCardCallBack(0,ItemTouchHelper.UP or ItemTouchHelper.DOWN
         or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,datas)).attachToRecyclerView(rv_pic)

        btn_flowlayout.setOnClickListener {
            rv_test.layoutManager=com.charliesong.demo0327.FlowLayoutManager()
        }

        btn_linealayout.setOnClickListener {
            rv_test.layoutManager=LinearLayoutManager(this@ActivityLayoutManagerCustom)
        }
        btn_overlaylayout.setOnClickListener {
            CardConfig.initConfig(this@ActivityLayoutManagerCustom)
            val callback = RenRenCallback(rv_test, rv_test.adapter , datas)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(rv_test)
            rv_test.layoutManager=OverLayCardLayoutManager()
        }
        btn_flow_gravity.setOnClickListener {
            println("40dp================="+btn_flow_gravity.height +"======="+rv_test.height)
            rv_test.layoutManager=FlowGravityLayoutManager().apply { setHorizontalCenter(true) }
        }
        btn_move.setOnClickListener { rv_test.offsetChildrenHorizontal(10) }

//        LinearSnapHelper().attachToRecyclerView(rv_test)
//        PagerSnapHelper().attachToRecyclerView(rv_test)
    }


    private fun initData(){
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
    }
}