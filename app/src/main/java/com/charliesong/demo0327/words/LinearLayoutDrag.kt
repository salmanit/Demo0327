package com.charliesong.demo0327.words

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.LinearLayout
import android.view.MotionEvent
import android.view.View
import com.charliesong.demo0327.BaseRvAdapter
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_words.*
import java.util.*


/**
 * Created by charlie.song on 2018/4/28.
 */
class LinearLayoutDrag:LinearLayout{
    constructor(context: Context?) : super(context){
        initDrag()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initDrag()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initDrag()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        println("==============onlayout")
    }
    private lateinit var viewDragHelper: ViewDragHelper
    private lateinit var rv_words:RecyclerView
    private var oldLeft=0;
    private var oldTop=0
    private var lastInser=-1
    private fun initDrag(){

        viewDragHelper= ViewDragHelper.create(this,object : ViewDragHelper.Callback(){
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                println("try capture  ${child}  pointer id=$pointerId")
                if(child.id== R.id.tv_word_insert){
                    oldLeft=child.left
                    oldTop=child.top
                    lastInser=-1
                    rv_words=findViewById(R.id.rv_words)
                    return true
                }
                return false
            }
            //返回值用来限制控件可以移动的范围的
            override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
                return  left
            }

            override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
                return top
            }
            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                println("change ==$left  $top  $dx  $dy")
                var insert=-1
                if(top+changedView.height>rv_words.top&&top<rv_words.bottom){//保证移动的控件在rv内部才做处理
                    for( i in 1 until rv_words.childCount){
                        var child=rv_words.getChildAt(i)
                        if(left<=child.left || left+changedView.width<=child.right){
                          insert=i
                            break
                        }
                    }
                }
                println("insert==========$insert"  )
                if(insert!=-1){
                    if(lastInser==-1){//first insert
                        (rv_words.adapter as BaseRvAdapter<WordBean>).datas.add(insert, WordBean("haha",true))
                        rv_words.adapter.notifyItemInserted(insert)
                    } else if(lastInser!=insert){

                        Collections.swap((rv_words.adapter as BaseRvAdapter<WordBean>).datas,lastInser,insert)
                        rv_words.adapter.notifyItemMoved(lastInser,insert)
                    }
                    println("insert==========$insert  lastInsert======$lastInser")
                    lastInser=insert
                }else{
                    println("lastInsert=======$lastInser")
                    if(lastInser!=-1){
                        (rv_words.adapter as BaseRvAdapter<WordBean>).datas.removeAt(lastInser)
                        rv_words.adapter.notifyItemRemoved(lastInser)
                        lastInser=-1
                    }
                }
                //现在碰到问题解决不了先放弃。。也就是rv_word  notify的时候，布局会刷新，这个类重新走了onlayout方法，完事我们拖动的控件又回到原始位置去了。
            }

            override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                println("release=   $xvel   $yvel  $oldLeft -- $oldTop")
                //这是就是反弹回初始位置
//                viewDragHelper.settleCapturedViewAt(oldLeft,oldTop)
//                postInvalidate()

//                if (viewDragHelper.smoothSlideViewTo(releasedChild, oldLeft,oldTop)) {
//                    ViewCompat.postInvalidateOnAnimation(releasedChild);
//                    postInvalidate();
//                }
            }
        })
    }
    //处理是否拦截
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //由viewDragHelper 来判断是否应该拦截此事件
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //将触摸事件传给viewDragHelper来解析处理
        viewDragHelper.processTouchEvent(event)
        //消费掉此事件，自己来处理
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    private fun handleMove(){

    }
}