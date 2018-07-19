package com.charliesong.demo0327.page

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.widget.LinearLayout
import android.widget.TextView
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
        tab_page.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                changeTextStyle(tab.position,false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                changeTextStyle(tab.position,true)
            }
        })
    }
    private fun changeTextStyle(position: Int,selected:Boolean){
        var parent=tab_page.getChildAt(0) as LinearLayout
        var tabview=parent.getChildAt(position) as LinearLayout
        var tv=tabview.getChildAt(1) as TextView
        tv.setTypeface(if(selected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT)
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