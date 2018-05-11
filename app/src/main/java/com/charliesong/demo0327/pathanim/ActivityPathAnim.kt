package com.charliesong.demo0327.pathanim

import android.graphics.Color
import android.os.Bundle
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.R
import com.mcxtzhang.pathanimlib.res.StoreHousePath
import com.mcxtzhang.pathanimlib.utils.PathParserUtils
import com.mcxtzhang.pathanimlib.utils.SvgPathParser
import kotlinx.android.synthetic.main.activity_path_anim.*

/**
 * Created by charlie.song on 2018/5/2.
 */
class ActivityPathAnim:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_anim)
        try {
//            var s = getString(R.string.path_clock)
//            var p = SvgPathParser().parsePath(s)
//            val pv= pathAnimView1.setSourcePath(p).setColorBg(Color.WHITE).setColorFg(Color.BLACK)
//            pathAnimView1.setOnClickListener {
//                pv.startAnim()
//            }
        }catch (e:Exception){
        }


    }

}