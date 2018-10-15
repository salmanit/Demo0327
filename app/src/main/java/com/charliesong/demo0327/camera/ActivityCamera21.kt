package com.charliesong.demo0327.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.*
import android.support.annotation.RequiresApi
import android.view.Window
import android.view.WindowManager
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity

class ActivityCamera21 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏模式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_camera2)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP||getSystemService(Context.CAMERA_SERVICE) as CameraManager==null) {
            showToast("不支持camera2")
            finish()
            return
        }
        requestCamera()
        requestRecordAudio()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun requestPermissionSuccess() {
        supportFragmentManager.findFragmentById(R.id.layout_container)?:
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container,FragmentCamera2(),FragmentCamera2::class.java.name).commitAllowingStateLoss()
    }


}
