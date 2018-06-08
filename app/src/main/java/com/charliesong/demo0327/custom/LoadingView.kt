package com.charliesong.demo0327.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextUtils
import android.view.animation.AnticipateInterpolator

/**
 * Created by charlie.song on 2018/6/4.
 */
class LoadingView:View{
    constructor(context: Context?) : super(context){
        initPaint()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initPaint()
    }
    var colors = intArrayOf(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.GRAY, Color.parseColor("#215980"))//6个小球的颜色
    var startAngle = 0f //旋转的角度,弧度表示
    var paint = Paint(ANTI_ALIAS_FLAG) //小球用
    var paintBG = Paint(Paint.ANTI_ALIAS_FLAG)//画背景
    var paintClear=Paint() //裁剪中心用
    private fun initPaint() {
        paint.setStyle(Paint.Style.FILL)
        paintBG.setColor(Color.WHITE)
        paintBG.style=Paint.Style.FILL
        paintClear.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))//如果view设置了背景，那么clear的部分就会成灰黑色。
    }

    var radius = 0f//原始几个小球的距离中心点的距离
    var radiusFactor = 1f//小球距离中心点的比例，收缩用
    var clearRadius=0f;//中间透明的半径
    var path=Path()
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            radius=1f/4*Math.min(width,height)
        }

    }
    var text=""
    fun showText(msg:String){
        text=msg
        postInvalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(radius==0f){
            return
        }

        canvas.save()
        canvas.translate(width/2f,height/2f)
        if(clearRadius>0){
            //注释的方法，如果要用，则得确保没有设置background，否则clear的部分就成了黑色了。
//            canvas.drawColor(Color.DKGRAY)
//            canvas.drawCircle(0f,0f,clearRadius,paintClear)
            canvas.clipPath(path.apply {
                reset()
                addCircle(0f,0f,clearRadius,Path.Direction.CW)
            },Region.Op.DIFFERENCE)
            canvas.drawColor(Color.DKGRAY) //需要注意，裁剪的时候先裁剪后绘制，否则无效
        }else{
            canvas.drawColor(Color.DKGRAY)
            //draw 6 balls
            (0 ..5).forEach {
                val r=radius*radiusFactor;
                val angle=Math.PI/180*60*it+startAngle
                val x=r*Math.cos(angle)
                val y=r*Math.sin(angle)
                paint.color=colors[it]
                canvas.drawCircle(x.toFloat(),y.toFloat(),10f,paint)
            }
        }
        canvas.restore()

        if(!TextUtils.isEmpty(text)){
            canvas.drawText(text,0,text.length,20f,20f,paint);
        }
    }
    private fun resetDefault(){
        startAngle=0f
        radiusFactor=1f
        clearRadius=0f;
        animaRotate?.cancel()
        anima2?.cancel()
        anima3?.cancel()
        animaRotate=null
        anima2=null
        anima3=null
    }
    var animaRotate:ValueAnimator?=null
     fun startLoading(){
         resetDefault()
        animaRotate=ValueAnimator.ofFloat(0f,Math.PI.toFloat()*2).apply {
            setDuration(2000L)
            repeatCount=ValueAnimator.INFINITE
            addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    startAngle=animation.animatedValue as Float
                    postInvalidate()
                }
            })
            interpolator=null//改为线性加速，默认是中间加速的
            start()
        }
    }
    var anima2:ValueAnimator?=null
    fun stopLoading(){
        animaRotate?.cancel()
        anima2=ValueAnimator.ofFloat(1f,0f).apply {
            resetDefault()
            addUpdateListener( object :ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    radiusFactor=animation.animatedValue as Float
                    postInvalidate()
                }
            })
            addListener(object :AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    startShowByCircle()
                }
            })
            interpolator=AnticipateInterpolator()
            start()
        }
    }
    var anima3:ValueAnimator?=null
    private fun startShowByCircle(){
        anima3=ValueAnimator.ofFloat(0f, Math.sqrt(width*width/4.0+height*height/4.0).toFloat()).apply {
            setDuration(700L)
            addUpdateListener( object :ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    clearRadius=animation.animatedValue as Float
                    postInvalidate()
                }
            })
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetDefault()
    }
}