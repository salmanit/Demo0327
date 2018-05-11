package com.charliesong.demo0327.words

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by charlie.song on 2018/4/28.
 */
class ItemDecorationSpace:RecyclerView.ItemDecoration{
    constructor() : super()


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left=5
        outRect.right=5
        outRect.bottom=11
    }
}