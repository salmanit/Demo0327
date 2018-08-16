package com.charliesong.demo0327.contact

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils

class ItemDecorationC:ItemDecorationContact<Contact>(){
    override fun getDrawText(t: Contact): CharSequence {
        return t.index
    }

    override fun indexEqual(t: Contact, t1: Contact): Boolean {
        return TextUtils.equals(t.index.substring(0,1),t1.index.substring(0,1))
    }

    override fun drawFloatingBgText(c: Canvas, text: CharSequence, x: Float, y: Float, paint: Paint, rect: Rect) {
        val radius=indexHeight/2-6f
        //重写以下，原本的文字是从左边开始的，这里要添加的一个圆圈，所以从圆的中心开始画，因此在init方法里重新设置了paint的textAlign属性
        c.drawText(text,0,1,x+radius,y,paintFloatBgText)
        c.drawCircle(x+radius,rect.centerY().toFloat(),radius,paintCircle)//文字的y值是修正过的，在中心点下边一点，所以这里不能用那个y
    }
    val paintCircle=Paint(Paint.ANTI_ALIAS_FLAG)
    override fun initSomeThing() {
        super.initSomeThing()
        paintFloatBgText.textAlign=Paint.Align.CENTER
        paintCircle.color=Color.WHITE
        paintCircle.style=Paint.Style.STROKE
        paintCircle.strokeWidth=2f
    }
}