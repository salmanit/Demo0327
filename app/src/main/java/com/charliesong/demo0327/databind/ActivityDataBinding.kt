package com.charliesong.demo0327.databind

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.charliesong.demo0327.BR
import com.charliesong.demo0327.R
import com.charliesong.demo0327.app.BaseFunction
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.bean.TreeBean
import com.charliesong.demo0327.databinding.ActivityDataBindingBinding
import com.charliesong.demo0327.databinding.ItemTreeBinding
import com.charliesong.demo0327.app.MyAPIManager
import kotlinx.android.synthetic.main.activity_data_binding.*
import kotlinx.android.synthetic.main.item_contact.*

class ActivityDataBinding : BaseActivity() {

    data class LoginParams(var phone:String?=null,var psw:String?=null)

    var loginParams=LoginParams()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       DataBindingUtil.setContentView<ActivityDataBindingBinding>(this,R.layout.activity_data_binding).apply {

           cbAgree.setOnCheckedChangeListener { buttonView, isChecked ->
               state=isChecked
           }
           cbAgree.isChecked=true;
           this.buttonText="submit"
           params=loginParams
       }
        btn_go.setOnClickListener {
            showToast("${loginParams}")
        }
        tv_name.setText("jerry")
        tv_phone.setText("10086")
//        setContentView(R.layout.activity_data_binding)
//        DataBindingUtil.bind<ActivityDataBindingBinding>(findViewById(R.id.cstlayout))?.apply {
//            cbAgree.isChecked=true;
//        }
//
//        ActivityDataBindingBinding.bind(findViewById(R.id.cstlayout))

        defaultSetTitle("databinding")
        MyAPIManager.getAPI().getAllTreeChildren()
                .compose(BaseFunction.handle())
                .subscribe({
                    initRvTree(it)
                }, {
                    println("failed=============${it}")
                    it.printStackTrace()
                })
    }

    fun initRvTree(list: List<TreeBean>) {
        rv_tree.apply {
            layoutManager = GridLayoutManager(this@ActivityDataBinding, 4)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    outRect.apply {
                        right = 10
                        bottom = 10
                    }
                }
            })
            println("lists count================${list.size}")
            adapter = object : BaseRvAdapter<TreeBean>(ArrayList<TreeBean>().apply {
                addAll(list)
            }) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_tree
                }

//                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {
//                   var bind= DataBindingUtil.inflate<ItemTreeBinding>(LayoutInflater.from(parent.context), R.layout.item_tree, parent, false);
//                    return BaseRvHolder(bind.root)
//                }
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
//                    DataBindingUtil.getBinding<ItemTreeBinding>(holder.itemView)?.apply {
//                        this.tree=getItemData(position)
//                        this.index="$position";
//                    }
//                println("$position===================${holder.itemView.tag}")
                    ItemTreeBinding.bind(holder.itemView).apply {
                        when(position%2){
                            0->{
                                this.tree=getItemData(position)
                                this.index="$position"
                            }
                            1->{
                                this.setVariable(BR.tree,getItemData(position))
                                this.setVariable(BR.index,"$position")
                            }
                            2->{
                                this.tvTree.setText(getItemData(position).name)
                                this.tvIndex.setText("$position")
                                tvIndex.setBackgroundColor(Color.RED)
                            }
                        }

                    }
                }
            }
        }
    }


}
