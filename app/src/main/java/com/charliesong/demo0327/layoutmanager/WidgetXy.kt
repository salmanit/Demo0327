package com.charliesong.demo0327.layoutmanager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class WidgetXy :View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    private var pX=0f;
    private var pY=0f;
    fun setXandY(pX:Float,pY:Float){
        this.pX=pX
        this.pY=pY
        postInvalidate()
    }
    var paint=Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(width==0){
            return
        }
        canvas.save()
        canvas.translate(width/2f,height/2f)
        paint.color=Color.BLACK
        paint.style=Paint.Style.STROKE
        paint.strokeWidth=10f
        canvas.drawCircle(0f,0f,width/3f,paint)
        if(pX!=0f){
            paint.style=Paint.Style.FILL
            paint.color=Color.RED
            canvas.drawCircle(pX,pY,5f,paint)
        }
        paint.color=Color.BLUE
        paint.textAlign=Paint.Align.CENTER
        canvas.drawText(">API21",0f,0f,paint)
        canvas.restore()
    }
}