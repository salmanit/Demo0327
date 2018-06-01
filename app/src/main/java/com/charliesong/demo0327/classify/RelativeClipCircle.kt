package com.charliesong.demo0327.classify

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by charlie.song on 2018/5/17.
 */
class RelativeClipCircle :RelativeLayout{

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
    }

    override fun draw(canvas: Canvas) {
        println("====================draw")
        super.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        count=(w+radius)/(3*radius)
        start=(width+radius-3*radius*count)/2
        println("==================onSizeChange===$count===$start")
    }
    var start=0
    var radius=20
    var count=0
    override fun dispatchDraw(canvas: Canvas) {
        println("===================dispatchDraw========$count")
        super.dispatchDraw(canvas)
        var path=Path()
        var  p=Paint()
        p.color=Color.RED
        for(i in 0 until count){
            path.reset()
            path.addCircle(start+radius.toFloat(),0f,radius.toFloat(),Path.Direction.CCW)
//            canvas.drawPath(path,p)
            canvas.clipPath(path, Region.Op.DIFFERENCE)
            start+=3*radius
        }
    }

    override fun onDraw(canvas: Canvas) {
        println("====================onDraw")
        super.onDraw(canvas)

    }


}