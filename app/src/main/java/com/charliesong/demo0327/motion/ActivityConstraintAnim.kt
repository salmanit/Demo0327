package com.charliesong.demo0327.motion

import android.os.Bundle
import android.os.Environment
import android.support.constraint.ConstraintSet
import android.support.constraint.motion.MotionLayout
import android.support.transition.TransitionManager
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_constraint_anim.*
import java.io.File

class ActivityConstraintAnim : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_anim)
        defaultSetTitle("motionLayout")
        btn_test2.setOnClickListener {
//            constraintLayout1.transitionToEnd()
//            animateToKeyframeTwo()
            showToast("hello")
//            testCopyFileDir()
        }
        constraintLayout1.setTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//                println("change=========$p1===$p2==$p3")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//                println("complete============$p1")
            }
        })
    }


    fun animateToKeyframeTwo() {
        val constraintSet =  ConstraintSet()
        constraintSet.clone(this, R.layout.activity_constraint_anim2); //载入要更新的布局到constraintSet中
        TransitionManager.beginDelayedTransition(constraintLayout1); // 开启
        constraintSet.applyTo(constraintLayout1)
    }

    private fun  testCopyFile(){
        val old=File(Environment.getExternalStorageDirectory(),"temp.txt")
        old.writeBytes("temp text".toByteArray())
        //新的File的需要保证parent的路径是存在的，否则会失败
       val result= old.renameTo(File(Environment.getExternalStorageDirectory(),"temp2.txt"))
        println("result==========$result")
//        old.copyTo(old,true)
    }
    //把aaa目录下的所有文件移动到Android/data/包名/files 下，可能不准确，应该是把aaa目录移动到包名下，重命名为files
    private fun  testCopyFileDir(){
        val old=File(Environment.getExternalStorageDirectory(),"aaa/temp.txt")
        val make=old.parentFile.mkdirs()
        println("result========make $make")
        old.writeBytes("temp text".toByteArray())
        println("old file=======${old.absolutePath}=========${old.length()}")
        //新的File的需要保证parent的路径是存在的，否则会失败
       val result= old.parentFile.renameTo(getExternalFilesDir(""))
        println("result==========$result")
    }
}
