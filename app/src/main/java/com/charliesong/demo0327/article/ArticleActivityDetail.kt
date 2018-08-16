package com.charliesong.demo0327.article

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_article_detail.*

/**
 * Created by charlie.song on 2018/5/11.
*简单实现上滑切换下一个，不过效率不太好,另外切换页面的时候会看到闪动的白线，
 * 应该是下个item移动的时候有时间差，和上一个item有了间隔，所以可以动态的修改背景颜色为这两个item中的某一个一样，就应该看不出了
 *
 */
class ArticleActivityDetail : BaseActivity() {

    var index = 0
    var articles = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        index = intent.getIntExtra("index", 0)
        articles = intent.getStringArrayListExtra("lists")
        defaultSetTitle(articles.get(index))
        for(i in 0 until index){
            articles.add(articles.removeAt(0))
        }
        rv_detail.apply {
            layoutManager = UpLoadLayoutManager()
            adapter=object : BaseRvAdapter<String>(articles){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_article_detail
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    val s=getItemData(position)
                    holder.setText(R.id.tv_detail,s)
                    val originalPosition= s.substring("article ".length).toInt()
                    holder.getView<View>(R.id.layout_bg).setBackgroundColor(Color.parseColor(if(originalPosition%2==0) "#215980" else "#684592"))
                    holder.itemView.scrollY=0
                    println("onBindViewHolder====================$position====$s===size=${datas.size}")
                }

            }

        }
        ItemTouchHelper(UpFlingCallback(articles)).attachToRecyclerView(rv_detail)
    }



}