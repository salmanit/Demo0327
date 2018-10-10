package com.charliesong.demo0327.step

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*

class AllStepsShow : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    val datas= arrayListOf<StepBean>()
    val paint=Paint(Paint.ANTI_ALIAS_FLAG)
    var txtBounds=Rect()
    var intervalRange=0;
    private val gestureListener=object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            println("onFling=============${velocityX}")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            offsetX+=currentChange
            println("down==============${offsetX}=======$currentChange")
            return super.onDown(e)
        }
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            println("onScroll======${e1.getX()}/${e2.getX()}==$distanceX")
            currentChange=distanceX
            postInvalidateOnAnimation()
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }
    init {
        val calendar=Calendar.getInstance()
        repeat(50){
            datas.add(StepBean(100, calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR,-1)
        }

        paint.textAlign=Paint.Align.CENTER
        paint.textSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20f,resources.displayMetrics)
        paint.getTextBounds("31",0,2,txtBounds)
        println("text size=============${paint.textSize}")
        initSomething()
    }

    var gesture:GestureDetector?=null
    private fun initSomething(){
        gesture= GestureDetector(context,gestureListener)
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            intervalRange=(right-left)/8
        }
    }

    var offsetX=0f
    var currentChange=0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color=Color.BLACK
        val centerX=width/2f+offsetX+currentChange;
        val y=height*8/9f
        canvas.drawCircle(centerX,y-txtBounds.height()/2f,20f,paint)
        for(i in 0 until datas.size){
            val stepBean=datas.get(i)
//            println("i====$i===${stepBean.getDate()}")
            val x=centerX-intervalRange*i
            paint.color=if(i==0) Color.WHITE else Color.BLACK
            canvas.drawText(stepBean.getDate(),x,y,paint)
            if(x<0){
//                println("centerX======$centerX===${intervalRange}*${i}")
            }
        }
        val calendar=Calendar.getInstance()
        paint.color=Color.GRAY
        for(i in 1 ..4){
            calendar.add(Calendar.DAY_OF_YEAR,1)
//            println("right i=====$i=========${calendar.time}")
            canvas.drawText(StepBean(0, calendar.time).getDate(),centerX+i*intervalRange,y,paint)
            if(centerX-i*intervalRange<0){
                break
            }
        }
    }


    var oldX=0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        gesture?.onTouchEvent(event)
        val x=event.getX()
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                oldX=x;
            }
            MotionEvent.ACTION_MOVE->{
                currentChange=x-oldX;
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                currentChange=x-oldX;
            }
        }
//        postInvalidateOnAnimation()
        println("onTouchEvent    x=$x====old=${oldX}=====${event.action}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        println("dispatchTouchEvent==============${event?.action}")
//        gesture?.onTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

}