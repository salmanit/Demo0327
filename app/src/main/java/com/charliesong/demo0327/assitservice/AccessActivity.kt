package com.charliesong.demo0327.assitservice

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.charliesong.demo0327.*
import kotlinx.android.synthetic.main.access_activity.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.Future
import android.content.DialogInterface
import android.support.v4.app.DialogFragment
import com.charliesong.demo0327.assitservice.AccessActivity.DeleteDialogFragment




/**
 * Created by charlie.song on 2018/3/30.
 */
class AccessActivity : BaseActivity() {

    var url1="https://upload.jianshu.io/users/upload_avatars/3994917/ae996b9a-5391-496f-b209-3367fb5b0112.png?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
    var url2="https://upload.jianshu.io/users/upload_avatars/5275362/63c0bb2d-b849-4909-9136-b5aae8b01dcc.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.access_activity)

        tv_check.setOnClickListener {
        var result=AssistUtil.isAccessibilitySettingsOn(this,AssistService::class.java)
            showToast("$result")
        }

        tv_go_set.setOnClickListener {
            AssistUtil.goSetService(this)
        }

        tv_click.setOnClickListener {
//            emulateSyncThread()

            DeleteDialogFragment().show(supportFragmentManager, "delete")
        }
        rv_contact.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true)
            adapter=object :BaseRvAdapter<String>(){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_simple_imageview
                }
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    var iv=holder.getView<ImageView>(R.id.iv_head)
                    MyApplication.loadCircleImage(if(position%2==0) url1 else url2,iv)
                    if(position>0){
                        var params=iv.layoutParams as RecyclerView.LayoutParams
                        params.rightMargin=-25
                        iv.layoutParams=params
                    }
                }

                override fun getItemCount(): Int {
                    return 4
                }
            }

        }



    }

    var callback1=object :Callable<String>{
        override fun call(): String {
            Thread.sleep(1000)

            return "1s"
        }
    }
    var callback2=object :Callable<String>{
        override fun call(): String {
            Thread.sleep(2000)

            return "2s"
        }
    }
    var excutors= Executors.newFixedThreadPool(10)
    private fun emulateSyncThread(){

        var futures= ArrayList<Future<String>>()

        for(i in 0 until 10 ){
            var future:Future<String>
            if(i%2==0){
              future=  excutors.submit(callback1)
            }else{
              future=  excutors.submit(callback2)
            }
//            println("$i========${future.get()}")
            futures.add(future)
        }
        var sb=StringBuffer()
        var format=SimpleDateFormat("HH:mm:ss sss", Locale.CHINESE)
        println("time1==========${format.format(Date(System.currentTimeMillis()))}")
        for(i in 0 until 10){
//            sb.append(futures[i].get())
            println("$i=======${futures[i].get()}")
        }
        println("time2==========${format.format(Date(System.currentTimeMillis()))}")
        println("result============${sb.toString()}")
    }

//id================@16908313   id================@16908314
     class DeleteDialogFragment : DialogFragment() {
        override fun dismiss() {
            System.err.println("DeleteDialogFragment==================1==============dismiss")
            super.dismiss()
        }

        override fun onDismiss(dialog: DialogInterface) {
            System.err.println("DeleteDialogFragment================================dismiss")
            super.onDismiss(dialog)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(getActivity()).setTitle("delete")
                    .setMessage("are you sure  you want to delete this")
                    .setNegativeButton("cancel", object : DialogInterface.OnClickListener {

                        override fun onClick(dialog: DialogInterface, which: Int) {
                            System.err.println("================================click cancel")
                        }
                    }).setPositiveButton("ok", object : DialogInterface.OnClickListener {

                        override fun onClick(dialog: DialogInterface, which: Int) {
                            System.err.println("================================click ok")

                        }
                    })
                    .setOnCancelListener(object : DialogInterface.OnCancelListener {

                        override fun onCancel(dialog: DialogInterface) {
                            System.err.println("================================cancel")

                        }
                    })
                    .create()

        }
    }





}