package com.charliesong.demo0327.brief

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_brief_samsung.*

/**
 * Created by charlie.song on 2018/5/22.
 */
class ActivityBriefSamsung:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brief_samsung)
        initRv()

    }
    var minHeight=50//顶部悬浮窗的最小高度
    var maxHeigth=150//最大高度.和item里的顶部分类高度一样
    var originalHeigth=300//默认显示的底图的高度，也是recyclerview第一个item的itemDecoration的top的高度
    var datas= arrayListOf<BriefBean>()
    private fun initRv(){
        datas.add(BriefBean("商业",8,"place holder business",Color.BLACK))
        datas.add(BriefBean("新闻",2,"place holder news",Color.RED))
        datas.add(BriefBean("科技",3,"place holder technology",Color.GREEN))
        datas.add(BriefBean("体育",6,"place holder sports",Color.BLUE))
        datas.add(BriefBean("娱乐",8,"place holder recreation",Color.YELLOW))
        rv_brief.apply {
            layoutManager=LinearLayoutManager(this@ActivityBriefSamsung)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                    var position=parent.getChildAdapterPosition(view)
                    if(position==0){
                        outRect.top=originalHeigth
                    }
                }
            })
            adapter=object :BaseRvAdapter<BriefBean>(datas){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_brief_samsung
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    var bean=getItemData(position)
                    holder.setText(R.id.tv_float_title,bean.title)
                    holder.setText(R.id.tv_float_sub_title,"${bean.newArticle} new articles")
                    holder.setText(R.id.tv_placeholder,bean.testContent)
                    holder.getView<View>(R.id.layout_float_top).apply {
                        visibility=if(position==0) View.GONE else View.VISIBLE
                        setBackgroundColor(bean.color)
                    }
                }

            }
        }
        rv_brief.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                 var manager=recyclerView.layoutManager as LinearLayoutManager
                var first=manager.findFirstVisibleItemPosition();
                var holder=recyclerView.findViewHolderForAdapterPosition(first)
                var firstTop=holder.itemView.top
                var firstBottom=holder.itemView.bottom
                var layoutParams1=layout_float_top1.layoutParams
                var data=datas[first]


                if(first==0){
                    if(firstTop>=minHeight){
                        layoutParams1.height=minHeight
                        layout_float_top1.setBackgroundColor(Color.TRANSPARENT)
                        var alpha=1-(firstTop-minHeight)*1f/(originalHeigth-minHeight)
                        tv_float_title1.alpha=alpha
                        view_mask.alpha=alpha
                        view_mask.y=firstTop-originalHeigth*1f
                        iv_top_.scrollTo(0,-(firstTop-originalHeigth)/2)
                    }else{
                        view_mask.y=-originalHeigth*1f
                        layout_float_top1.setBackgroundColor(data.color)
                    }
                    tv_float_title1.setTextSize(TypedValue.COMPLEX_UNIT_SP,26f)
                    tv_float_sub_title1.setText("")
                }else{

                    var changeHeight=maxHeigth+firstTop
                    layoutParams1.height=Math.max(changeHeight,minHeight)
                    //字体大小从18sp到15sp变化，而悬浮窗高度从maxheight到minHeight变化
                    tv_float_title1.setTextSize(TypedValue.COMPLEX_UNIT_SP,15f+3f*(layoutParams1.height-minHeight)/(maxHeigth-minHeight))
                    layout_float_top1.setBackgroundColor(data.color)
                    tv_float_sub_title1.setText("${data.newArticle} new articles")
                    tv_float_sub_title1.alpha=Math.max(0f,1-(maxHeigth-layoutParams1.height)/40f)//滑动个40dp就让子标题不可见
                }
                tv_float_title1.setText(data.title)
                if(firstBottom<=minHeight){
                    layout_float_top1.y=firstBottom-minHeight*1f
                    layoutParams1.height=minHeight
                }else{
                    layout_float_top1.y=0f
                }
                layout_float_top1.layoutParams=layoutParams1
                println("position========$first=====top==  $firstTop bottom= $firstBottom==========alpha= ${tv_float_title1.alpha}")
            }
        })

    }

}