package com.charliesong.demo0327.design

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.fragment_comment.*

/**
 * Created by charlie.song on 2018/4/26.
 */
class FragmentComment : BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.fragment_comment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_test.apply {
            layoutManager=LinearLayoutManager(activity)

            adapter=object :BaseRvAdapter<String>(){
                override fun getLayoutID(viewType: Int): Int {
                    return  R.layout.item_contact
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                   holder.setText(R.id.tv_name,"temp data $position")
                    holder.setImageRes(R.id.iv_head,R.mipmap.ic_launcher_round)
                }

                override fun getItemCount(): Int {
                    return 50
                }
            }
        }


    }
}