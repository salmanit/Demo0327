package com.charliesong.demo0327.design

import android.os.Bundle
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_scroll_test.*

/**
 * Created by charlie.song on 2018/4/26.
 */
class ActivityScorllTest:BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_test)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setTitle("game")
            setDisplayHomeAsUpEnabled(true)
        }
        vp.adapter=FragmentAdapter(supportFragmentManager)

        tab_layout.setupWithViewPager(vp)

    }


}