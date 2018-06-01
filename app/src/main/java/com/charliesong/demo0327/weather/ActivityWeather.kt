package com.charliesong.demo0327.weather

import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_weather2.*
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class ActivityWeather : BaseActivity() {
    var dp180=180f
    var intervalLoation=0;
    var intervalDate=0
    var intervalTemperature=0

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_weather2)
        dp180=resources.displayMetrics.density*180
        sv.onScrollChangeListener=object :ScrollChangeListener{
            override fun onScroollY(yScroll: Float) {
                if(intervalLoation==0){
                    intervalLoation=layout_location.left
                    intervalDate=layout_date.left
                    intervalTemperature=layout_temperature.left
                    println("left===========$intervalLoation===$intervalDate===$intervalTemperature")
                }
                if(yScroll<dp180){
                    layout_location.translationX=(-yScroll/dp180)*intervalLoation
                    layout_date.translationX=(-yScroll/dp180)*intervalDate
                    layout_location.translationY=(-yScroll/dp180)*40
                    layout_date.translationY=(-yScroll/dp180)*40
                    layout_temperature.translationX=200*(yScroll/dp180)
                    layout_temperature.translationY=(-yScroll/dp180)*80
                    println("transx=========${layout_location.translationX}  date=${layout_location_date.translationX} temperature==${layout_temperature.translationX}")
                }

            }
        }
//     backRun()
    }

    private fun backRun(){
        println("45==================${Thread.currentThread().name}==${Thread.currentThread().id}")
        Thread(object:Runnable{
            override fun run() {
                testCallBack()
            }
        }).start()
    }
    private fun testCallBack(){
        val cb=object:Callable<String>{
            override fun call(): String {
                    Thread.sleep(3333)
                println("task complet===============${Thread.currentThread().name}==${Thread.currentThread().id}")
                return "return result=================48"
            }
        }
        val futureTask=FutureTask(cb)
        println("53==================start")
        Thread(futureTask).start()
        println("55=====================start2====${Thread.currentThread().name}==${Thread.currentThread().id}")
        val result=futureTask.get()
        println("58=====get result=============$result")
    }
}
