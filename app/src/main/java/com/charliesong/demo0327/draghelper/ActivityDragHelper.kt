package com.charliesong.demo0327.draghelper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.LinearLayoutManager
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_drag_helper.*

class ActivityDragHelper : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_helper)

        rv_bottom.apply {
            layoutManager= LinearLayoutManager(this@ActivityDragHelper)

            adapter=object : BaseRvAdapter<String>(){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_text
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setText(R.id.tv_content,"$position  test data")
                }

                override fun getItemCount(): Int {
                    return 30
                }
            }

        }

    }
}
