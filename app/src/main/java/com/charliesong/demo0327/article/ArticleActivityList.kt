package com.charliesong.demo0327.article

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.charliesong.demo0327.*
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.base.RvItemTouchListener
import kotlinx.android.synthetic.main.activity_article_detail.*

/**
 * Created by charlie.song on 2018/5/11.
 */
class ArticleActivityList: BaseActivity(){

    var articles= arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        defaultSetTitle("article")
        setTitle("article")
        for(i in 0 until 50){
            articles.add("article $i")
        }
        rv_detail.apply {
            layoutManager=LinearLayoutManager(this@ArticleActivityList)

            adapter=object : BaseRvAdapter<String>(articles){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_article_title
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_title,getItemData(position))
                    holder.setText(R.id.tv_description,"${getItemData(position)}  description.....")
                }

            }
            itemAnimator=DefaultItemAnimator()
            addOnItemTouchListener(RvItemTouchListener(rv_detail).apply {
                listener=object : RvItemTouchListener.RvItemClickListener{
                    override fun singleTab(position: Int, viewHolder: RecyclerView.ViewHolder) {
                        startActivity(Intent(this@ArticleActivityList,ArticleActivityDetail::class.java)
                                .putExtra("index",position)
                                .putExtra("lists",articles))
                    }

                    override fun longPress(position: Int) {

                    }

                }
            })
        }
    }

    private fun setToolbarTitle(title:String){
        supportActionBar?.run {
            setTitle(title)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}