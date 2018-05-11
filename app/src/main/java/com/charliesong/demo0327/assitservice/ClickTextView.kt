package com.charliesong.demo0327.assitservice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

/**
 * Created by charlie.song on 2018/4/27.
 */
class ClickTextView:TextView{
    constructor(context: Context?) : super(context){
        initPaint()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initPaint()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initPaint()
    }
    val paint=Paint()
    var colorBg=Color.RED
    var checked=false
    private fun initPaint(){
        paint.style=Paint.Style.FILL
        paint.flags=Paint.ANTI_ALIAS_FLAG
    }
    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(width/2f,height/2f)
        val radius=Math.min(width,height)/2f
        paint.color=Color.argb(43,Color.red(colorBg),Color.green(colorBg),Color.blue(colorBg))
        canvas.drawCircle(0f,0f,radius,paint)
        paint.color=colorBg
        canvas.drawCircle(0f,0f,radius-10,paint)
        if(!checked){
            paint.color=Color.parseColor("#88ffffff")
            canvas.drawCircle(0f,0f,radius,paint)
        }
        canvas.restore()

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action==MotionEvent.ACTION_DOWN){
            checked=!checked
            postInvalidate()
        }
        return super.onTouchEvent(event)
    }
}