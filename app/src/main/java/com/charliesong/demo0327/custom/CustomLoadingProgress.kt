package com.charliesong.demo0327.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by charlie.song on 2018/4/2.
 */
class CustomLoadingProgress:View{

    constructor(context: Context?) : super(context){
        doSomething()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        doSomething()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        doSomething()

    }
    fun doSomething() {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        paint.color=Color.parseColor("#55888888")
        paint.style=Paint.Style.FILL
        println("===============do some ")
//        paint2.style=Paint.Style.FILL
    }
    var paint=Paint()
    var paint2=Paint()
    var radius=0f;
    var rectF=RectF()
    var rectFSmall2=RectF()
    var path=Path()
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        radius=Math.min(width/2f,height/2f)-10
        rectF.set(-radius,-radius,radius,radius)
        rectFSmall2.set(-radius*0.5f,-radius*0.5f,radius*0.5f,radius*0.5f)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate((width/2).toFloat(), (height/2).toFloat())
        //画圆角背景
        canvas.drawRoundRect(rectF,11f,11f,paint)
        //扣除中心圆
        paint2.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        canvas.drawCircle(0f,0f,radius*0.7f,paint2)
        paint2.setXfermode(null)
        //画进度
        canvas.drawArc(rectFSmall2,270f,-270f,true,paint)
        canvas.restore()
    }
}