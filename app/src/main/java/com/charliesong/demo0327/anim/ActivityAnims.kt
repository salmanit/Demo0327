package com.charliesong.demo0327.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_anims.*

/**
 * Created by charlie.song on 2018/6/6.
 */
class ActivityAnims:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anims)
        defaultSetTitle("简单动画")
        btn_object.setOnClickListener {
          var x=  ObjectAnimator.ofFloat(iv_anim,"ScaleX",0.1f,1f).setDuration(3000)
          var y=  ObjectAnimator.ofFloat(iv_anim,"ScaleY",0.1f,1f).setDuration(2000)

           AnimatorSet().apply {
               playTogether(x,y)
               start()
           }

        }
    btn_frame.setOnClickListener {

    }
        btn_property.setOnClickListener {
        var anim=TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,2f)
            anim.duration=3000
            anim.fillAfter=true

            iv_anim2.startAnimation(anim)
        }

        btn_frame.setOnClickListener {
                var drawable=iv_anim3.drawable as AnimationDrawable
                drawable.apply {
                    if(start){
                        stop()
                    }else{
                        start()
                    }
                    start=!start
                }
        }
        iv_anim2.setOnClickListener {
            Snackbar.make(findViewById(android.R.id.content),"click.....",3000).show()
        }

        sb_move.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            var oldProgress=0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    circleView(oldProgress,progress)
                    oldProgress=progress
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }
        var start=false

    private fun circleView(old:Int,now :Int){
          var hypot=  Math.hypot(iv_top.width.toDouble(),iv_top.height.toDouble())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimationUtils.createCircularReveal(iv_top,0,0, (hypot*old/100).toFloat(), (hypot*now/100).toFloat()).apply {
                duration=60000

            }.start()
        }

    }

}