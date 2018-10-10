package com.charliesong.demo0327.camera

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_camera.*
import java.io.File

class ActivityCustomCamera : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera)

    }

    fun takePIC(v:View){
        val file=File(getExternalFilesDir(""),"${System.currentTimeMillis()}.jpg")
        sf.takePic(file)

    }

    fun resetSurface(v:View){
        sf.resetThis()
    }
}
