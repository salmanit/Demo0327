package com.charliesong.demo0327.select

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_text_select.*

/**
 * Created by charlie.song on 2018/6/1.
 */
class ActivityTextSelect : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_select)
        defaultSetTitle("select text")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {



            et_1.customSelectionActionModeCallback = object : ActionMode.Callback2() {
                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    println("2==============onActionItemClicked===" + item.title)

                    if (item.itemId==android.R.id.selectAll) {
                        mode.invalidate()
                    } else {
                        mode.finish()

                    }
                    return false
                }

                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    println("2==============onCreateActionMode===" + menu.size())
                    menu.clear()
                    mode.menuInflater.inflate(R.menu.select_menu2, menu)

                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    println("2==============onPrepared===" + menu.size())

                    return true
                }

                override fun onDestroyActionMode(mode: ActionMode) {
                    println("2==============onDestroyActionMode===" + mode.title)
                }
            }
        }


    }


}