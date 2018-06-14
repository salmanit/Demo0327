package com.charliesong.demo0327.page

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.*
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_paging.*

class ActivityPaging:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        defaultSetTitle("page")

        rv_paging.apply {
            layoutManager=LinearLayoutManager(this@ActivityPaging)
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                var paint=Paint()
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom=3
                }

                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                    super.onDraw(c, parent, state)
                    paint.color=Color.LTGRAY
                    var childCount=parent.childCount
                    repeat(childCount){
                        var child=parent.getChildAt(it)
                        if(child!=null){
                            c.drawRect(parent.paddingLeft.toFloat(),child.bottom.toFloat(),parent.width.toFloat()-parent.paddingRight,child.bottom+3f,paint)
                        }
                    }
                }
            })
            adapter=MyPagingAdapter(callback)
//            addOnScrollListener(object :RecyclerView.OnScrollListener(){
//                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    var last=adapter.itemCount
//                    mPagedList.loadAround(last)
//
//                }
//            })

        }
        makePageList()


    }

    lateinit var mPagedList:PagedList<Student>

    private fun getAdapter():MyPagingAdapter{
        return  rv_paging.adapter as MyPagingAdapter
    }
    private fun makePageList() {
        val mPagedListConfig = PagedList.Config.Builder()
                .setPageSize(10) //分页数据的数量。在后面的DataSource之loadRange中，count即为每次加载的这个设定值。
                .setPrefetchDistance(10) //初始化时候，预取数据数量。
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()
        mPagedList = PagedList.Builder(MyDataSource().apply {
            addInvalidatedCallback {
                println("80--------------=================invalidated")
            }
        },mPagedListConfig)
                .setNotifyExecutor {
                println("setNotifyExecutor=============1=====${Thread.currentThread().name}")
                }
                .setFetchExecutor {
                    println("setFetchExecutor=========1=========${Thread.currentThread().name}")
                }
                .build()
        mPagedList.addWeakCallback(null,object :PagedList.Callback(){
            override fun onChanged(position: Int, count: Int) {

            }

            override fun onInserted(position: Int, count: Int) {

            }

            override fun onRemoved(position: Int, count: Int) {

            }
        })
        getAdapter().submitList(mPagedList)
//     var liveData=   LivePagedListBuilder(object :DataSource.Factory<Int,Student>(){
//            override fun create(): DataSource<Int, Student> {
//                return MyDataSource()
//            }
//        }, mPagedListConfig).build()
//              liveData  .observe(this, Observer {
//            println("base 34==================observer====${it?.size}")
//            getAdapter().submitList(it)
//        })
    }

inner  class MyDataSource2:PositionalDataSource<Student>(){

     fun loadRange(startPosition: Int, count: Int): List<Student>?{
         if(startPosition>50){
             return null
         }
         var list= arrayListOf<Student>()

         repeat(count){
             list.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
         }
         return list
     }



    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Student>) {
        val list = loadRange(params.startPosition, params.loadSize)
        if (list != null) {
            callback.onResult(list)
        } else {
            invalidate()
        }
    }

     fun countItems(): Int{
         return 1110
     }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Student>) {
        val totalCount = countItems()
        if (totalCount == 0) {
            callback.onResult(emptyList<Student>(), 0, 0)
            return
        }

        // bound the size requested, based on known count
        val firstLoadPosition = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
        val firstLoadSize = PositionalDataSource.computeInitialLoadSize(params, firstLoadPosition, totalCount)

        // convert from legacy behavior
        val list = loadRange(firstLoadPosition, firstLoadSize)
        if (list != null && list.size == firstLoadSize) {
            callback.onResult(list, firstLoadPosition, totalCount)
        } else {
            // null list, or size doesn't match request
            // The size check is a WAR for Room 1.0, subsequent versions do the check in Room
            invalidate()
        }
    }


}
    inner  class MyDataSource: PositionalDataSource<Student>(){
        private fun computeCount(): Int {
            // actual count code here
            return 3000
        }

        private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Student>? {
            // actual load code here
            if(startPosition>50){
                return null
            }
            var list= arrayListOf<Student>()

            repeat(loadCount){
                list.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
            }
            return list
        }

        override fun loadInitial(@NonNull params: PositionalDataSource.LoadInitialParams,
                        @NonNull callback: PositionalDataSource.LoadInitialCallback<Student>) {
            //加载数据这里可以自己根据实际需求来处理
            val totalCount = computeCount()
            val position = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
            val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
            println("122=====loadInitial $position==$loadSize===${Thread.currentThread().name}")
            loadRangeInternal(position, loadSize)?.apply {
                callback.onResult(this, position,totalCount)

            }

        }

        override fun loadRange(@NonNull params: PositionalDataSource.LoadRangeParams,
                      @NonNull callback: PositionalDataSource.LoadRangeCallback<Student>) {
            //加载数据这里可以自己根据实际需求来处理
            println("132=====load range  ${params.startPosition}==${params.loadSize}===${Thread.currentThread().name}")
            loadRangeInternal(params.startPosition, params.loadSize)?.apply {
                callback.onResult(this)
            }

        }

    }

    open inner  class  MyPagingAdapter :PagedListAdapter<Student,BaseRvHolder>{
        constructor(diffCallback: DiffUtil.ItemCallback<Student>) : super(diffCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {

            return BaseRvHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paging,parent,false))
        }

        override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
           getItem(position)?.apply {
                holder.setText(R.id.tv_name,name)
                holder.setText(R.id.tv_age,"${age}")
            }
            println("onBindViewHolder=============$position//${itemCount} ===${getItem(position)}")
        }

    }

    val callback=object :DiffUtil.ItemCallback<Student>(){
        override fun areItemsTheSame(oldItem: Student?, newItem: Student?): Boolean {
            return  oldItem?.id==newItem?.id
        }

        override fun areContentsTheSame(oldItem: Student?, newItem: Student?): Boolean {
            return  oldItem?.age==newItem?.age
        }
    }
}