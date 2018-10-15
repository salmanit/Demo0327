package com.charliesong.demo0327.camera

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_camera.*
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class ActivityCustomCamera : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏模式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_custom_camera)
        requestCamera()
        sf.captureSuccess=object :CameraSurface.CaptureSuccessIML{
            override fun savePictureSuccess(desFile: File) {
                showToast("capture success")
//                sf.resetThis()
            }
        }
    }

    override fun requestPermissionSuccess() {
        sf.resetThis()
    }
    val simpleDateFormat= SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    fun takePIC(v:View){
        val file=File(getExternalFilesDir(""),"${simpleDateFormat.format(Date(System.currentTimeMillis()))}.jpg")
        sf.takePic(file)

    }

    fun resetSurface(v:View){
        sf.resetThis()
    }
    fun changeCamera(v:View){
        sf.changeCamera(v)
    }
}
