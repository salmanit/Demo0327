package com.charliesong.demo0327.contact

import android.text.TextUtils

/**
 * Created by charlie.song on 2018/4/3.
 */
data class Contact(var head :String="https://upload.jianshu.io/users/upload_avatars/5275362/63c0bb2d-b849-4909-9136-b5aae8b01dcc.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
                   ,var phone:String,var name:String,var index:String="#",var py:String="#")
{
    init {
      index=PinyinUtils.getPinYinHeadChar(name).toUpperCase()
        py=PinyinUtils.getPingYin(name)
        if(TextUtils.isEmpty(index)){
            index="#"
        }
    }
}
