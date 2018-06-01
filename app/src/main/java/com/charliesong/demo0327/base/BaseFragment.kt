package com.charliesong.demo0327.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by charlie.song on 2018/4/26.
 */
abstract class BaseFragment:Fragment(){

   abstract fun getLayoutID():Int
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutID(),container,false)
    }
}