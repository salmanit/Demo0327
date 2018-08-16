package com.charliesong.demo0327.livedata

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.support.v4.widget.PopupWindowCompat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.PopupWindow
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_livedata_learn.*
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Parcelable
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.support.transition.AutoTransition
import android.transition.Transition
import android.util.Log
import android.view.Gravity
import java.util.*


class ActivityLiveDataLearn : BaseActivity() {

    lateinit var modle: ViewModleTest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata_learn)
        defaultSetTitle("livedata learn")
        modle = ViewModelProviders.of(this).get(ViewModleTest::class.java)

        with(modle) {
            tv_name.text = this.getName().value+"=${modle}"
            getName().observe(this@ActivityLiveDataLearn, Observer {

                tv_name.text = it
            })

            getNameListData().observe(this@ActivityLiveDataLearn, Observer {

                val sb = StringBuffer()
                it?.forEach {
                    sb.append(it)
                }
                tv_name.text = sb.toString()
            })
        }

        btn_change.setOnClickListener {
            modle.getName().value = "current $num"
        }

        btn_add.setOnClickListener {
            val data = modle.getNameListData().value ?: arrayListOf()
            data.add("add $num  ")
            num++
            modle.getNameListData().value = data
        }


        btn_show_pw.setOnClickListener {
            showPW(it)
        }


        initWV()
    }

    private fun initWV() {
        wv.apply {

            val mWebSettings = wv.getSettings()
            mWebSettings.setJavaScriptEnabled(true)
//        mWebSettings.setSupportZoom(true);
//        mWebSettings.setBuiltInZoomControls(true);
            //mWebSettings.setDisplayZoomControls(false);
            mWebSettings.setAppCacheEnabled(true)
//            var mode = if (isNetworkConnected(this@ActivityLiveDataLearn)) WebSettings.LOAD_NO_CACHE else WebSettings.LOAD_CACHE_ONLY
//            println("mode=====================$mode")
//            mWebSettings.setCacheMode(mode);
            mWebSettings.setAppCachePath(getContext().getCacheDir().getAbsolutePath() + "/myWeb");
            mWebSettings.setAppCachePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myWeb")
            mWebSettings.setDatabaseEnabled(true)//启用数据库

            mWebSettings.setLoadWithOverviewMode(true)
            mWebSettings.setUseWideViewPort(true)
            mWebSettings.setDefaultTextEncodingName("GBK")
            mWebSettings.setLoadsImagesAutomatically(true)
            //启用地理定位
            mWebSettings.setGeolocationEnabled(true)
            mWebSettings.setGeolocationDatabasePath(context.applicationContext.getDir("database",
                    Context.MODE_PRIVATE).path)
            //开启DomStorage缓存
            mWebSettings.setDomStorageEnabled(true)

            setWebChromeClient(WebChromeClient())
            setWebViewClient(WebViewClient())

            loadUrl("http://www.baidu.com")
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (manager == null) {
            println("ConnectivityManager is null")
            return false
        }
        val activeNetwork = manager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting

    }

    var internetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
                val activeNetwork = (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetworkInfo
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected) {
                        connectedChange(true)
                    } else {
                        connectedChange(false)
                    }

                } else {
                    connectedChange(false)
                }
            }
        }
    }
    var modeOld=-1
    fun connectedChange(isConnected: Boolean) {
        var mode = if (isConnected) WebSettings.LOAD_NO_CACHE else WebSettings.LOAD_CACHE_ONLY
        if(modeOld==-1||modeOld!=mode){
            wv.settings.cacheMode = mode
            if(modeOld==WebSettings.LOAD_CACHE_ONLY){
                wv.reload()
            }
            modeOld=mode
        }
        println("connectedChange mode=====================$mode")

    }

    override fun onResume() {
        super.onResume()
        var filter = IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(internetReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetReceiver)
    }

    override fun onBackPressed() {
        if(wv.canGoBack()){
            wv.goBack()
        }else{
            super.onBackPressed()
        }
    }

    var num = 0

    fun showPW(v: View) {
        var view = LayoutInflater.from(this).inflate(R.layout.pw_simple_text, null)
        var pw = PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, 500)
        pw.isOutsideTouchable = true
        pw.setBackgroundDrawable(ColorDrawable())
        pw.isFocusable = true
        if(Build.VERSION.SDK_INT>=23){
            pw.enterTransition=android.transition.AutoTransition()
        }
//        pw.showAtLocation(v,Gravity.CENTER  ,0,0)
        pw.showAsDropDown(v)
//        testSomething(btn_bottom)

    }


    private fun testSomething(v:View){
        var mDrawingLocation=IntArray(2)
        v.getLocationInWindow(mDrawingLocation);
        println("111=============${Arrays.toString(mDrawingLocation)}")
        var r=Rect(10,10,110,100)
        var result=v.requestRectangleOnScreen(r, true); //这个作用是啥查一下

        v.getLocationInWindow(mDrawingLocation);
        println("222======${result}=======${Arrays.toString(mDrawingLocation)}")
    }
}


class NetworkConnectChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent.action) {
//            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
//            Log.e(TAG1, "wifiState$wifiState")
//            when (wifiState) {
//                WifiManager.WIFI_STATE_DISABLED -> {//do something
//                     }
//                WifiManager.WIFI_STATE_DISABLING -> {
//                }
//                WifiManager.WIFI_STATE_ENABLING -> {
//                }
//                WifiManager.WIFI_STATE_ENABLED ->{
//                    //do some thing
//                }
//                WifiManager.WIFI_STATE_UNKNOWN -> {
//                }
//                else -> {
//                }
//            }
//        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager
        // .WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
        // 当然刚打开wifi肯定还没有连接到有效的无线
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
//            val parcelableExtra = intent
//                    .getParcelableExtra<Parcelable>(WifiManager.EXTRA_NETWORK_INFO)
//            if (null != parcelableExtra) {
//                val networkInfo = parcelableExtra as NetworkInfo
//                val state = networkInfo.state
//                val isConnected = state === State.CONNECTED// 当然，这边可以更精确的确定状态
//                Log.e(TAG1, "isConnected$isConnected")
//                if (isConnected) {
//                    //APP.getInstance().setWifi(true)
//                } else {
//                   // APP.getInstance().setWifi(false)
//                }
//            }
//        }
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val manager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            Log.i(TAG1, "CONNECTIVITY_ACTION")

            val activeNetwork = manager.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected) {
                    if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
//                        APP.getInstance().setWifi(true)
                        Log.e(TAG, "当前WiFi连接可用 ")
                    } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
//                        APP.getInstance().setMobile(true)
                        Log.e(TAG, "当前移动网络连接可用 ")
                    }
                } else {
                    Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ")
                }


                Log.e(TAG1, "info.getTypeName()" + activeNetwork.typeName)
                Log.e(TAG1, "getSubtypeName()" + activeNetwork.subtypeName)
                Log.e(TAG1, "getState()" + activeNetwork.state)
                Log.e(TAG1, "getDetailedState()" + activeNetwork.detailedState.name)
                Log.e(TAG1, "getDetailedState()" + activeNetwork.extraInfo)
                Log.e(TAG1, "getType()" + activeNetwork.type)
            } else {   // not connected to the internet
                Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ")
//                APP.getInstance().setWifi(false)
//                APP.getInstance().setMobile(false)
//                APP.getInstance().setConnected(false)

            }


        }
    }

    companion object {

        private val TAG = "xujun"
        val TAG1 = "xxx"
    }


}




