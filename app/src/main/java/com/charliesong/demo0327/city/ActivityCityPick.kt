package com.charliesong.demo0327.city

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.charliesong.demo0327.BaseActivity
import com.charliesong.demo0327.R
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import kotlinx.android.synthetic.main.activity_city_pick.*

/**
 * Created by charlie.song on 2018/5/2.
 */
class ActivityCityPick:BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pick)

        btn_go.setOnClickListener {
                showCityChoose()
        }
    //

    }

    private fun showCityChoose(){
        var hotCities =  ArrayList<HotCity>();
        hotCities.add( HotCity("北京", "北京", "101010100"));
        hotCities.add( HotCity("上海", "上海", "101020100"));
        hotCities.add( HotCity("广州", "广东", "101280101"));
        hotCities.add( HotCity("深圳", "广东", "101280601"));
        hotCities.add( HotCity("杭州", "浙江", "101210101"));

        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())  //此方法必须调用
                .enableAnimation(cb_enable_animator.isChecked)  //启用动画效果
//                .setAnimationStyle(anim)  //自定义动画
//                .setLocatedCity( LocatedCity("杭州", "浙江", "101210101"))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)  //指定热门城市
                .setOnPickListener(object : OnPickListener {
                    override fun onPick(position:Int, data:City?) {
                        Toast.makeText(getApplicationContext(), data?.getName()?:"", Toast.LENGTH_SHORT).show();
                    }

                    override fun onLocate() {
                        //开始定位，这里模拟一下定位
                        println("==============start location")
                         Handler().postDelayed(object :Runnable {
                             override fun run() {
                                println("==============start location end")
                                //定位完成之后更新数据
                                CityPicker.getInstance()
                                        .locateComplete( LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 2000);
                    }
                })
                .show();
    }
}
