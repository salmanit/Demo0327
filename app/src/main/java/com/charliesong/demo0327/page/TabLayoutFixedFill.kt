package com.charliesong.demo0327.page

import android.content.Context
import android.graphics.Canvas
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.graphics.Color
import android.graphics.Paint
import android.widget.LinearLayout
import android.graphics.RectF
import android.support.design.R
import android.support.v4.view.ViewCompat

class TabLayoutFixedFill : TabLayout {
    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                0, R.style.Widget_Design_TabLayout)
        indicotorHeight = a.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0)
        paintLine.color = a.getColor(R.styleable.TabLayout_tabIndicatorColor, 0)
        a.recycle()
        setSelectedTabIndicatorColor(Color.TRANSPARENT)

    }

    var factor = 1f//线条的长度和文字宽度的比例，因为有的需求是比文字稍微长点。所以这里可以修改
    var indicotorHeight = 2;//线条的高度
    var paintLine = Paint()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rectIndicator.left, getHeight().toFloat() - indicotorHeight, rectIndicator.right, getHeight().toFloat(), paintLine);
    }

    override fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        super.setupWithViewPager(viewPager, autoRefresh)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updateIndicator(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {
            }
        })

    }

    var rectIndicator = RectF() //记录下要画的线条的left和right位置

     fun updateIndicator(position: Int, positionOffset: Float) {
        if(position>=tabCount){
            return
        }
        var rectF = getTextViewRect(position)
        var rectF2 = rectF
        if (position < tabCount - 1) {
            rectF2 = getTextViewRect(position + 1)
        }
        if (positionOffset < 0.5) {
            rectIndicator.left = rectF.left + rectF.width * positionOffset //第一个最左边移动到第一个的中心位置
            rectIndicator.right = rectF.right + (rectF2.center - rectF.right) * positionOffset * 2 //移动范围，从第一个右边，移动到另一个控件的中心位置
        } else {
            rectIndicator.left = rectF2.left - (rectF2.left - rectF.center) * (1 - positionOffset) * 2 //移动范围，从第一个中心到另一个最左边
            rectIndicator.right = rectF2.left + rectF2.width * positionOffset//第二个中心点到第二个的右边
        }
        ViewCompat.postInvalidateOnAnimation(this@TabLayoutFixedFill)
    }

    /**找出某个tabview里Textview的left和right位置*/
    private fun getTextViewRect(selectedPosition: Int): ViewOption {
        var slidingTabStrip = getChildAt(0) as LinearLayout
        var tabView = slidingTabStrip.getChildAt(selectedPosition) as LinearLayout
        var textView = tabView.getChildAt(1);
        val add = (factor - 1) * textView.width / 2
        return ViewOption(tabView.left + textView.left - add, tabView.left + textView.right + add)
    }

    /**记录下view的left,right,center ,and  width*/
    data class ViewOption(var left: Float, var right: Float, var center: Float = (right + left) / 2f, var width: Float = (right - left))
}