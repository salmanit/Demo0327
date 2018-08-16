package com.charliesong.demo0327.nofity

import android.Manifest
import android.app.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import android.graphics.BitmapFactory
import android.content.Context
import android.content.Intent
import android.os.UserManager
import android.widget.Button
import android.widget.TextView
import com.charliesong.demo0327.widget.ShowFloatingWindow
import kotlinx.android.synthetic.main.activity_nofity8.*


class ActivityNofity8 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nofity8)
        defaultSetTitle("notify")
        notifyHandle()
        floatingWindowHandle()
        var he=findViewById(R.id.btn_write) as Button

//        println("=====${intent?.extras}====${intent?.getStringExtra("title")}")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        println("newIntent===${getIntent()}=====${getIntent()?.getStringExtra("title")}===========${intent?.extras}====${intent?.getStringExtra("title")}")
    }

    private fun floatingWindowHandle(){
        btn_show_float.setOnClickListener {
            ShowFloatingWindow().showView()
        }
    }

    private fun notifyHandle(){
        createChannel("group_001", "广告", "channel_001", "推广信息", "自家的推广信息")
        createChannel("group_001", "广告", "channel_002", "游戏推广", "游戏推广信息")
        createChannel("group_002", "聊天", "channel_003", "聊天消息", "聊天消息xxx")
        createChannel("group_002", "聊天", "channel_004", "好友申请", "好友申请xxx")
        createChannel(null,null,"channel_005","更新提示","版本更新提示")
        btn_ad_1.setOnClickListener {
            createNotificationChannel( "channel_001", "推广信息", "自家的推广信息",1)

        }
        btn_ad_2.setOnClickListener {
            createNotificationChannel("channel_002", "游戏推广", "游戏推广信息",2)
        }

        btn_chat_1.setOnClickListener {
            createNotificationChannel("channel_003", "聊天消息", "聊天消息xxx",3)
        }
        btn_chat_2.setOnClickListener {
            createNotificationChannel("channel_004", "好友申请", "好友申请xxx",4)
        }

        btn_delete5.setOnClickListener {
            deleteNotificationChannel("channel_005")
        }
        btn_delete_group1.setOnClickListener {
            deleteNotificationChannelGroup("group_001")
        }
    }
    //创建分组，渠道，可以分开弄的，这样group就不用多次creat了。
    private fun createChannel(groupID: String?, groupName: String?, channelID: String, channelName: String, channelDes: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //channelId要唯一
            val adChannel = NotificationChannel(channelID,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT)
            //补充channel的含义（可选）
            adChannel.description = channelDes
            adChannel.setShowBadge(true)//默认是true，在app的logo右上角有个圆点，而且长按logo还能看到最新一条通知

            //将渠道添加进组（先创建组才能添加）
            groupID?.apply {
                //分组（可选）
                notificationManager.createNotificationChannelGroup(NotificationChannelGroup(this,groupName))
                adChannel.group=groupID
            }
            //创建channel
            notificationManager.createNotificationChannel(adChannel)
        }
    }
    //SDK26以上创建通知的builder的时候需要一个channel id
    private fun createNotificationChannel( channelID: String, title: String, content: String,id:Int) {
        var builder: Notification.Builder = Notification.Builder(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知时，标记你的渠道id
            builder = Notification.Builder(this, channelID)
            builder.setTimeoutAfter(1*60*1000)//1分钟后自动消失
        }
        val bundle=Bundle()
        bundle.putString("title","title...........")
        val intent=PendingIntent.getActivity(this,10, Intent(this,ActivityNofity8::class.java),PendingIntent.FLAG_UPDATE_CURRENT,bundle)
        val notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle("This is $title")
                .setContentText("content:$content")
                .setContentIntent(intent)
                .setAutoCancel(true)
                .setNumber(1)
                .build()
        notificationManager.notify(id, notification)
    }

    //删除某个渠道，传入渠道id
    private fun deleteNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.deleteNotificationChannel(channelId)
        }
    }
    //删除某个分组，传入分组id，需要注意的是，group删了，它下边的channel也都删了，相关的channel的通知都弹不了了。
    private fun deleteNotificationChannelGroup(groupID: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.deleteNotificationChannelGroup(groupID)
        }
    }

    fun checkStoragePermission() {
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun checkPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                println("=========shouldShowRequestPermissionRationale")
            } else {
                println("=========shouldShowRequestPermissionRationale====false")
            }
//          ActivityCompat.requestPermissions(this, arrayOf())
        }

    }

    fun getPermissions(permission: String) {
        when (permission) {

        }
    }
}
