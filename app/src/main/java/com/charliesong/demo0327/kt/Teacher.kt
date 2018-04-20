package com.charliesong.demo0327.kt

/**
 * Created by charlie.song on 2018/4/2.
 */
data class Teacher(var name:String,var age:Int,var cla:String){

    init {
        println("$name  $age $cla")
    }
    constructor( name: String):this(name,23,"dot")//secondary constructor 不能使用var或者val修饰

}