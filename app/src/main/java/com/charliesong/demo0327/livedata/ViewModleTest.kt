package com.charliesong.demo0327.livedata

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.CountDownLatch


class ViewModleTest:ViewModel(){

     private  var mCurrentName: MutableLiveData<String>?=null

     private  var mNameListData: MutableLiveData<ArrayList<String>>?=null
    fun getName():MutableLiveData<String>{
        if(mCurrentName==null){
            mCurrentName= MutableLiveData()
        }
        return mCurrentName!!
    }

    fun getNameListData(): MutableLiveData<ArrayList<String>>{
        if(mNameListData==null){
            mNameListData=MutableLiveData()
        }
        return  mNameListData!!
    }


}
