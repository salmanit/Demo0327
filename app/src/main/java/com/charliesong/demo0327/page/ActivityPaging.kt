package com.charliesong.demo0327.page

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.paging.*
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
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
        makePageList()
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
            adapter=MyPagingAdapter(callback).apply {
//                submitList(mPagedList)
            }
//            addOnScrollListener(object :RecyclerView.OnScrollListener(){
//                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    var lastPos = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition();
//                    mPagedList.loadAround(lastPos);//触发Android Paging的加载事务逻辑。
//
//                }
//            })

        }

    }

    lateinit var mPagedList:PagedList<Student>

    private fun getAdapter():MyPagingAdapter{
        return  rv_paging.adapter as MyPagingAdapter
    }
    private fun makePageList() {
        val mPagedListConfig = PagedList.Config.Builder()
                .setPageSize(6) //分页数据的数量。在后面的DataSource之loadRange中，count即为每次加载的这个设定值。
                .setPrefetchDistance(6) //初始化时候，预取数据数量。
                .setInitialLoadSizeHint(12)
                .setEnablePlaceholders(false)
                .build()
        mPagedList = PagedList.Builder(MyDataSource(),mPagedListConfig)
                .setNotifyExecutor {
                println("setNotifyExecutor=============1=====${Thread.currentThread().name}")
                }
                .setFetchExecutor {
                    println("setFetchExecutor=========1=========${Thread.currentThread().name}")
                }
                .build()

        val data = LivePagedListBuilder(MyDataSourceFactory(), PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .build()).build()

        data.observe(this, Observer {
            println("98==================observer====${it?.size}")
            getAdapter().submitList(it)
        })
    }

    inner class MyDataSourceFactory:DataSource.Factory<Int,Student>(){
        override fun create(): DataSource<Int, Student> {
            return  MyDataSource()
        }
    }
    class aaa:PageKeyedDataSource<Int,Student>(){
        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Student>) {

            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Student>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class bbb:ItemKeyedDataSource<Student,Student>(){
        override fun loadInitial(params: LoadInitialParams<Student>, callback: LoadInitialCallback<Student>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        }

        override fun loadAfter(params: LoadParams<Student>, callback: LoadCallback<Student>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun loadBefore(params: LoadParams<Student>, callback: LoadCallback<Student>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getKey(item: Student): Student {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    inner  class MyDataSource: PositionalDataSource<Student>(){
        private fun computeCount(): Int {
            // actual count code here
            return 3000
        }

        private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Student> {
            // actual load code here

            var list= arrayListOf<Student>()
            if(startPosition<50)
            repeat(loadCount){
                list.add(Student(startPosition+it+1,"stu ${startPosition+it+1}"))
            }
            return list
        }

        override fun loadInitial(@NonNull params: PositionalDataSource.LoadInitialParams,
                        @NonNull callback: PositionalDataSource.LoadInitialCallback<Student>) {

            val totalCount = computeCount()
            val position = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
            val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
            println("79=====loadInitial $position==$loadSize===${Thread.currentThread().name}")
            callback.onResult(loadRangeInternal(position, loadSize), position, totalCount)
        }

        override fun loadRange(@NonNull params: PositionalDataSource.LoadRangeParams,
                      @NonNull callback: PositionalDataSource.LoadRangeCallback<Student>) {
            println("99=====load range  ${params.startPosition}==${params.loadSize}===${Thread.currentThread().name}")
            loadRangeInternal(params.startPosition, params.loadSize).apply {
                if(this.size>0){
                    callback.onResult(this)
                }
                println("size===========${this.size}")
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