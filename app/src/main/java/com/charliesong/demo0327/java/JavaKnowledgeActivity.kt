package com.charliesong.demo0327.java

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_java_knowledge.*
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

class JavaKnowledgeActivity : BaseActivity() {

    val obj = Object()
    var loop = true;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_java_knowledge)

        defaultSetTitle("java action learn")
        handleNotify()
        rotateButton()
        pattern()
        handleNumber()
        liveData.observe(this, android.arch.lifecycle.Observer {
            tv_result.setText(it)
        })
    }

    var liveData = MutableLiveData<String>()
    var lastTime = 0L
    var isAnimate = false
    val sb = StringBuffer()
    private fun handleNotify() {
        btn_start_thread.setOnClickListener {
            thread {
                synchronized(obj) {
                    while (loop) {
                        sb.append("thread=======${Thread.currentThread().name}\n")
                        liveData.postValue(sb.toString())
                        obj.wait()
                    }
                }
            }
        }
        btn_notify.setOnClickListener {
            synchronized(obj) {
                obj.notify()
            }
        }
        btn_notify_all.setOnClickListener {
            synchronized(obj) {
                obj.notifyAll()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loop = false
        synchronized(obj) {
            obj.notifyAll()
        }
    }

    private fun pattern() {
        //以空格分割字符串
        btn_pattern.setOnClickListener {
            var originlaStr = et_pattern.text.toString().trim()
            val pattern = Pattern.compile("\\s+")
            tv_pattern.text = Arrays.toString(pattern.split(originlaStr))
            check(originlaStr)
        }

//        var p=Pattern.compile("(\\d+)")
//        val  matcher=p.matcher("aa2222dd443")
//        var groupCount=matcher.groupCount()
//        println("match========${matcher.matches()}=====$groupCount")
//        while (matcher.find()){
//            val start=matcher.start()
//            val end= matcher.end()
//            println("result====start=$start=end=$end=========${matcher.group()}===${matcher.group(1)}")
//        }

//        val fileName="1/0912/3_98/hello-0912_3_98_0025.zip"
//        val versionPattern = Pattern.compile("(\\S+)_(\\d+)_(\\d+)_(\\d+).zip")
//        val patternMatcher = versionPattern.matcher(fileName)
//        if (!patternMatcher.find() || patternMatcher.groupCount() !== 4) {
//            println("count==========${patternMatcher.groupCount()}")
//            return
//        }
//        try {
//            val Version = patternMatcher.group(1)
//            val majorVersion = Integer.valueOf(patternMatcher.group(2))
//            val minorVersion = Integer.valueOf(patternMatcher.group(3))
//            val subminorVersion = Integer.valueOf(patternMatcher.group(4))
//            println("========$Version=====$majorVersion===$minorVersion===$subminorVersion")
//        } catch (e: NumberFormatException) {
//
//        }

//        val pattern=Pattern.compile("\\d+")
//        val matcher=pattern.matcher("2332ddd")
//         val r=matcher.lookingAt()
//        var matcher2=pattern.matcher("aa222233dd")
//          val  r2=matcher2.lookingAt()
//        println("======$r===$r2")
        // ======true===false
//        val regex = "^(010|02\\d|0[3-9]\\d{2})(\\d{6,8}$)"
//        val pattern = Pattern.compile(regex)
//        val matcher = pattern.matcher("02552160433")
//        if (matcher.find()) {
//            println("122====${matcher.group(1)}============${matcher.group(2)}")
//        }
    }

    private fun rotateButton() {
        btn_test.setOnClickListener {
            if (isAnimate) {
                return@setOnClickListener
            }
            isAnimate = true
            ValueAnimator.ofInt(1, 360).apply {
                duration = 3000
                addUpdateListener {
                    val current = System.currentTimeMillis()
                    val angle = it.animatedValue as Int
                    if (current - lastTime > ValueAnimator.getFrameDelay() || angle == 360) {
                        lastTime = current
                        val params = btn_test.layoutParams as ConstraintLayout.LayoutParams
                        params.circleAngle = angle.toFloat()
                        btn_test.layoutParams = params
                    }

                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        isAnimate = false

                    }
                })
                start()
            }
        }
    }

    private fun check(str: String) {
//        val str="123"
        val pattern = Pattern.compile("(\\d{3})(\\d{0,4})(\\d{0,4})")
        val m = pattern.matcher(str)
        if (m.matches()) {
            println("===========${m.group(1)}==${m.group(2)}===${m.group(3)}")
        } else {
            println("===========not match")
        }
    }

    private fun handleNumber() {
        btn_number.setOnClickListener {
            val str = et_pattern.text.toString()
            val sp = SpannableString(str)
            val matcher = Pattern.compile("\\d+").matcher(str)
            while (matcher.find()) {
                sp.setSpan(ForegroundColorSpan(Color.BLUE), matcher.start(), matcher.end(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            tv_number.setText(sp)
        }
    }
}
