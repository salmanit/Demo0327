package com.charliesong.demo0327.page

import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
/**ItemKeyedDataSource
 * 这个默认在初始化记载了默认的比如10条数据以后，它会先执行loadBefore方法加载初始化的首条数据之前的数据【如果不需要，需要自己在方法里处理】，完事会执行
 * loadAfter加载初始化的最后一条数据之后的数据
 * 如果接口分页使用的最后一条数据的id之类的那么用这个比较合适
 * */
class FragmentItemKeyedDS:FragmentPageBase(){
    override fun getDataSource(): DataSource<Int, Student> {
        return  object :ItemKeyedDataSource<Int,Student>(){
            override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Student>) {
                println("FragmentItemKeyedDS loadInitial size =====: ${params.requestedLoadSize} ")
                getDataBackgroundAfter(0,params.requestedLoadSize)?.apply {
                    callback.onResult(this,0,this.size)
                }
            }
             //刚开始是在loadInitial>>  loadBefore>>然后就是这个了，之后手指上滑的时候执行这个
            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Student>) {
                println("FragmentItemKeyedDS loadAfter size =======: ${params.requestedLoadSize}  page:${params.key}")
                getDataBackgroundAfter(params.key,params.requestedLoadSize)?.let {
                    callback.onResult(it)
                }
            }
            //这个在loadInitial加载完pagesize的数据以后，会先调用这个加载第一条数据之前的数据
            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Student>) {
                println("FragmentItemKeyedDS loadBefore size====: ${params.requestedLoadSize}  page:${params.key}")
                getDataBackgroundBefore(params.key,params.requestedLoadSize)?.let {
                    callback.onResult(it)
                }
            }
            //这里返回的key就是上边方法里LoadParams的key
            override fun getKey(item: Student): Int {
               println("FragmentItemKeyedDS=================getKey=======${item}")
                return item.id
            }
        }
    }
    fun getDataBackgroundAfter(startPosition:Int,size :Int): List<Student>?{
//        println("FragmentItemKeyedDS  getData=====================${Thread.currentThread().name}")
        var lists= arrayListOf<Student>()
        if(startPosition>50){
            return null
        }
        repeat(size){
            lists.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
        }
        return lists
    }
    fun getDataBackgroundBefore(startPosition:Int,size :Int): List<Student>?{
//        println("FragmentItemKeyedDS  getData=====================${Thread.currentThread().name}")
        var lists= arrayListOf<Student>()
        if(startPosition<-50){//我们初始化的数据就是1到8，第一条的key就是1，这里我不需要在1之前还有数据，所以就return了，根据实际情况来处理
            return null
        }
        repeat(size){
            lists.add(0,Student(startPosition-it-1,"stu ${startPosition-it-1}"))
        }
        return lists
    }
}