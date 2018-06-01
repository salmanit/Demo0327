package com.charliesong.demo0327.classify

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.fragment_classify_small.*
import java.util.*

/**
 * Created by charlie.song on 2018/5/16.
 */
class FragmentClassifySmall:DialogFragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_classify_small,container,false)
    }
    var url= arrayListOf<String>()
    var book= arrayListOf<String>()
    var itemWidth=0;
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_app.apply {
            layoutManager=GridLayoutManager(activity,5)
            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                    outRect.apply {
                        this.right=10
                        this.left=10
                        this.bottom=50
                        val position=parent.getChildViewHolder(view).adapterPosition
                        if(position/5==0){
                            this.top=60
                        }
                    }
                }
            })
            adapter= object : BaseRvAdapter<String>(url) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_classify2
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setImageUrl(R.id.iv_cover,url[position])
                    holder.setText(R.id.tv_book_name,book[position])
                    holder.itemView.setBackgroundColor( Color.RED )
//                    if(itemWidth==0){
//                        itemWidth=(rv_app.measuredWidth/3)-100
//                    }
//                    var params=holder.itemView.layoutParams
//                    params.width=itemWidth

                }

            }
        }

        ItemTouchHelper(object :ItemTouchHelper.Callback(){
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                val dragTag=ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragTag,0)
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return true
            }

            override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, fromPos: Int, target: RecyclerView.ViewHolder?, toPos: Int, x: Int, y: Int) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                Collections.swap(url,fromPos,toPos)
                Collections.swap(book,fromPos,toPos)
                recyclerView.adapter.notifyItemMoved(fromPos,toPos)
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

            }
        }).attachToRecyclerView(rv_app)
    }



}