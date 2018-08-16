package com.charliesong.demo0327.pathanim

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import com.mcxtzhang.pathanimlib.PathAnimView
import com.mcxtzhang.pathanimlib.utils.SvgPathParser
import kotlinx.android.synthetic.main.activity_path_anim.*

/**
 * Created by charlie.song on 2018/5/2.
 */
class ActivityPathAnim: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_anim)
        defaultSetTitle("path anim")
        try {
            val s = getString(R.string.path_clock)
            val p = SvgPathParser().parsePath(s)
            val pathAnimView1=PathAnimView(this)
            findViewById<ViewGroup>(R.id.root_view).addView(pathAnimView1, ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100))
            val pv= pathAnimView1.setSourcePath(p).setColorBg(Color.WHITE).setColorFg(Color.BLACK)
            pathAnimView1.setOnClickListener {
                pv.startAnim()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }



    }


}