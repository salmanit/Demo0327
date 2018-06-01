package com.charliesong.demo0327.classify

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.Region
import android.util.AttributeSet
import android.view.View

/**
 * Created by charlie.song on 2018/5/18.
 */
class ClipView:View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
    }
    override fun onDraw(canvas: Canvas) {

//        super.onDraw(canvas)
        println("11============on draw")
        var p=Path()
        p.addCircle(40f,0f,20f,Path.Direction.CCW)
        canvas.clipPath(p, Region.Op.DIFFERENCE)
        p.reset()
        p.addCircle(100f,20f,20f,Path.Direction.CCW)
        canvas.clipPath(p)
        canvas.clipRect(300f,0f,400f,50f, Region.Op.DIFFERENCE)
        canvas.clipRect(500f,0f,700f,30f, Region.Op.DIFFERENCE)
//        canvas.drawColor(Color.RED)
    }

    override fun draw(canvas: Canvas?) {
        println("11============draw")
        super.draw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        println("11============dispatch Draw")
    }
}