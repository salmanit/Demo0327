package com.charliesong.demo0327.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.ReplacementTransformationMethod
import android.text.method.TransformationMethod
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login2.*
import java.util.regex.Pattern

class ActivityLogin2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        val point=Point()
        windowManager.defaultDisplay.getSize(point)
        screenHeight=point.y
        stateBarHeight=getStatusHeight(this)
        view_state.layoutParams.apply {
            height=stateBarHeight
            view_state.layoutParams=this
        }
        contentView=window.decorView as ViewGroup
        addObserver()
        //dp261 就是图片的高度300【减去】最后要显示的radiobutton的高度40，加个1是因为像素float转化int有误差，所以多加了个1

        et_phone.transformationMethod=object :ReplacementTransformationMethod(){
            override fun getOriginal(): CharArray {
                return  charArrayOf( 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')

            }

            override fun getReplacement(): CharArray {
                return charArrayOf( '①', '②', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
            }
        }
        et_phone2.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                et_phone2.removeTextChangedListener(this)
                val pattern = Pattern.compile("(\\d{3})(\\d{0,4})(\\d{0,4})")
                val str=s.toString().trim().replace("-","")
                val m = pattern.matcher(str)
                if (m.matches()) {
                    var changed=m.group(1)
                    (2 .. 3).forEach {
                        val end=m.group(it)
                        if(!TextUtils.isEmpty(end)){
                            changed="$changed-$end"
                        }
                    }

                    et_phone2.setText(changed)
                    et_phone2.setSelection(changed.length)
                }else{
                    et_phone2.setText(str)
                    et_phone2.setSelection(str.length)
                }
                et_phone2.addTextChangedListener(this)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("beforeTextChanged==========$s==$start==$count==$after")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("onTextChanged==========$s==$start==$count==$before")
            }
        })

           btn_login.setOnClickListener {
               println("phone====${et_phone.text.toString()}=============${et_phone2.text.toString()}=========${et_psw.text.toString()}")
           }
    }
    var screenHeight=0
    var stateBarHeight=0;
    lateinit var contentView:ViewGroup
    private fun addObserver() {
        contentView.viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }

    private fun removeObserver() {
        contentView?.viewTreeObserver?.removeOnGlobalLayoutListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObserver()
    }
    var softInputShow=false;//保存键盘的显示状态，防止切换editTextView引起动画异常
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            var rect = Rect() //数字键盘和字母键盘高度不一样，如果在两个editTextView之间切换这里也会走的，
            contentView.getWindowVisibleDisplayFrame(rect)

            println("onGlobalLayout======${layout_login.height}===t/b=${layout_login.top}/${layout_login.bottom}=====$stateBarHeight" +
                    "  rect===${rect.toShortString()} screenHeight==$screenHeight")
                if (!softInputShow&&rect.bottom < screenHeight) {
                    //高度变小说明键盘弹出来了
                    println("===============键盘弹出来了==${findViewById<View>(R.id.action_mode_bar_stub)}")
                    softInputShow=true

                    startUIAnimator(true)
                } else if (softInputShow&&rect.bottom >= screenHeight) {
                    //键盘消失了
                    println("===============键盘消失了")
                    softInputShow=false
                    view_state.visibility=View.GONE
                    startUIAnimator(false)
                }
        }

    }

    private fun startUIAnimator(softInputShow:Boolean){
        removeObserver()
        ValueAnimator.ofFloat(0f,1f)
                .apply {
                    duration = 500
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            addObserver()
                            if(softInputShow){
                                view_state.visibility=View.VISIBLE
                            }
                        }
                    })
                    addUpdateListener {
                        var value = it.animatedValue as Float
                        if(!softInputShow){
                            value=1-value
                        }
                        layout_root.translationY =( -dp2px(261)+stateBarHeight) * value

                        if(rb1.isChecked){
                            updateState(rb1,rb2,1-value)
                        }else{
                            updateState(rb2,rb1,1-value)
                        }
                    }
                    start()
                }
    }
    fun dp2px(dp: Int): Float {
        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.density * dp
    }
    val  colorBG=ColorDrawable(Color.BLUE)
    /**
     * @param vCheck 当前选中的那个，用来变色
     * @param vUncheck 没有选中的那个
     * @param factor 要隐藏的那个vuncheck的当前比重*/
    private fun updateState(vCheck:View,vUncheck:View,factor:Float){
        //vcheck 修改背景色
        colorBG.alpha= ((1-factor)*255).toInt()
        vCheck.background=colorBG

        //vuncheck修改显示的比重
        var param = vUncheck.layoutParams as LinearLayout.LayoutParams
        param.weight =factor
        vUncheck.alpha=factor
        vUncheck.layoutParams = param;
        vUncheck.background=ColorDrawable(Color.TRANSPARENT)
    }
}