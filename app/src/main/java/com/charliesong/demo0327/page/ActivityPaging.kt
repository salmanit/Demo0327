package com.charliesong.demo0327.page

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.arch.paging.PositionalDataSource
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
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

            adapter=MyPagingAdapter(callback).apply {
                submitList(mPagedList)
            }
            addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    var lastPos = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition();

//                    mPagedList.loadAround(lastPos);//触发Android Paging的加载事务逻辑。
                }
            })
        }

    }

    var datas= arrayListOf<Student>()
    lateinit var mPagedList:PagedList<Student>

    private fun makePageList() {
        val mPagedListConfig = PagedList.Config.Builder()
                .setPageSize(3) //分页数据的数量。在后面的DataSource之loadRange中，count即为每次加载的这个设定值。
                .setPrefetchDistance(5) //初始化时候，预取数据数量。
                .setEnablePlaceholders(false)
                .build()

        mPagedList = PagedList.Builder(MyDataSource(),20)
                .setNotifyExecutor {
                println("setNotifyExecutor==================")
                }
                .setFetchExecutor {
                    println("setFetchExecutor==================")
                }
                .build()
    }


    inner  class MyDataSource: PositionalDataSource<Student>(){
        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Student>) {
            println("loadRange===========${params.startPosition}===${params.loadSize}")
            simulateLoadData(params.startPosition,params.loadSize)
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Student>) {

            println("loadInitial===========${params.pageSize}===${params.placeholdersEnabled}===${params.requestedLoadSize}==${params.requestedStartPosition}")
            simulateLoadData(params.requestedStartPosition,params.pageSize)
        }

    }

    open  fun simulateLoadData(startPosition:Int,pageSize:Int){
        repeat(pageSize){
            datas.add(Student(startPosition+it,"stu ${startPosition+it}"))
        }
    }

    open  class  MyPagingAdapter :PagedListAdapter<Student,BaseRvHolder>{
        constructor(diffCallback: DiffUtil.ItemCallback<Student>) : super(diffCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {

            return BaseRvHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paging,parent,false))
        }

        override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
           getItem(position)?.apply {
                holder.setText(R.id.tv_name,name)
                holder.setText(R.id.tv_age,"${age}")
            }

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