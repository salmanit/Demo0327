package com.charliesong.demo0327.bottom

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.TabLayout
import android.view.View
import android.view.ViewTreeObserver
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bottom_sheet.*

class ActivityBottomSheet : BaseActivity() {
    var behavior: BottomSheetBehavior2<View>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)

        tab_bottom.addTab(tab_bottom.newTab().setText("first"))
        tab_bottom.addTab(tab_bottom.newTab().setText("second"))
        tab_bottom.addTab(tab_bottom.newTab().setText("third"))
        supportFragmentManager.apply {
            findFragmentById(R.id.layout_container)
                    ?: beginTransaction().replace(R.id.layout_container, FragmentScrollTest()).commitAllowingStateLoss()
        }

        tab_bottom.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                tab_bottom.viewTreeObserver.removeOnGlobalLayoutListener(this)
                (tab_bottom.layoutParams as ConstraintLayout.LayoutParams).apply {
                    this.topMargin=tb.bottom
                    tab_bottom.layoutParams=this
                }
            }
        })
        xml1()
    }
    var expand=false;
    private fun xml1() {
        behavior = BottomSheetBehavior2.from(cstLayout_bottom)
        behavior?.apply {
            this.isHideable=false;
            this.setBottomSheetCallback(object : BottomSheetBehavior2.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    view_hidden.visibility = if (slideOffset < 1f) View.INVISIBLE else View.VISIBLE
                    val newExpand=slideOffset>=1f
                    if(newExpand!=expand){
                        expand=newExpand;
                        tb.setBackgroundColor(if(expand) Color.BLUE else Color.TRANSPARENT)
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }
            })

        }

        tv_title.setOnClickListener {
            BottomDialogFragment().show(supportFragmentManager, BottomDialogFragment::class.java.name)
        }
        tab_bottom.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                showMore()
            }
        })
//        chronometer.start() //奇葩的bug，我加了这个玩意，那个bottom的view滑动到顶部的时候自动就滚下来一段距离。。。
    }

    private fun showMore() {
        println("showMore===${cstLayout_bottom.top}=======${cstLayout_bottom.bottom}")
        if (cstLayout_bottom.top == iv_bg.bottom - (tab_bottom.layoutParams as ConstraintLayout.LayoutParams).topMargin) {
            cstLayout_bottom.offsetTopAndBottom(-100)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        chronometer.stop()
    }
}
