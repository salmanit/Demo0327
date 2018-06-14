package com.charliesong.demo0327.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_paging_fragments.*

class ActivityPagingFragments:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_paging_fragments)
        defaultSetTitle("page 3 data source")
        vp_page.adapter=MyVpAdapter(supportFragmentManager)
        tab_page.setupWithViewPager(vp_page)
    }

    var titles= arrayOf("PageKeyed","ItemKeyed","Positional")
    inner class MyVpAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            when(position){
                0->{
                    return FragmentPageKeyedDS()
                }
                1->{
                    return FragmentItemKeyedDS()
                }
                2->{
                   return FragmentPositionalDS()
                }
                else ->{
                    return Fragment()
                }
            }
        }

        override fun getCount(): Int {
           return titles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}