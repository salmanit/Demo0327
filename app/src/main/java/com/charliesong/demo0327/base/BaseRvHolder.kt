package com.charliesong.demo0327.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by charlie.song on 2018/3/30.
 */
class BaseRvHolder : RecyclerView.ViewHolder {
    init {
        root=itemView
    }
    constructor(itemView: View) : super(itemView) {
        root=itemView
    }
    lateinit var root:View
    var views= hashMapOf<Int,View>()
    fun <T>getView(id: Int):T{
        var view=views.get(id)
        if(view==null){
            view=root.findViewById<View>(id)
            views.put(id,view)
        }
        return view!! as T
    }
    fun setText(id:Int,strRes:Int){

        setText(id,root.context.getString(strRes))
    }
    fun setText(id:Int,str:CharSequence){
        (getView<TextView>(id)).setText(str)
    }
    fun setImageRes(id:Int,iconRes:Int){
        (getView<ImageView>(id)).setImageResource(iconRes)
    }
    fun setImageUrl(id:Int,url:String){
        var iv=(getView<ImageView>(id))
    Glide.with(iv).load(url)
            .into(iv)
    }
}