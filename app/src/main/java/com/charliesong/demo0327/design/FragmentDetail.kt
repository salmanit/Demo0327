package com.charliesong.demo0327.design

import android.os.Bundle
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.R

/**
 * Created by charlie.song on 2018/4/26.
 */
class FragmentDetail: BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.fragment_detail
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//    }
//    var showOnce=false
//    override fun setMenuVisibility(menuVisible: Boolean) {
//        if(!showOnce&&menuVisible){
//            showOnce=true
//            loadData()
//        }
//        super.setMenuVisibility(menuVisible)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//    }
//    fun loadData(){
//        if(!showOnce){
//            return
//        }
//
//        //load data
//    }
}