package com.charliesong.demo0327.pathanim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.charliesong.demo0327.R
import kotlin.math.tan

class MusicIcon:View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val path=Path()
    val paint=Paint()
    val pathMeasure=PathMeasure()
    val positions= FloatArray(2)
    val tans=FloatArray(2)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            recyclePIC()
            pics.add(BitmapFactory.decodeResource(resources, R.mipmap.music_1_white))
            pics.add(BitmapFactory.decodeResource(resources, R.mipmap.music_2_white))
            path.reset()
            path.moveTo(width.toFloat(),height-4f.toFloat())
            path.quadTo(0f,height-4f,width/3f,0f)

            paint.apply {
                color=Color.RED;
                strokeWidth=3f;
                style=Paint.Style.STROKE
            }

            pathMeasure.setPath(path,false)
            startAnim()
        }
    }
    var animator:ValueAnimator?=null;
    fun startAnim(){
        animator?.cancel()
        animator=ValueAnimator.ofFloat(0f,1f).apply {
            setDuration(3000)
            repeatCount=ValueAnimator.INFINITE
            repeatMode=ValueAnimator.RESTART
            addUpdateListener {
                 value=it.animatedValue as Float;


                postInvalidateOnAnimation()
            }
            addListener(object :AnimatorListenerAdapter(){
                override fun onAnimationRepeat(animation: Animator?) {
                    super.onAnimationRepeat(animation)
                    repeate=true
                }
            })
            start()
        }

    }
    var repeate=false;
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawPath(path,paint)
        if(pics.size==0||width==0){
            return
        }
        val count=pics.size;
        repeat(count){
            var v=value;
            if(it>0){
               val de= v-it*1f/count
                if(de<0){
                    if(repeate){
                        v=1+de;
                    }else{
                        v=0f
                    }
                }else{
                    v=de
                }
            }
            val w:Int= (width/5f*v).toInt();
            println("i====${it}===v/value==$v/$value=====$w")
            if(w>0){
                pathMeasure.getPosTan(pathMeasure.length*v,positions, tans)
                val bp=Bitmap.createScaledBitmap(pics[it],w,w,false)
                paint.alpha= (255*(if(v<0.5f) v*2 else (1-v)*2)).toInt()
                canvas.drawBitmap(bp,positions[0],positions[1],paint)
            }
        }
    }
    val pics= arrayListOf<Bitmap>()
    var value=0.1f
    fun recyclePIC(){
        repeat(pics.size){
            pics.get(it).apply {
                if(!this.isRecycled){
                    this.recycle()
                }
            }
        }
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        recyclePIC()
    }
}