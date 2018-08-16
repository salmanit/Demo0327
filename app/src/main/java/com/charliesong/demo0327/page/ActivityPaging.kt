package com.charliesong.demo0327.page

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.*
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class ActivityPaging : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        defaultSetTitle("page")

        //recyclerview 设置adapter以及分割线
        rv_paging.apply {
            layoutManager = LinearLayoutManager(this@ActivityPaging)
            //弄条分割线
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                var paint = Paint()
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom = 3
                }

                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                    super.onDraw(c, parent, state)
                    paint.color = Color.LTGRAY
                    var childCount = parent.childCount
                    repeat(childCount) {
                        var child = parent.getChildAt(it)
                        if (child != null) {
                            c.drawRect(parent.paddingLeft.toFloat(), child.bottom.toFloat(), parent.width.toFloat() - parent.paddingRight, child.bottom + 3f, paint)
                        }
                    }
                }
            })
            adapter = MyPagingAdapter(callback)
        }
        //创建pageList
        makePageList()

    }

    private fun getAdapter(): MyPagingAdapter {
        return rv_paging.adapter as MyPagingAdapter
    }

    private fun makePageList() {
        val mPagedListConfig = PagedList.Config.Builder()
                .setPageSize(10) //分页数据的数量。在后面的DataSource之loadRange中，count即为每次加载的这个设定值。
                .setPrefetchDistance(5)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()

        //不建议这种，因为还得自己处理Executor,建议使用下边注释的代码
        var mPagedList = PagedList.Builder(MyDataSource(), mPagedListConfig)
                .setNotifyExecutor {
                    println("setNotifyExecutor=============1=====${Thread.currentThread().name}")//进来是非主线程pool-7-thread-1，因为这个是更新ui的，所以现编切换到主线程
                    Handler(Looper.getMainLooper()).post(it)//弄个全局变量，不要每次都new一个，我这里就方便测试
                }
                .setFetchExecutor {
                    println("setFetchExecutor=========1=========${Thread.currentThread().name}") //这里进来的是main线程，因为你要加载数据，所以切换线程
                    Executors.newFixedThreadPool(2).execute(it) //这里不应该每次都new一个，应该写个全局变量
                }
                .setBoundaryCallback(object :PagedList.BoundaryCallback<Student>(){
                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                        println("=====================onZeroItemsLoaded")
                    }

                    override fun onItemAtEndLoaded(itemAtEnd: Student) {
                        super.onItemAtEndLoaded(itemAtEnd)
                        println("=================onItemAtEndLoaded====${itemAtEnd}")
                    }

                    override fun onItemAtFrontLoaded(itemAtFront: Student) {
                        super.onItemAtFrontLoaded(itemAtFront)
                        println("================onItemAtFrontLoaded=====${itemAtFront}")
                    }
                })
                .setInitialKey(initKey)//不设置的话默认就是0
                .build()

        getAdapter().submitList(mPagedList)

        //如果懒得自己处理setNotifyExecutor和setFetchExecutor，建议用下边的，系统都有默认值，省事
//         LivePagedListBuilder(object : DataSource.Factory<Int, Student>() {
//            override fun anchor(): DataSource<Int, Student> {
//                return MyDataSource() //DataSource有3种，这里就简单随便写了个，自己看需求写
//            }
//        }, mPagedListConfig)
//                .build()
//                .observe(this, Observer {
//                    getAdapter().submitList(it)
//                })
    }
    var initKey=0;//初始从哪个位置开始加载数据，这里测试从第20条开始，这样下拉可以看到前20条数据，上拉可以看到20之后的数据
    inner class MyDataSource : PositionalDataSource<Student>() {

        private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Student>? {
            // actual load code here
//            if (startPosition > 40) { //模拟数据加载完的情况
//                return null
//            }
            var list = arrayListOf<Student>()
            if(startPosition<50)
            repeat(loadCount) {
                list.add(Student(startPosition + it + 1, "stu ${startPosition + it + 1}"))
            }
            return list
        }

        override fun loadInitial(@NonNull params: PositionalDataSource.LoadInitialParams,
                                 @NonNull callback: PositionalDataSource.LoadInitialCallback<Student>) {
            //加载数据这里可以自己根据实际需求来处理params.requestedStartPosition就是我们设置的initKey=20
            loadRangeInternal(params.requestedStartPosition, params.requestedLoadSize)?.apply {
                callback.onResult(this, params.requestedStartPosition)
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

    //adapter和我们以前的没太大区别，就是构造方法里需要传一个参数，用来判断是否是相同数据而已
    open inner class MyPagingAdapter : PagedListAdapter<Student, BaseRvHolder> {
        constructor(diffCallback: DiffUtil.ItemCallback<Student>) : super(diffCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {

            return BaseRvHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paging, parent, false))
        }

        override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
            getItem(position)?.apply {
                holder.setText(R.id.tv_name, name)
                holder.setText(R.id.tv_age, "${age}")
            }
//            println("onBindViewHolder=============$position//${itemCount} ===${getItem(position)}")
        }

    }

    val callback = object : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student?, newItem: Student?): Boolean {
            return oldItem?.id == newItem?.id
        }

        override fun areContentsTheSame(oldItem: Student?, newItem: Student?): Boolean {
            return oldItem?.age == newItem?.age
        }
    }
}