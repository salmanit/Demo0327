package com.charliesong.demo0327.brief

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_flip_board_setting.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by charlie.song on 2018/5/23.
 */
class ActivityFlipBoardSetting:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_board_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle("flip board setting")
            setDisplayHomeAsUpEnabled(true)
        }
        initRvDatas()
        initRv()
    }
    var screenWidht=0
    var datas= arrayListOf<FlipSettingBean>()
    var subTypes= arrayListOf<String>("头条","图片","社会","国际","国内")
    private fun initRvDatas() {
        screenWidht=windowManager.defaultDisplay.width
        datas.add(FlipSettingBean("news",subTypes,"#ff0000"))
        datas.add(FlipSettingBean("business",subTypes,"#647464"))
        datas.add(FlipSettingBean("technology",subTypes,"#7575be"))
        datas.add(FlipSettingBean("sports",subTypes,"#30ceac"))
        datas.add(FlipSettingBean("video",subTypes,"#E7D50E"))
        datas.add(FlipSettingBean("fashion",subTypes,"#2283BF"))
        datas.add(FlipSettingBean("foods",subTypes,"#FFAEB9AE"))
        datas.add(FlipSettingBean("travel",subTypes,"#FFA58DBC"))
        datas.add(FlipSettingBean("recreation",subTypes,"#FFF4A34C"))
        datas.add(FlipSettingBean("science",subTypes,"#FFFA4582"))

    }

    private fun initRv(){
        rv.apply {
            layoutManager=LinearLayoutManager(this@ActivityFlipBoardSetting)

            adapter=object :BaseRvAdapter<FlipSettingBean>(datas){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_flip_setting
                }

                var mSharedPool = RecyclerView.RecycledViewPool()
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    var data=getItemData(position)
                    var color=Color.parseColor(data.colorStr)
                    holder.getView<TextView>(R.id.tv_type).apply {
                        text=data.type
                        if(data.checked){
                            setBackgroundResource(R.drawable.shape_white_stoke)
                        }else{
                            setBackgroundColor(color)
                        }
                    }

                    holder.getView<CheckBox>(R.id.cb).isChecked=data.checked
                    holder.getView<View>(R.id.layout_expand).apply {
                        visibility=if(data.expanded) View.VISIBLE else View.GONE
                        setBackgroundColor(color)
                    }
                    holder.getView<View>(R.id.view_mask).apply {
                        setBackgroundColor(color)
                        visibility=if(data.checked)View.VISIBLE else View.GONE //蒙版的可见性修改
                    }

                    holder.getView<TextView>(R.id.tv_expand).apply {
                        text=if(data.expanded) "collpse" else "expand"
                        setOnClickListener {
                            data.expanded=!data.expanded
                            text=if(data.expanded) "collpse" else "expand"
                            //底部的通过动画来修改bottomMargin实现动态展开的效果
                            holder.getView<View>(R.id.layout_expand).apply {
                                var anim=ValueAnimator.ofInt(0,-50)

                                if(data.expanded){
                                    anim=ValueAnimator.ofInt(-50,0)
                                    visibility=View.VISIBLE
                                }
                                anim.setDuration(200)
                                anim.start()
                                anim.addUpdateListener {
                                    holder.getView<View>(R.id.layout_expand).apply {
                                        var params=layoutParams as LinearLayout.LayoutParams
                                        params.bottomMargin=it.animatedValue as Int
                                        layoutParams=params
                                    }
                                }
                            }
                        }
                    }

                    holder.getView<View>(R.id.layout_top).setOnClickListener {
                        data.checked=!data.checked
                        holder.getView<CheckBox>(R.id.cb).isChecked=data.checked
                        //展开的子类布局收缩
                        holder.getView<View>(R.id.layout_expand).apply {
                            var params=layoutParams as LinearLayout.LayoutParams
                            params.bottomMargin=-50
                            layoutParams=params
                        }
                        //文字修改
                        data.expanded=false
                        holder.getView<TextView>(R.id.tv_expand).text="expand"
                        //取消旧的动画
                        with( holder.getView<View>(R.id.view_mask)){
                            if(animation!=null){
                                animation.cancel()
                            }
                        }
                        //开启动画
                        var samll=0.1f
                        var big=1f
                        var scaleAnim=ScaleAnimation(1f,60f/screenWidht,1f,25f/150,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                        if(data.checked){
                             scaleAnim=ScaleAnimation(60f/screenWidht,1f,25f/150,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                        }
                        scaleAnim.duration=200
                        scaleAnim.setAnimationListener(object :Animation.AnimationListener{
                            override fun onAnimationRepeat(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                holder.getView<View>(R.id.view_mask).visibility=if(data.checked)View.VISIBLE else View.GONE
                                holder.getView<TextView>(R.id.tv_type).apply {
                                    if(data.checked){
                                    }else{
                                        setBackgroundColor(color)
                                    }
                                }
                            }

                            override fun onAnimationStart(animation: Animation?) {
                                holder.getView<TextView>(R.id.tv_expand).visibility=if(data.checked)View.VISIBLE else View.GONE
                                holder.getView<TextView>(R.id.tv_type).apply {
                                    if(data.checked){
                                        setBackgroundResource(R.drawable.shape_white_stoke)
                                    }
                                }
                            }
                        })
                        holder.getView<View>(R.id.view_mask).startAnimation( scaleAnim)
                        holder.getView<View>(R.id.view_mask).visibility=View.VISIBLE
                    }
                    holder.getView<RecyclerView>(R.id.rv_sub_types).apply {
                        recycledViewPool=mSharedPool
                        if(adapter==null){
                            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false).apply {
                                recycleChildrenOnDetach=true

                            }

                            addItemDecoration(object :RecyclerView.ItemDecoration(){
                                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                                    outRect.right=11

                                }
                            })

                            adapter=object :BaseRvAdapter<String>(data.subTypes){
                                override fun getLayoutID(viewType: Int): Int {
                                    return R.layout.item_flip_sub_type
                                }

                                override fun onBindViewHolder(holder: BaseRvHolder, p: Int) {
                                    holder.setText(R.id.tv_sub_type,getItemData(p)+":$position")

                                }

                                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {
                                    println("147==============$position=====")
                                    return super.onCreateViewHolder(parent, viewType)
                                }
                            }
                        }else{
                            (adapter as BaseRvAdapter<String>).initData(data.subTypes!!)
                        }

                    }
                }

            }
        }
    }
}