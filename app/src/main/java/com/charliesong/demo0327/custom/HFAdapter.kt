package com.charliesong.demo0327.custom

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class HFAdapter(val adapter:RecyclerView.Adapter<RecyclerView.ViewHolder>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

     val heads= arrayListOf<View>()
    val footers= arrayListOf<View>()
    val HEADER_START=-100000
    val FOOTER_START=1000000
    fun headsize():Int{
        return heads.size
    }
    fun footersize():Int{
        return footers.size
    }
    fun addHeader(v:View){
        heads.add(v)
        notifyDataSetChanged()
    }
    fun addFooter(v:View){
        footers.add(v)
        notifyDataSetChanged()
    }
    fun removeHeader(v:View){
        heads.remove(v)
        notifyDataSetChanged()
    }
    fun removeFooter(v:View){
        footers.remove(v)
        notifyDataSetChanged()
    }

    fun clearHeader(){
        val size=heads.size
        heads.clear()
        notifyItemRangeRemoved(0,size)
    }
    fun clearFooter(){
        val size=footers.size
        footers.clear()
        notifyItemRangeRemoved(itemCount-size,size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==0){
            return adapter.onCreateViewHolder(parent,viewType)
        }else if(viewType<0){
            return  object :RecyclerView.ViewHolder(heads[-viewType+HEADER_START]){}
        }else{
            return  object :RecyclerView.ViewHolder(footers[viewType-FOOTER_START]){}
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position<heads.size){
            return HEADER_START-position
        }else if(position>=heads.size+adapter.itemCount){
            return FOOTER_START+(position-(heads.size+adapter.itemCount))
        }
        return adapter.getItemViewType(position-heads.size)
    }
    override fun getItemCount(): Int {
        return heads.size+footers.size+adapter.itemCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type=getItemViewType(position)
           if(type>HEADER_START&&type<FOOTER_START){
               adapter.onBindViewHolder(holder,position-heads.size)
           }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            if(position>=heads.size&&position<heads.size+adapter.itemCount){
                adapter.onBindViewHolder(holder,position,payloads)
            }
        }
    }

}