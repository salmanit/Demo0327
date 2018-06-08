package com.charliesong.demo0327.navigation

import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.Explode
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.util.UtilNormal
import kotlinx.android.synthetic.main.nav_fragment_home.*
import kotlinx.android.synthetic.main.nav_fragment_step_one.*
import kotlinx.android.synthetic.main.nav_fragment_step_one1.*
import kotlinx.android.synthetic.main.nav_fragment_step_two.*

class FragmentHome:BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.nav_fragment_home
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn1.setOnClickListener {
            val options = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()
            Navigation.findNavController(it).navigate(R.id.go_StepOne,null,options)

        }
    }
}


class FragmentStepOne:BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.nav_fragment_step_one
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.apply {
            tv_step1.text = "${tv_step1.text}+${getString("title")}+${getString("title2")}"
        }
        btn_step1.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fragmentStepOne1)
        }
    }
}
var url="https://upload.jianshu.io/users/upload_avatars/8706116/0df51b97-56d2-4363-9d7f-ecd305b9722b.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
var url2="https://upload.jianshu.io/users/upload_avatars/3994917/ae996b9a-5391-496f-b209-3367fb5b0112.png?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
var url3="https://upload.jianshu.io/users/upload_avatars/5275362/63c0bb2d-b849-4909-9136-b5aae8b01dcc.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
class FragmentStepOne1:BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.nav_fragment_step_one1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.apply {
            tv_step11.text = "this is new added step one 1"
        }

        btn_step11.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.go_StepTwo)
        }
        iv1.setOnClickListener(listener)
        iv2.setOnClickListener(listener)
        iv3.setOnClickListener(listener)
        iv4.setOnClickListener(listener)
        UtilNormal.loadCircleImage(url,iv1)
        UtilNormal.loadCircleImage(url2,iv2)
        UtilNormal.loadCircleImage(url3,iv3)
        UtilNormal.loadCircleImage(url,iv4)

    }

    val listener=object :View.OnClickListener{
        override fun onClick(v: View) {
        TransitionManager.beginDelayedTransition(layout_test,AutoTransition().addTransition(Explode()))
            changeSize(v)
            changeVisibility(v)
        }
    }
    fun changeVisibility(view :View){
        (0 until layout_test.childCount).forEach {
            var child=layout_test.getChildAt(it)
            if(child is ImageView && child!=view){
                child.visibility=if(child.visibility==View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }
    }
    fun changeSize(view: View){
//        var params=view.layoutParams
        if(view.scaleX==1f){
            view.scaleX=1.5f
            view.scaleY=1.5f
        }else{
            view.scaleX=1.0f
            view.scaleY=1.0f
        }
    }

}
class FragmentStepTwo:BaseFragment(){
    override fun getLayoutID(): Int {
        return R.layout.nav_fragment_step_two
    }

    private lateinit var mSet: AutoTransition

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_step2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.go_home)
        }


        mSet = AutoTransition()
        mSet.setOrdering(TransitionSet.ORDERING_TOGETHER)
        mSet.setDuration(222)
        mSet.setInterpolator(FastOutSlowInInterpolator())
        mSet.addTransition(TextScale())

        tv_click.setOnClickListener {
            // Note: this has to be called before BottomNavigationItemView#initialize().
            TransitionManager.beginDelayedTransition(layout_p, mSet)

            tv_click.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP,if(small) 30f else 22f)
                if(small){

                    tv_click.scaleX=1f
                    tv_click.scaleY=1f
                }else{
                    tv_click.scaleX=0.7f
                    tv_click.scaleY=0.7f
                }
                small=!small
            }

        }
    }
    var small=true
}