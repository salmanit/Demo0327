package com.charliesong.demo0327.layoutmanager

import android.os.Bundle
import android.support.v7.widget.*
import com.charliesong.demo0327.*
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.activity_layout_manager_custom.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.SparseIntArray
import android.widget.ImageView
import com.charliesong.demo0327.base.*
import com.charliesong.demo0327.base.FlowLayoutManager


/**
 * Created by charlie.song on 2018/5/2.
 */
class ActivityLayoutManagerCustom: BaseActivity(){

    var datas= arrayListOf<String>()
    var urls= arrayListOf<String>()
    var pics= intArrayOf(R.mipmap.pic11,R.mipmap.sjzg)
    var itemDecoration:ItemDecorationSpace?=null;
     data class DP(var name:String,var url: String)
    var dps= arrayListOf<DP>()
    var i=1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager_custom)

        initData()

        btn_fragment.setOnClickListener {
            var fragment=if(i%2==0) FragmentSimpleText() else FragmentScrollConflict()
            supportFragmentManager.beginTransaction().replace(R.id.layout_root,fragment,fragment.javaClass.name).commitAllowingStateLoss()
            i++
            println("35======================${supportFragmentManager.fragments.size}")
        }
        rv_test.apply {
//            layoutManager=FlowGravityLayoutManager()
            itemDecoration=ItemDecorationSpace()
            addItemDecoration(itemDecoration)
            adapter=object : BaseRvAdapter<String>(datas){
                override fun getLayoutID(viewType: Int): Int {

                    return R.layout.item_simple_word
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_word,getItemData(position)+position)
                    val urlPosition=position%urls.size
                    holder.setImageUrl(R.id.iv_girl,urls[urlPosition])
//                    holder.getView<ImageView>(R.id.iv_girl).apply {
//                        var lp=layoutParams;
//                        lp.height=(370*0.5*heigths[urlPosition]).toInt()
//                        println("position=====$position/$urlPosition=====height=${lp.height}")
//                    }
//                    println("===============bind view hoder======$position")
                }
            }
            addOnItemTouchListener(object : RvItemTouchListener(rv_test){}.apply {
                listener=object : RvItemTouchListener.RvItemClickListener{
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
            adapter=object : BaseRvAdapter<DP>(dps){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_pic
                }
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    val data=getItemData(position)
                    holder.apply {
                        setText(R.id.tv_word,data.name)
                        setImageUrl(R.id.iv_bg,data.url)
                    }
                }
            }
        }
        ItemTouchHelper(SwipCardCallBack(0,ItemTouchHelper.UP or ItemTouchHelper.DOWN
         or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,dps)).attachToRecyclerView(rv_pic)



        btn_flowlayout.setOnClickListener {
            rv_test.layoutManager= FlowLayoutManager()
        }

        btn_linealayout.setOnClickListener {
            rv_test.layoutManager=LinearLayoutManager(this@ActivityLayoutManagerCustom)
        }
        btn_overlaylayout.setOnClickListener {
            CardConfig.initConfig(this@ActivityLayoutManagerCustom)
            val callback = RenRenCallback(rv_test, rv_test.adapter, datas)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(rv_test)
            rv_test.layoutManager= OverLayCardLayoutManager()
        }
        btn_flow_gravity.setOnClickListener {
            println("40dp================="+btn_flow_gravity.height +"======="+rv_test.height)
            rv_test.layoutManager= FlowGravityLayoutManager().apply { setHorizontalCenter(true) }
        }
        btn_move.setOnClickListener {
//            datas.removeAt(4)
//            rv_test.adapter.notifyItemRemoved(4)
            rv_test.smoothScrollToPosition(0)

        }

        btn_grid_layout.setOnClickListener {
            rv_test.layoutManager=GridLayoutManager(this@ActivityLayoutManagerCustom,3).apply {
                spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                        when(position){
                            0 or 1->{
                                return 3
                            }
                            else ->return 1;
                        }
                    }
                }
            }
        }
        btn_stagger_layout.setOnClickListener {
//            rv_test.removeItemDecoration(itemDecoration)

            rv_test.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL).apply{
            gapStrategy=2
            }
        }
//        LinearSnapHelper().attachToRecyclerView(rv_test)
//        PagerSnapHelper().attachToRecyclerView(rv_test)
    }

    var heigths= arrayListOf<Double>()
    private fun initData(){
        heigths.add(341.0/436)
        heigths.add(525.0/700f)
        heigths.add(525.0/700f)
        heigths.add(233.0/216f)
        heigths.add(455.0/568f)
        heigths.add(355.0/640f)
        heigths.add(202.0/249f)
        heigths.add(525.0/700f)
        heigths.add(1.0)
        heigths.add(1.0)
        heigths.add(1.0)
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-9c767c10de3c992e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/436")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-4d3b91acbec238f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-562c52905730ed41.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-71ae2b0041382790.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/216")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-94317998d3ffe0a0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/568")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-22a39533f950ad99.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/640")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-d3ddf36ef3b1d735.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/249")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-7100e7946373e28d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload.jianshu.io/users/upload_avatars/1777208/8d32947b00c5?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96")
        urls.add("https://cdn2.jianshu.io/assets/default_avatar/1-04bbeead395d74921af6a4e8214b4f61.jpg")
        urls.add("https://cdn2.jianshu.io/assets/default_avatar/13-394c31a9cb492fcb39c27422ca7d2815.jpg")
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


        (0 until datas.size).forEach {
            dps.add(DP(datas[it],urls[it%urls.size]))
        }
    }
}