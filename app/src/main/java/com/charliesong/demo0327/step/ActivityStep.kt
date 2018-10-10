package com.charliesong.demo0327.step

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_step.*
import android.support.v4.view.ViewCompat.requestApplyInsets
import android.support.v4.view.ViewCompat.setFitsSystemWindows
import android.databinding.adapters.ViewBindingAdapter.setPadding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout

class ActivityStep : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
        defaultSetTitle("samsung step")

        btn_temp.setOnClickListener {
            if(diaFragment==null){
                diaFragment=TempDialog()
            }
            diaFragment?.show(supportFragmentManager,"test")
        }
    }
    var diaFragment:DialogFragment?=null

    class TempDialog :DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val contentV=LayoutInflater.from(activity).inflate(R.layout.dialog_simple_test,null)
            contentV.findViewById<View>(R.id.textView).setOnClickListener {
                setupDecor()
            }
            val  dialog=AlertDialog.Builder(activity).setTitle("test").setView(contentV).setNegativeButton("cancel",null)
                    .setPositiveButton("ok",null).create()

            return dialog
        }

//        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//            return inflater.inflate(R.layout.dialog_simple_test,container,false)
//        }
        override fun onStart() {
            super.onStart()
            val window = dialog.window
//    window.setWindowAnimations(R.style.dialog_bottom_show)
            val param = window!!.attributes
            param.width = resources.displayMetrics.widthPixels
//            param.gravity=Gravity.BOTTOM
            window.attributes = param
            window.decorView.setPadding(0,0,0,0)
            window.decorView.setBackgroundColor(Color.RED)
            window.setBackgroundDrawable(ColorDrawable(Color.RED))
        }

        private fun checkView(decor:View){
            if(decor!=null&&decor is ViewGroup){
                val vg=decor as ViewGroup
                val count=vg.childCount
                repeat(count){
                    val child=vg.getChildAt(it)
                    println("parent=${vg}======child${count}====${it}====$child")
                    if(child is ViewGroup){
                        checkView(child)
                    }
                }
            }
        }
        private fun setupDecor() {
            val mWindow = dialog.window
            val decor = mWindow.getDecorView()
            checkView(decor)
           val parent=(((decor as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(0)
            println("1=========${parent}")
            println("2===============${activity!!.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)}")

            println("set padding=========${parent.width}/${resources.displayMetrics.widthPixels}====${parent}\n===" +
                    "${parent.parent}===${(parent.parent.parent)}")
            val  p=(decor as ViewGroup).getChildAt(0);
            val params=p.layoutParams as FrameLayout.LayoutParams

            println("p=${p}===========${params.leftMargin}/${params.rightMargin}/${params.topMargin}/${params.bottomMargin}===" +
                    "${ p.paddingLeft}==${p.paddingTop}==${p.paddingBottom}")

            println("decorview===========${decor.paddingLeft}/${decor.paddingRight}/${decor.paddingTop}/${decor.paddingBottom}==${decor.parent}")
        }
    }

}
