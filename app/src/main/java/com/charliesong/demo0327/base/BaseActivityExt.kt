package com.charliesong.demo0327.base

/**
 * Created by charlie.song on 2018/4/20.
 */

fun showSnackBar(msg:String,action:ByteArray.()->Unit){
        println("============$msg")
        var arr=ByteArray(msg.length);
        arr.run {
                println("size==$size")
                action() }
}