package com.charliesong.demo0327.article

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.BaseRvAdapter
import com.charliesong.demo0327.BaseRvHolder
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_article_detail.*

/**
 * Created by charlie.song on 2018/5/11.
 * //没有完工，layoutmanager有问题，滑动过后数据不对头。。。
 */
class ArticleActivityDetail : BaseActivity() {

    var index = 0
    var articles = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        index = intent.getIntExtra("index", 0)
        articles = intent.getStringArrayListExtra("lists")
        setTitle("${articles.get(index)}")

        for(i in 0 until index){
            articles.add(articles.removeAt(0))
        }
        rv_detail.apply {
            layoutManager = UpLoadLayoutManager()
            adapter=object :BaseRvAdapter<String>(articles){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_article_detail
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    var s=getItemData(position)
                    holder.setText(R.id.tv_detail,s)
                    val originalPosition= s.substring("article ".length).toInt()
                    holder.getView<View>(R.id.layout_bg).setBackgroundColor(Color.parseColor(if(originalPosition%2==0) "#215980" else "#684592"))
                    (holder.itemView as ScrollView).scrollY=0
                    println("onBindViewHolder====================$position====$s===size=${datas.size}")
                }

            }

        }
        ItemTouchHelper(UploadCallback(articles)).attachToRecyclerView(rv_detail)
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.run {
            setTitle(title)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}