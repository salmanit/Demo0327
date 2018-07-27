package com.charliesong.demo0327.weather

import android.content.Context
import android.graphics.Canvas
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import com.charliesong.demo0327.R
/**
 * Created by charlie.song on 2018/4/27.
 */
class WeatherScrollView:NestedScrollView{
    constructor(context: Context) : super(context){
        initSome()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initSome()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initSome()
    }
    private fun initSome(){
        dp220= (context.resources.displayMetrics.density*220).toInt();
    }
   lateinit var hiddenView: View;
    var dp220=220;//这个就是那个默认隐藏的控件的高度，在xml里这个控件初始的时候设置了android:layout_marginTop="-220dp"，也就是不可见的
    override fun draw(canvas: Canvas?) {
        super.draw(canvas) //简单看下scrollview的代码就可以知道，滚动的时候它会调用这个修改子控件的位置
        println("================draw")
        if(!touch){
            return
        }
        hiddenView=findViewById(R.id.tv_hidden) //那个默认不可见的布局，也即是marginTop为负的控件本身的高度
        var result=scrollY-dp220;
        if(result<=0){
          var params=  hiddenView.layoutParams as LinearLayout.LayoutParams
            params.topMargin=result //不停的修改topMargin直到完全可见
            hiddenView.layoutParams=params
        }
        onScrollChangeListener?.run {
            onScroollY(scrollY.toFloat()) //这回掉就是为了处理下上边的动画，
        }
    }
    var touch=false
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        var result= super.onTouchEvent(ev)
        if(ev.action==MotionEvent.ACTION_DOWN){
            touch=true
        }
        if(ev.action==MotionEvent.ACTION_UP||ev.action==MotionEvent.ACTION_CANCEL){
            touch=false
            val dp180=dp220*180/220; //180是我们默认设置的滚动这部分距离最高点的padding。滚动一半我们就还原，滚动超过一半就让它滚到顶部
             if(scrollY<dp180){
                 if(scrollY<dp180/2){
                     smoothScrollTo(0,0)
                 }else{
                     smoothScrollTo(0,dp180)
                 }
                 onScrollChangeListener?.run {
                     onScroollY(scrollY.toFloat())
                 }
            }
        }
        return result
    }
     var onScrollChangeListener:ScrollChangeListener?=null
}