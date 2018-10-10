package com.charliesong.demo0327.article

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.charliesong.demo0327.*
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.base.RvItemTouchListener
import com.charliesong.demo0327.custom.HFAdapter
import com.charliesong.demo0327.layoutmanager.net.VegaLayoutManager
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
        for(i in 0 until 50){
            articles.add("article $i")
        }
        val btn=Button(this)
        btn.text="add"
        btn.layoutParams=FrameLayout.LayoutParams(100,100).apply {
            gravity=Gravity.RIGHT or Gravity.BOTTOM
        }
        btn.setOnClickListener {
            articles.add(0,"article "+articles.size)
            (rv_detail.adapter as HFAdapter).apply {
                notifyItemInserted(this.headsize())
            }
        }
        (window.decorView as FrameLayout).addView(btn)
        rv_detail.apply {
//            layoutManager=LinearLayoutManager(this@ArticleActivityList)
//            layoutManager=VegaLayoutManager()
            layoutManager=LayoutManagerScaleFirst(this@ArticleActivityList)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                   outRect.bottom=10
                    outRect.left=10
                    outRect.right=10
                }
            })
            val ad=object : BaseRvAdapter<String>(articles){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_article_title
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_title,getItemData(position))
                    holder.setText(R.id.tv_description,"${getItemData(position)}  description.....")
                    holder.itemView.setOnClickListener {
                        startActivity(Intent(this@ArticleActivityList,ArticleActivityDetail::class.java)
                                .putExtra("index",position)
                                .putExtra("lists",articles))
                    }
                }

            }

            adapter=HFAdapter(ad as RecyclerView.Adapter<RecyclerView.ViewHolder>).apply {
                val inflater=LayoutInflater.from(this@ArticleActivityList)
//                addHeader(inflater.inflate(R.layout.item_classify,rv_detail,false))
//                addHeader(inflater.inflate(R.layout.item_contact,rv_detail,false))
//                addFooter(inflater.inflate(R.layout.item_contact,rv_detail,false))
//                addFooter(inflater.inflate(R.layout.item_classify,rv_detail,false))
            }
        }
    }
}