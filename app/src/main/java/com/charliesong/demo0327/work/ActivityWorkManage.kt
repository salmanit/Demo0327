package com.charliesong.demo0327.work

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.TextUtils
import androidx.work.*
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.util.UtilNormal
import kotlinx.android.synthetic.main.activity_work_manage.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityWorkManage : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manage)
        defaultSetTitle("work manage")



        btn_start.setOnClickListener {
            startWork()
        }

        btn_get.setOnClickListener {
            getStatus()

        }

        btn_start_repeat.setOnClickListener {
            startRepeateWork()
        }
        btn_get_repeat.setOnClickListener {
            getRepeatWorkStatus()
        }

        btn_cancel_repeat.setOnClickListener {
            cancelRepeatWork()
        }
    }

    fun startWork() {
        var constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        var inputData = Data.Builder().putString("title", "title1").build()

        var reques = OneTimeWorkRequest.Builder(MyWorker::class.java).setInitialDelay(20, TimeUnit.SECONDS).addTag("work1").setConstraints(constraint).build()
        var reques2 = OneTimeWorkRequest.Builder(MyWorker::class.java).setInitialDelay(50, TimeUnit.SECONDS).addTag("work2").setInputData(inputData).build()
        var reques3 = OneTimeWorkRequest.Builder(MyWorker::class.java).setInitialDelay(50, TimeUnit.SECONDS).addTag("work3").build()
        WorkManager.getInstance().beginWith(reques, reques2).then(reques3).enqueue()
    }

    fun getStatus() {
        WorkManager.getInstance().getStatusesByTag("work1")?.apply {
            removeObserver(observer)
            observeForever(observer)
        }
        println("111===========${WorkManager.getInstance().getStatusesByTag("work2")}====${WorkManager.getInstance().getStatusesByTag("work2")?.value?.size}")
    }
    val observer=object :android.arch.lifecycle.Observer<MutableList<WorkStatus>> {
        override fun onChanged(t: MutableList<WorkStatus>?) {
            println("00=================${t?.size}")
            (0 until (t?.size?:0)).forEach {
                var workstaus = t!![it]
                println("${it}==============${workstaus.id}====${workstaus.state}//${workstaus.state.isFinished}==${workstaus.tags}")
            }
        }
    }
    public class MyWorker : Worker() {
        var formate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        override fun doWork(): WorkerResult {
            var smg = formate.format(Date(System.currentTimeMillis()))
            inputData.apply {
                smg = this.getString("title", "") +"  "+ smg
            }
            UtilNormal.saveWords(smg)
            return WorkerResult.SUCCESS
        }
    }


    fun startRepeateWork(){
        var request=PeriodicWorkRequest.Builder(MyWorker::class.java,1,TimeUnit.MINUTES).addTag("hello").build()
        WorkManager.getInstance().enqueue(request)
        UtilNormal.saveUUID(this,request.id.toString())
    }

    fun getRepeatWorkStatus(){
        var str=UtilNormal.getUUID(this)
        if(TextUtils.isEmpty(str)){
            return
        }
        WorkManager.getInstance().getStatusById(UUID.fromString(str))?.apply {
            this.removeObserver(observerOnly)
            this.observeForever(observerOnly)

        }
    }
    fun cancelRepeatWork(){
        var str=UtilNormal.getUUID(this)
        if(TextUtils.isEmpty(str)){
            return
        }
        WorkManager.getInstance().cancelWorkById(UUID.fromString(str))
//        WorkManager.getInstance().cancelAllWorkByTag("hello")

    }
    val observerOnly=object:Observer<WorkStatus>{
        override fun onChanged(t: WorkStatus?) {
            t?.apply {
                println("onChanged==============${t.id}====${t.state}//${t.state.isFinished}==${t.tags}")
            }
        }
    }
}