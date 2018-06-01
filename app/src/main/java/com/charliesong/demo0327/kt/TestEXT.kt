package com.charliesong.demo0327.kt

import android.app.Activity
import android.widget.Toast

/**
 * Created by charlie.song on 2018/5/29.
 */

//class TestEXT{

    fun  Activity.testShowToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
//}