package com.charliesong.demo0327.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open  abstract class BaseRVDBAdapter<T,B :ViewDataBinding>:RecyclerView.Adapter<RecyclerView.ViewHolder>{
    var listsData= arrayListOf<T>()

    constructor(listsData: ArrayList<T>) : super() {
        this.listsData = listsData
    }

    constructor() : super()
    fun setData(listsData: ArrayList<T>){
        this.listsData = listsData
        notifyDataSetChanged()
    }
    fun addData(addData: ArrayList<T>){
        var start=this.listsData.size
        var count=addData.size;
        this.listsData.addAll(addData)
        notifyItemRangeInserted(start,count)
    }
    abstract  fun getLayoutID(viewType: Int):Int;
    abstract fun handleDataBinding(b :B,position: Int);
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHoler(DataBindingUtil.inflate<B>(LayoutInflater.from(parent.context), getLayoutID(viewType), parent, false).root)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        DataBindingUtil.getBinding<B>(holder.itemView)?.apply{
            handleDataBinding(this,position)
        }
    }

    override fun getItemCount(): Int {
       return listsData?.size?:0
    }

    class  MyViewHoler(v :View):RecyclerView.ViewHolder(v){

    }



}