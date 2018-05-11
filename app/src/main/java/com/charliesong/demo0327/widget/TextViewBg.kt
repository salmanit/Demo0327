package com.charliesong.demo0327.widget

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.charliesong.demo0327.R

/**
 * Created by charlie.song on 2018/4/28.
 */
class TextViewBg:AppCompatTextView{
    constructor(context: Context) : super(context){
        initAttr(context,null)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        initAttr(context,attrs)
    }
    private var strokeWidth=0f
    private var strokeColor=Color.TRANSPARENT
    private var bgColor=Color.TRANSPARENT
    private var radius=0f
    private  var rectF= RectF()
    private  var rectFStroke= RectF()
    private var paint=Paint(Paint.ANTI_ALIAS_FLAG)
    private fun initAttr(context: Context, attrs: AttributeSet?){
        attrs?.run {
            var typeArray=context.obtainStyledAttributes(this,R.styleable.TextViewBg)
            strokeWidth= typeArray.getDimensionPixelSize(R.styleable.TextViewBg_sage_tv_stroke_width,0).toFloat()
            strokeColor=typeArray.getColor(R.styleable.TextViewBg_sage_tv_stroke_color,Color.TRANSPARENT)
            bgColor=typeArray.getColor(R.styleable.TextViewBg_sage_tv_bg_color,Color.TRANSPARENT)
            radius= typeArray.getDimensionPixelSize(R.styleable.TextViewBg_sage_tv_radius,0).toFloat()
            typeArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            rectF.set(strokeWidth,strokeWidth,right-left-strokeWidth,bottom-top-strokeWidth)
            var halfStroke=strokeWidth/2f;
            rectFStroke.set(rectF.left-halfStroke,rectF.top-halfStroke,rectF.right+halfStroke,rectF.bottom+halfStroke)
        }
    }
    override fun onDraw(canvas: Canvas) {

        if(bgColor!=Color.TRANSPARENT){
            paint.style=Paint.Style.FILL_AND_STROKE
            paint.color=bgColor
            canvas.drawRoundRect(rectF,radius,radius,paint)
        }
        if(strokeColor!=Color.TRANSPARENT){
            paint.style=Paint.Style.STROKE
            paint.strokeWidth=strokeWidth
            paint.color=strokeColor
            canvas.drawRoundRect(rectFStroke,radius,radius,paint)
        }
        super.onDraw(canvas)
    }
}