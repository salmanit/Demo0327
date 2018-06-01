package com.charliesong.demo0327.third

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_door_sign.*

/**
 * Created by charlie.song on 2018/5/11.
 */
class ActivityDoorSign :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door_sign)
//        animatorDoor(doorsign1)
//        animatorDoor(doorsign2)
          doorsign1.onInteractivityChanged(true)
    }

    private fun animatorDoor(view:View) {
        var rotateAnimation=RotateAnimation(0f,30f, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,0.1f)
        rotateAnimation.duration=1000
        rotateAnimation.repeatMode=Animation.REVERSE
        rotateAnimation.repeatCount=1
        rotateAnimation.setInterpolator(AccelerateInterpolator())
        view.startAnimation(rotateAnimation)
    }

}