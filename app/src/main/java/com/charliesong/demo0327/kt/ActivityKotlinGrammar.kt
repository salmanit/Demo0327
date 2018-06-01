package com.charliesong.demo0327.kt

import android.os.Bundle
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_kotlin_grammar.*

/**
 * Created by charlie.song on 2018/4/2.
 */
class ActivityKotlinGrammar: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_grammar)
        btn_range.setOnClickListener { rangeTest() }
        btn_action.setOnClickListener {
            actionTest()
        }
        btn_foreach.setOnClickListener { forTest() }

    }

    private fun forTest(){
        //循环N次，from 0 start
        repeat(5){
            println("a============$it")
        }
        for(i in 0 ..4){
            println("b==========$i")
        }
        for(i in 0 until 5){
            println("c==========$i")
        }
        (0..4).forEach {
            println("d==============$it")
        }
    }
    private fun actionTest(){
        var s="hello"
       s= s.also {
            s+"_also"
        }
        println("also retun this====$s")
        s= s.apply {
            s+"_apply"
        }
        println("apply and also retun this=========$s")

        var letResult= s.let {
            s.length+2
        }
        println("let return block result=======$letResult")
       var runResult= s.run {
           s.length
//           return@run 333
        }
        println("run return block result===$runResult")

        var test:String?=null

    }
    fun rangeTest(){
        val i=3;
        if(i in 0 ..10){
            println("$i is in 0 ..10")
        }
        for ( j in 0 ..3){
            println("j===$j")
        }
        for(i in 0 until 2){
            println("until i ==$i")
        }
        for(i in 4 ..1){
            println("nothing $i")
        }

        for(i in 4 downTo  2){
            println("down to i==$i")
        }

        for(i in 4 until  2 ){
            println("nothing ...$i")
        }
        for(i in 1 ..10 step 5){
            println("step  i=$i")
        }
    }

    fun baseTest(){
        val items = listOf("apple", "banana", "kiwifruit")
        for (index in items.indices) {
            println("item at $index is ${items[index]}")
        }
    }
}