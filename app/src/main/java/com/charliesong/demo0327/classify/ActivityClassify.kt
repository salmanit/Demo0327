package com.charliesong.demo0327.classify

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.words.ItemDecorationSpace
import kotlinx.android.synthetic.main.activity_classify.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker
import android.view.Gravity
import android.widget.TextView
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver


/**
 * Created by charlie.song on 2018/5/15.
 */
class ActivityClassify : BaseActivity() {
    var urls = arrayListOf<String>()
    var books = arrayListOf<String>()

    var datas= arrayListOf<String>()
    var checkePosition = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify)
        defaultSetTitle("my book")
        initData()
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(3)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver { Gravity.CENTER }
                //you are able to break row due to your conditions. Row breaker should return true for that views
                .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build()
        rv_flow.apply {
            layoutManager = chipsLayoutManager

            adapter = object : BaseRvAdapter<String>(datas) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_text
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_content,getItemData(position)+position)

                }
            }
        }

        rv_book.apply {
            layoutManager = GridLayoutManager(this@ActivityClassify, 3)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    outRect.apply {
                        this.right = 50
                        this.bottom = 50
                        this.left = 50
                    }
                }
            })

            adapter = object : BaseRvAdapter<String>(urls) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_classify
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setImageUrl(R.id.iv_cover, urls[position])
                    holder.setText(R.id.tv_book_name, books[position])
                    holder.itemView.setBackgroundColor(if (checkePosition == position) Color.RED else Color.BLUE)
                    holder.itemView.setOnClickListener {
                        FragmentClassifySmall()
                                .apply {
                                    this.url.clear()
                                    this.url.addAll(urls.subList(3, 9))
                                    this.book.clear()
                                    this.book.addAll(books.subList(3, 9))
                                }
                                .show(supportFragmentManager, "why")
                    }
                }

            }
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                val dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlag, 0)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder?): Boolean {
                val from = viewHolder.adapterPosition
                val to = target?.adapterPosition ?: -1
                var start = ""
                viewHolder.itemView.apply {
                    start = "$left--$top--$right--$bottom==${translationX}==$translationY"
                }
                var end = ""
                target?.itemView?.apply {
                    end = "$left--$top--$right--$bottom"
                }
//                println("55===========$from->$to===${start+"/"+end}")
//                changeData(recyclerView,from,to)
                return true
            }

            override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                println("71=============moved==$fromPos->$toPos====$x/$y")
                checkePosition = -1
                /**这里不太适合，itemView太大，间距太小，下边的逻辑if条件无法达成，因为等满足条件的收都跑到下一个itemView了。所以需要大间距的
                 * 另外测试发现这里返回的x，y和实际是有差距的，明明看到已经移动到targe的右边的，可打印出来的数据却不是，所以做了下修正，减了20
                 */
                //判断是否是同一行或者同一列
                if (fromPos % 3 == toPos % 3) {//the same column
                    println("78==========${target.itemView.bottom}=====${viewHolder.itemView.height}=======${target.itemView.top}")
                    if ((toPos > fromPos && y > target.itemView.bottom - 20) or (toPos < fromPos && y + viewHolder.itemView.height < target.itemView.top - 20)) {
                        //交换
                        changeData(recyclerView, fromPos, toPos)
                    } else {
                        checkePosition = toPos
                    }

                } else if (fromPos / 3 == toPos / 3) {//the same row
                    println("87==========${target.itemView.right}=====${viewHolder.itemView.width}=======${target.itemView.left}")
                    if ((toPos > fromPos && x > target.itemView.right - 20) or (toPos < fromPos && x + viewHolder.itemView.width < target.itemView.left - 20)) {
                        //交换
                        changeData(recyclerView, fromPos, toPos)
                    } else {
                        checkePosition = toPos
                    }
                } else {
                    changeData(recyclerView, fromPos, toPos)
                }
                recyclerView.adapter.notifyItemChanged(toPos)
            }

            fun changeData(recyclerView: RecyclerView, fromPos: Int, toPos: Int) {
                println("========changeData")
                Collections.swap(urls, fromPos, toPos)
                Collections.swap(books, fromPos, toPos)
                recyclerView.adapter.notifyItemMoved(fromPos, toPos)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                println("62==============$viewHolder========$actionState")
            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
                super.clearView(recyclerView, viewHolder)
                println("67===============${viewHolder?.adapterPosition}========checkedPosition=$checkePosition")
                if (checkePosition >= 0) {
                    checkePosition = -1
                }
            }

            override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
                return 0.5f
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

            }
        }).attachToRecyclerView(rv_book)

    }


    fun initData() {
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-9c767c10de3c992e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/436")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-4d3b91acbec238f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-562c52905730ed41.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-71ae2b0041382790.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/216")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-94317998d3ffe0a0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/568")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-22a39533f950ad99.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/640")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-d3ddf36ef3b1d735.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/249")
        urls.add("https://upload-images.jianshu.io/upload_images/1777208-7100e7946373e28d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
        urls.add("https://upload.jianshu.io/users/upload_avatars/1777208/8d32947b00c5?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96")
        urls.add("https://cdn2.jianshu.io/assets/default_avatar/1-04bbeead395d74921af6a4e8214b4f61.jpg")
        urls.add("https://cdn2.jianshu.io/assets/default_avatar/13-394c31a9cb492fcb39c27422ca7d2815.jpg")

        books.add("1")
        books.add("2")
        books.add("3")
        books.add("4")
        books.add("5")
        books.add("6")
        books.add("7")
        books.add("8")
        books.add("9")
        books.add("10")
        books.add("11")


        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
        datas.add("到底水电费")
        datas.add("到底请稍等")
        datas.add("到底进入")
        datas.add("到底")
        datas.add("到底阿斯顿")
        datas.add("到底的")
        datas.add("到底巍峨")
        datas.add("到底顺道")
        datas.add("到底第三饭")
        datas.add("到底士大夫随风倒十分")
        datas.add("到底色")
    }
}