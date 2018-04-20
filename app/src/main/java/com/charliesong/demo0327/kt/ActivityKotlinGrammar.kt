package com.charliesong.demo0327.kt

import android.os.Bundle
import com.charliesong.demo0327.BaseActivity
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