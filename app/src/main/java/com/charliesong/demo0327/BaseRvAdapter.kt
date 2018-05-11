package com.charliesong.demo0327

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by charlie.song on 2018/3/30.
 */
abstract class BaseRvAdapter<T> : RecyclerView.Adapter<BaseRvHolder>{

    open  var datas= arrayListOf<T>()

    public constructor(datas: ArrayList<T>) : super() {
        this.datas = datas
    }

    constructor() : super()

    fun initData(data:List<T>){
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:Array<T>){
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }
//    override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
//
//    }
    abstract  fun getLayoutID(viewType: Int):Int;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {
      var view= LayoutInflater.from(parent.context).inflate(getLayoutID(viewType),parent,false)
        return  BaseRvHolder(view)
    }

    override fun getItemCount(): Int {
      return datas.size
    }
    fun getItemData(position:Int):T{
        return datas[position]
    }
}