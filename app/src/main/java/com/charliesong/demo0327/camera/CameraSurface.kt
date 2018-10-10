package com.charliesong.demo0327.camera

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.graphics.ImageFormat
import android.util.AttributeSet
import java.io.File
import java.io.FileOutputStream


class CameraSurface : SurfaceView, SurfaceHolder.Callback2 {

    constructor(c: Context) : super(c) {
        initHolder()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHolder()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initHolder()
    }

    private fun initHolder() {
        val holder = getHolder()
        //指定回调接口
        holder.addCallback(this)
    }

    private var theCamera: Camera? = null
    override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
        println("========================surfaceRedrawNeeded")
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        println("===================surfaceChanged==$width/$height===========$format")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        println("=================surfaceDestroyed")
        theCamera?.stopPreview()
        theCamera = null;
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        println("====================surfaceCreated")
        if (theCamera == null) {
            theCamera = Camera.open(1)
        }
        theCamera?.apply {
            val myParameters = getParameters();
            val supportPicSize = myParameters.supportedPictureSizes
            supportPicSize.forEach {
                println("support============${it.width}/${it.height}")
            }
            val support = myParameters.supportedPreviewSizes
            support.forEach {
                println("preview size=========${it.width}/${it.height}")
            }
            val index=Math.min(supportPicSize.size-1,supportPicSize.size/2)
            val saveSize=supportPicSize[index]
            println("index===$index=====${saveSize.width}/${saveSize.height}")
            myParameters.setPictureFormat(ImageFormat.JPEG);
            myParameters.set("jpeg-quality", 85);
            //需要注意下边2个size的大小不能随便改，需要是上边打印的support的值才可以。
            //而且宽高也是当前屏幕方向的值，如果屏幕旋转了，宽高值就反过来了。
            myParameters.setPreviewSize(640, 480)//如果要改预览图片大小也可以改
            myParameters.setPictureSize(saveSize.width, saveSize.height);
            myParameters.setGpsLongitude(131.123456)
            myParameters.setGpsLatitude(34.5555)
            myParameters.setGpsTimestamp(System.currentTimeMillis())
            myParameters.setGpsAltitude(521.125)
            myParameters.setGpsProcessingMethod("gps")
            parameters = myParameters
            setPreviewDisplay(holder);
            startPreview()
        }
    }

    fun resetThis() {
        surfaceCreated(holder)
    }

    fun takePic(desFile: File) {
        theCamera?.takePicture(null, null, object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
                try {
                    //弄成bitmap压缩以后，额外信息就丢失了，而且文件还比直接保存data的大一倍。
//                    val a = BitmapFactory.decodeByteArray(data, 0, data?.size ?: 0)
//                    val out = FileOutputStream(desFile)
//                    val result = a?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    val fos = FileOutputStream(desFile)
                    fos.write(data)
                    fos.close()
                    println("f===============${desFile.absolutePath}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}