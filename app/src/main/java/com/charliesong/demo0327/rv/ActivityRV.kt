package com.charliesong.demo0327.rv

import android.os.Bundle
import android.os.Looper
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.AsyncListUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.activity_rv.*
import java.util.*
import kotlin.concurrent.thread

class ActivityRV : BaseActivity() {
    var data= arrayListOf<StudentTest>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)
        defaultSetTitle("rv")

        rv_test.apply {
            layoutManager=LinearLayoutManager(this@ActivityRV)
            addItemDecoration(ItemDecorationSpace())
            adapter=MyAdapter(itemCallback)
        }

        rv_test2.apply {
            layoutManager=LinearLayoutManager(this@ActivityRV)
            addItemDecoration(ItemDecorationSpace())
            adapter=adapterNormal
        }


        btn_refresh.setOnClickListener {
//            data= arrayListOf<StudentTest>()
//            repeat(20){
//                data.add(StudentTest(data.size,"jerry ${it}",Random().nextInt(10)+20))
//            }
//            getMyAdapter().submitList(data)

            utils.refresh()
        }

        btn_load.setOnClickListener {
            val list= arrayListOf<StudentTest>()
            list.addAll(data)
            repeat(20){
                list.add(StudentTest(list.size,"jerry ${data.size+it}",Random().nextInt(10)+20))
            }
            getMyAdapter().submitList(list)
        }
        btn_add.setOnClickListener {
            val list= arrayListOf<StudentTest>()
            list.addAll(data)
            if(data.size>2)
            list.add(2,StudentTest(data.size,"jerry ${data.size}",Random().nextInt(10)+20))

            getMyAdapter().submitList(list)
        }
        btn_remove.setOnClickListener {
//            val list= arrayListOf<StudentTest>()
//            list.addAll(data)
//            if(data.size>3)
//            list.removeAt(3)
//
//            getMyAdapter().submitList(list)

            utils.onRangeChanged()
        }


    }

    val utils=  AsyncListUtil(StudentTest::class.java,22,object :AsyncListUtil.DataCallback<StudentTest>(){
        override fun refreshData(): Int {
            println("refeshData===========")
            repeat(Random().nextInt(10)+11){
                data.add(StudentTest(data.size,"jerry ${it}",Random().nextInt(10)+20))
            }
            return data.size
        }

        override fun fillData(data: Array<out StudentTest>?, startPosition: Int, itemCount: Int) {
            println("${data?.size}=================start=$startPosition itemcount=$itemCount")

        }
    },
            object :AsyncListUtil.ViewCallback(){
                override fun onDataRefresh() {
                    println("onDataRefresh===============")
                    adapterNormal.notifyDataSetChanged()
                }

                override fun getItemRangeInto(outRange: IntArray) {
                    println("getItemRangeInto==============${Arrays.toString(outRange)}")
                    outRange[0]=4
                    outRange[1]=11
                }

                override fun onItemLoaded(position: Int) {
                    println("onItemLoaded=======================$position")

                }
            })

    private val adapterNormal=object :BaseRvAdapter<StudentTest>(data){
        override fun getLayoutID(viewType: Int): Int {
            return R.layout.item_rv_test
        }

        override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
            val student = getItemData(position)
            holder.setText(R.id.tv_name, student.name)
                    .setText(R.id.tv_age, "age: ${student.age}")
                    .setText(R.id.tv_id, "ID: ${student.id}")
        }
    }







    private fun getMyAdapter():MyAdapter{
        return  rv_test.adapter as MyAdapter
    }
    val itemCallback=object :DiffUtil.ItemCallback<StudentTest>(){
        override fun areItemsTheSame(oldItem: StudentTest, newItem: StudentTest): Boolean {
            return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: StudentTest, newItem: StudentTest): Boolean {
            return  oldItem.age==newItem.age && TextUtils.equals(oldItem.name,newItem.name)
        }
    }

    data class StudentTest(var id: Int, var name: String, var age: Int)
    class MyAdapter(diffCallback: DiffUtil.ItemCallback<StudentTest>) : ListAdapter<StudentTest, BaseRvHolder>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {
            return BaseRvHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_test, parent, false))
        }

        override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
            val student = getItem(position)
            holder.setText(R.id.tv_name, student.name)
                    .setText(R.id.tv_age, "age: ${student.age}")
                    .setText(R.id.tv_id, "ID: ${student.id}")
        }
    }
}
