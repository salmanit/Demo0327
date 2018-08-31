package com.charliesong.demo0327.layoutmanager

import android.animation.*
import android.graphics.Color
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.util.Pools
import android.support.v7.widget.FitWindowsLinearLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.util.UtilNormal
import kotlinx.android.synthetic.main.activity_simple_anima.*

/**
 * Created by charlie.song on 2018/5/24.
 */
class ActivitySimpleAnima : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_anima)

        defaultSetTitle("toast")
        initTestDatas()
        rv_toast.apply {
            layoutManager = LinearLayoutManager(this@ActivitySimpleAnima, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    outRect.bottom = 10
                    outRect.left = 10
                }
            })
            itemAnimator = ItemAnimatorScaleLB2RT().apply {
                removeDuration = 700
                addDuration = 700
            }
            adapterRV = object : BaseRvAdapter<String>() {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_anima_text
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.getView<TextView>(R.id.tv_toast).apply {
                        setText(getItemData(position))
                        var bgg = background?.apply { setColorFilter(colors[index % colors.size], PorterDuff.Mode.SRC_IN) }
                        background = bgg
                    }
                }
            }
            adapter = adapterRV
        }
        reCalculateMargin()
        startSimulateRefresh()

        useTransition()
        if (Build.VERSION.SDK_INT >= 21) {
            wxytest()
        }
    }
    fun reCalculateMargin(){
        layout_container.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                layout_container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                (layout_container.layoutParams as FrameLayout.LayoutParams).apply {
                    this.topMargin=10+(supportActionBar?.height?:90)
                    println("heigth==========${supportActionBar?.height}")
                }
            }
        })
    }
    lateinit var adapterRV: BaseRvAdapter<String>
    var messages = arrayListOf<String>()
    var colors = arrayListOf<Int>()
    private fun initTestDatas() {
        messages.add("今天是个好日子。")
        messages.add("机箱的光阴不能忘。")
        messages.add("明天有出去玩的吗。")
        messages.add("有啊，要一起吗？")
        messages.add("我刚才看到xx拉。")
        messages.add("你们在说啥了")
        messages.add("听说周末有雨额，哪也去不了")
        messages.add("我们这里不下雨，没事")
        messages.add("累了，去睡了，再见。")
        messages.add("今天是个好日子。")
        messages.add("机箱的光阴不能忘。")
        messages.add("明天有出去玩的吗。")
        messages.add("有啊，要一起吗？")
        messages.add("我刚才看到xx拉。")
        messages.add("你们在说啥了")
        messages.add("听说周末有雨额，哪也去不了")
        messages.add("我们这里不下雨，没事")
        messages.add("累了，去睡了，再见。")
        colors.add(Color.RED)
//        colors.add(Color.GREEN)
        colors.add(Color.BLUE)
//        colors.add(Color.YELLOW)
//        colors.add(Color.LTGRAY)
    }

    var handler = Handler()
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startSimulateRefresh() {
        handler.postDelayed(viewRunnble, 100)
    }

    var index = 0;
    //在左边界触摸开始滑动删除的时候禁止刷新ui，否则touch事件就出问题了。
    private fun isEdgeTouch():Boolean{
        val p1=findViewById<View>(android.R.id.content).parent as ViewGroup
        val p2=p1.parent as ViewGroup
        val p3=p2.parent as ViewGroup
        println("left======0=====${p1}==========${p1.childCount}")
        println("left======1=====${p2}==========${p2.childCount}")
        println("left======2=====${p3}==========${p3.childCount}")
        val p4=p3.parent as ViewGroup?
        println("left======3=====${p4}=========")
        return findViewById<ViewGroup>(R.id.edgetouchid).getChildAt(0).left>0
    }
    //onViewCaptured================android.widget.LinearLayout{9559ca9 V.E...... .......D 0,0-1024,768}======0
    //left======0=====android.support.v7.widget.FitWindowsLinearLayout{1c0de56 V.E...... ......ID 0,0-1024,768 #7f08000d app:id/action_bar_root}==========2
    //left======1=====android.widget.FrameLayout{2616f00 V.E...... ......ID 0,0-1024,768}==========1
    //left======2=====android.widget.LinearLayout{d4e7339 V.E...... ......ID 0,0-1024,768}==========2
    //left======3=====com.charliesong.demo0327.draghelper.LeftEdgeTouchCloseLayout{8989d7e V.E...... ......ID 0,0-1024,768 #7f080089 app:id/edgetouchid}=========
    //com.android.internal.policy.PhoneWindow$DecorView{d4e7339 V.E...... R.....I. 0,0-0,0}
    val viewRunnble = object : Runnable {
        override fun run() {
            if(isEdgeTouch()){
                handler.postDelayed(this, 1000)
                return
            }
            adapterRV.apply {

                if (adapterRV.itemCount < 4) {
                    this.datas.add(messages[index % messages.size])//最后一个位置添加数据并notify
                    index++
                    notifyItemInserted(datas.size - 1)
                } else {
                    this.datas.removeAt(0)//删除第一条数据
                    notifyItemRemoved(0)
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun useTransition() {

        //初始化add动画,拉伸动画，从小到大
        var scaleAnimaX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f)
        var scaleAnimaY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        var scale = ObjectAnimator.ofPropertyValuesHolder(null as TextView?, scaleAnimaX, scaleAnimaY)
        //这里kotlin的null，半天不知道咋写,后来还是看源码里有这样的(Object)null ，才改对了kotlin的写法。
        scale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                var target = (animation as ObjectAnimator).target as View
                target.pivotY = target.height.toFloat()//修改下中心点，默认是控件的正中心，我们这里是从左下角开始变大的
            }
        })
        var transition = LayoutTransition().apply {
            setAnimator(LayoutTransition.APPEARING, scale) //这里只修改出现的动画，其他的用默认的，默认的消失动画就是一个透明度从1到0的动画。
            setDuration(500)
        }
        layout_container.layoutTransition = transition
        handler.post(addViewRunnable) //开始添加删除控件
    }

    var i = 0;
    var addViewRunnable = object : Runnable {
        override fun run() {
            if(isEdgeTouch()){
                handler.postDelayed(this, 1000)
                return
            }
            if (layout_container.childCount >= 4) {//控件已经有4的话，执行remove操作
                var tv = layout_container.getChildAt(0) as TextView
                pools.release(tv) //回收的控件留着复用，存起来
                layout_container.removeViewAt(0)
            } else {
                var tv = getView().apply {
                    text = messages[i % messages.size]
                    background = background?.apply { setColorFilter(colors[i % colors.size], PorterDuff.Mode.SRC_IN) }
                    i++
                }
                layout_container.addView(tv)
            }
            handler.postDelayed(this, 1000)
        }
    }

    var pools = Pools.SimplePool<TextView>(4)
    fun getView(): TextView {
        //先从pools 里看有没有回收的view，有的话就复用，没有就new一个
        var view = pools.acquire() ?: TextView(this).apply {
            var h = UtilNormal.dp2px(this@ActivitySimpleAnima, 50)
            layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, h)
            gravity = Gravity.CENTER
            setPadding(h / 2, 0, h / 2, 0)
            setTextSize(20f)
            setTextColor(Color.WHITE)
            setBackgroundResource(R.drawable.shape_lr_circle)
            pivotX = 0f
        }
        return view
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun wxytest() {
        var path = Path()
        var dp50 = UtilNormal.dp2px(this, 50).toFloat()
        path.moveTo(0f, dp50)
        path.lineTo(dp50, 0f)
        path.lineTo(0f, -dp50)
        path.lineTo(-dp50, 0f)
        path.close()
        anim = ObjectAnimator.ofMultiFloat(wxy, "XandY", path).setDuration(5000)
                .apply {
                    addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                        override fun onAnimationUpdate(animation: ValueAnimator) {
                            var valuesHolder = animation.animatedValue as FloatArray
                        }
                    })
                    repeatCount = 5
                    interpolator = LinearInterpolator()
                }
        anim?.start()
    }

    var anim: ObjectAnimator? = null
    override fun onPause() {
        super.onPause()
        anim?.cancel()
    }
}