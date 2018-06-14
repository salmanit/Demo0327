package com.charliesong.demo0327.page

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import android.util.Size
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
/**
 * PageKeyedDataSource分析说明，它的数据只会加载一次，来回滑动的话不会再次加载。
 * 我们平时如果分页用的是pageNum和pageSize的话用这个比较合适
 * */
class FragmentPageKeyedDS:FragmentPageBase(){
    override fun getDataSource(): DataSource<Int, Student> {
        return object :PageKeyedDataSource<Int,Student>(){
            override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Student>) {
                println("loadInitial size ===: ${params.requestedLoadSize} ")
                getDataBackground(5,params.requestedLoadSize).apply {
                    callback.onResult(this,4,6)
                    //这里的previousPageKey，和nextPageKey决定了前后是否有数据，如果你传个null，那么就表示前边或者手边没有数据了。也就是下边的loadBefore或者LoadAfter不会执行了
                }
            }
            //往上滑动会不停的调用这个方法，往回滑动的时候不调用任何方法
            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {
                println("loadAfter size =======: ${params.requestedLoadSize}  page:${params.key}")
                getDataBackground(params.key,params.requestedLoadSize).let {
                    callback.onResult(it,params.key+1)
                }
            }

            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {
                println("loadBefore size====: ${params.requestedLoadSize}  page:${params.key}")
                if(params.key<0){
                    return
                }
                getDataBackground(params.key,params.requestedLoadSize).let {
                    callback.onResult(it,params.key-1)
                }
            }
        }
    }
    fun getDataBackground(page:Int,size :Int): List<Student>{
        println("FragmentPageKeyedDS  getData=====================${Thread.currentThread().name}") //打印的结果是2个线程来回切换pool-4-thread-1，pool-4-thread-2
        var lists= arrayListOf<Student>()
        var startPosition=page*size
        repeat(size){
            lists.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
        }
        return lists
    }
}