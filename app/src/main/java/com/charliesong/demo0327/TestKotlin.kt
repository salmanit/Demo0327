package com.charliesong.demo0327

/**
 * Created by charlie.song on 2018/5/18.
 */
class TestKotlin {

    var activity=false
        get() = isAdd

    fun changeAdd(add:Boolean){
        isAdd=add
    }
    var isAdd=true;


    fun main(){
        println("18============$activity")
        changeAdd(false)
        println("20===============$activity")
    }
}