package com.charliesong.demo0327.page

import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource

class FragmentPositionalDS:FragmentPageBase(){
    override fun getDataSource(): DataSource<Int, Student> {
        return  object :PositionalDataSource<Student>(){
            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Student>) {
                println("FragmentPositionalDS=====load range  ${params.startPosition}==${params.loadSize}===${Thread.currentThread().name}")

                getDataBackground(params.startPosition,params.loadSize)?.apply {
                    callback.onResult(this)
                }
            }

            override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Student>) {
                println("FragmentPositionalDS=====loadInitial ${params.requestedStartPosition}===${params.requestedLoadSize}==${params.pageSize}==${Thread.currentThread().name}")
                getDataBackground(20,params.pageSize)?.apply {
                    callback.onResult(this,20)
                    /**第二个参数0表示的是首次加载的位置，可以是任何正整数**/
                }
            }
        }.apply {

        }
    }
    fun getDataBackground(startPosition:Int,size :Int): List<Student>?{
//        println("FragmentPositionalDS  getData=====================${Thread.currentThread().name}")
        var lists= arrayListOf<Student>()
        if(startPosition>90){
            return null
        }
        repeat(size){
            lists.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
        }
        return lists
    }
}