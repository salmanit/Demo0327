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
    fun setText(id:Int,strRes:Int):BaseRvHolder{
        setText(id,root.context.getString(strRes))
        return this
    }
    fun setText(id:Int,str:CharSequence):BaseRvHolder{
        (getView<TextView>(id)).setText(str)
        return this
    }
    fun setImageRes(id:Int,iconRes:Int):BaseRvHolder{
        (getView<ImageView>(id)).setImageResource(iconRes)
        return this
    }
    fun setImageUrl(id:Int,url:String):BaseRvHolder{
        var iv=(getView<ImageView>(id))
    Glide.with(iv).load(url)
            .into(iv)
        return this
    }
}