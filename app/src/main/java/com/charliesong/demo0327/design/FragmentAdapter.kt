package com.charliesong.demo0327.design

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by charlie.song on 2018/4/26.
 */
class FragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    var titles= arrayOf("detail","comment")
    override fun getItem(position: Int): Fragment {
        when(position){
            0->return FragmentDetail()
            1->return FragmentComment()
            else ->return Fragment()
        }
    }

    override fun getCount(): Int {
        return  titles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}