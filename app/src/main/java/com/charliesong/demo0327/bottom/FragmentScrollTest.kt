package com.charliesong.demo0327.bottom

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.words.ActivityWords
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.fragment_scroll_test.*

class FragmentScrollTest:BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.fragment_scroll_test
    }

val data= arrayListOf<String>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repeat(40){
            data.add("test ${it}")
        }
        rv_simple.apply {
            layoutManager=LinearLayoutManager(activity)
            addItemDecoration(ItemDecorationSpace())
            adapter=object :BaseRvAdapter<String>(data){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_contact
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_phone,getItemData(position))

                    holder.itemView.setOnClickListener {
                        if(position%2==0){
                            startActivity(Intent(activity,ActivityWords::class.java))
                        }
                    }
                }

            }
        }
    }
}