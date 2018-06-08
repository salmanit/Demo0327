package com.charliesong.demo0327.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
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

        btn_object.setOnClickListener {
          var x=  ObjectAnimator.ofFloat(iv_anim,"ScaleX",0.1f,1f).setDuration(3000)
          var y=  ObjectAnimator.ofFloat(iv_anim,"ScaleY",0.1f,1f).setDuration(2000)

           AnimatorSet().apply {
               playTogether(x,y)
               start()
           }

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


    }
var start=false

}