package com.charliesong.demo0327.design

import android.os.Bundle
import android.view.View
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.R

/**
 * Created by charlie.song on 2018/4/26.
 */
class FragmentComment : BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.fragment_comment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}