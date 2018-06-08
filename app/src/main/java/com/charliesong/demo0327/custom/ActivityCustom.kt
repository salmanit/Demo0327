package com.charliesong.demo0327.custom

import android.os.Bundle
import android.view.animation.*
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_custom.*

/**
 * Created by charlie.song on 2018/4/2.
 */
class ActivityCustom: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        loadingView.setOnClickListener {
            if(start){
                (it as LoadingView).stopLoading()
            }else{
                (it as LoadingView).startLoading()
            }
            start=!start
        }

        AccelerateInterpolator()
        AccelerateDecelerateInterpolator()
        LinearInterpolator()
        AnticipateInterpolator()
        AnticipateOvershootInterpolator()

        BounceInterpolator()
        CycleInterpolator(3f)
        DecelerateInterpolator()
        OvershootInterpolator()

        cus_pg.setOnClickListener { startCall() }
    }
    var start=false
    var interpolators= arrayListOf<Interpolator>(AccelerateInterpolator(),AccelerateDecelerateInterpolator(), AnticipateInterpolator(),AnticipateOvershootInterpolator()
    ,BounceInterpolator(),
            CycleInterpolator(3f),
            DecelerateInterpolator(),
            OvershootInterpolator())
    var index=0
    private fun startCall(){
        var inter=interpolators[index%interpolators.size]
        loadingView.showText(inter.javaClass.name)
        var trans=TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,1f)
        trans.duration=2000
        trans.interpolator=inter
        iv_call.startAnimation(trans)
        index++
    }

}