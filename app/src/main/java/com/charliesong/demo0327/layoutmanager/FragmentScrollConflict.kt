package com.charliesong.demo0327.layoutmanager

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.fragment_simple_rv.*

/**
 * Created by charlie.song on 2018/5/16.
 */
class FragmentScrollConflict : BaseFragment() {
    override fun getLayoutID(): Int {
        return R.layout.fragment_simple_rv
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }
    var datas= arrayListOf<String>()
    private fun initData() {
        for(i in 0 until 20){
            datas.add("classify $i")
        }
        rv_home.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {

                    outRect.bottom=5
                }
            })
            adapter = object : BaseRvAdapter<String>(datas) {
                override fun getLayoutID(viewType: Int): Int {
                    when (viewType) {
                        -1 -> return R.layout.item_header_rv
                        else -> return R.layout.item_scroll_conflict_text
                    }
                }

                override fun getItemViewType(position: Int): Int {
                    when (position) {
                        0 -> return -1;
                        else -> return 1
                    }
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    when(getItemViewType(position)){
                        -1->{
                            var temp= arrayListOf<String>()
                            temp.addAll(datas.subList(0,18))
                            holder.setText(R.id.tv_classify,getItemData(position))
                            holder.getView<RecyclerView>(R.id.rv_classify).apply {
                                layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                                addItemDecoration(object :RecyclerView.ItemDecoration(){
                                    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                                        outRect.right=50
                                    }
                                })
                                adapter = object : BaseRvAdapter<String>(temp) {
                                    override fun getLayoutID(viewType: Int): Int {
                                        return R.layout.item_simple_text
                                    }

                                    override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                                        holder.setText(R.id.tv_content,getItemData(position)+position)

                                    }
                                }
                            }
                        }
                        1->{
                            holder.setText(R.id.tv_place,getItemData(position))
                        }
                    }
                }
            }
        }
    }
}