package com.charliesong.demo0327.words

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.TextView
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.R
import kotlinx.android.synthetic.main.activity_words.*
import java.util.*

/**
 * Created by charlie.song on 2018/4/28.
 */
class ActivityWords : BaseActivity() {
    var rightPosition = 3;//正确答案的位置，假设是这个。
    var choice = -1;//这是松开手指的时候最终选择的位置索引
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)
        defaultSetTitle("填空題")

        var wordsOriginal = arrayListOf<WordBean>()
        wordsOriginal.add(WordBean("He"))
        wordsOriginal.add(WordBean("must"))
        wordsOriginal.add(WordBean("been"))
        wordsOriginal.add(WordBean("single"))
        wordsOriginal.add(WordBean("activity"))
        wordsOriginal.add(WordBean("fragment"))
        wordsOriginal.add(WordBean("honey"))
        wordsOriginal.add(WordBean("restaurant"))
        rv_words.apply {
            layoutManager = WordsLayoutManager()
            addItemDecoration(ItemDecorationSpace())
            adapter = object : BaseRvAdapter<WordBean>(wordsOriginal) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_word
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.itemView.layoutParams = RecyclerView.LayoutParams(-2, -2)
                    var bean = getItemData(position)
                    holder.getView<TextView>(R.id.tv_word).apply {
                        text = bean.word
                        visibility = if (bean.inserted) View.INVISIBLE else View.VISIBLE
                        if (choice == position) {//choice是我们最终选择的位置，这里处理下颜色，对的话为绿色，错的话为红色，
                            visibility = View.VISIBLE
                            setBackgroundColor(if (rightPosition == position) Color.GREEN else Color.RED)
                        }
                    }
                }
            }
        }

        var helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            var temp = -1
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

            }

            //这里用来判断处理哪些情况
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                var position = viewHolder.adapterPosition
                if (position != 0 || wordsOriginal[position].inserted) {//
                    return 0   //只有第一个可以拖动，另外拖动过之后也不能再次拖动了，其实第一个也就隐藏了
                }
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END  //这个是拖动的flag
                return makeMovementFlags(dragFlags, 0)
            }

            //拖拽到其他item附近的时候会不停的回掉这个方法，我们在这里做的就是交换对应的数据
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                var oldPositioon = viewHolder.adapterPosition
                var newPosition = target.adapterPosition
                if (temp < 0) {//首次拖动到有效范围的时候temp肯定是-1拉，这时候我们在拖动的位置添加一条选项数据，也就是第一条数据。
                    wordsOriginal.add(newPosition, wordsOriginal.get(0).apply { inserted = true })//为啥inserted=true，是为了使这个item不可见，只用来占位
                    recyclerView.adapter.notifyItemInserted(newPosition)
                }
                if (temp == newPosition) {//在同一个位置来回晃悠，啥也不干
                    return false
                }
                if (temp > 0) {//这个temp其实就是我们添加的那条数据，用他来代替第一条数据来移动
                    Collections.swap(wordsOriginal, temp, newPosition)
                    recyclerView.adapter.notifyItemMoved(temp, newPosition)
                }
                temp = newPosition
                return true
            }

            //使用kotlin的时候得注意，这个viewHolder是可能为空的，当state为Idle的时候
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                viewHolder?.run {
                    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                        itemView.setBackgroundColor(Color.RED); //正在拖动的item弄成红色
                    }
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(0);//正在拖动的item颜色还原
            }

            //手指拖动的时候isCurrentlyActive是true，松开手指的时候成为false，可以在false的时候判断下当前item的位置是否在有效位置
            override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                //我们只判断y的范围是否在有效范围内，temp大于0表示曾经移动到有效范围内
                if (temp > 0 && !isCurrentlyActive) {
                    val firstChild = recyclerView.getChildAt(0)
                    val secondTop = recyclerView.getChildAt(1).top
                    val lastBottom = recyclerView.getChildAt(recyclerView.childCount - 1).bottom
                    if (firstChild.bottom + dY < secondTop || firstChild.top + dY > lastBottom) {
                        //有效范围之外松手，那么还原数据，不做判断
                        wordsOriginal.removeAt(temp)
                        recyclerView.adapter.notifyItemRemoved(temp)
                        wordsOriginal[0].inserted = false;
                        recyclerView.adapter.notifyItemChanged(0)
                    } else {
                        choice = temp
                        recyclerView.adapter.notifyDataSetChanged()
                        //最终的choice和正确的rightPosition比较下，对错之后要感谢啥，自己处理
                        showToast("选择${if (choice == rightPosition) "正确" else "错误"}")
                    }
                    temp = -1
                }
            }

        })
        helper.attachToRecyclerView(rv_words)

    }

}