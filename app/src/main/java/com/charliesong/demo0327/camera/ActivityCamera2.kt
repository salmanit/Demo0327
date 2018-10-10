package com.charliesong.demo0327.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_camera2.*
import java.io.File
import java.io.FileOutputStream

@SuppressLint("NewApi")
class ActivityCamera2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish()
            return
        }


    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if(ttv.isAvailable){

        }else{
            initTexttureView()
        }
    }

    override fun onPause() {
        super.onPause()
        stopBackgroundThread()
    }
var count=0
    fun initTexttureView() {
        ttv.apply {
            surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
                    println("onSurfaceTextureSizeChanged===============w/h==$width/$height")
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                    if(count%50==0)
                        println("onSurfaceTextureUpdated=======================")
                    count++
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                    println("onSurfaceTextureDestroyed=========================")
                    return true
                }

                override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                    println("onSurfaceTextureAvailable===========================w/h==$width/$height")
                    initCamera()
                }
            }
        }
    }

    var cameraManager: CameraManager? = null
    @SuppressLint("NewApi", "MissingPermission")
    fun initCamera() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameralists = cameraManager?.cameraIdList
        cameralists?.forEach {
            println("camera=========$it")
        }
        val characteristics = cameraManager?.getCameraCharacteristics("0")
        characteristics?.apply {
            this.keys.forEach {
                println("kv====================${it.name}=========${characteristics.get(it)}")
            }
//            this.availableCaptureRequestKeys.forEach {
//                println("kv1===============${it.name}")
//            }
//            this.availableCaptureResultKeys.forEach {
//                println("kv2===================${it.name}")
//            }
        }

        cameraManager?.registerAvailabilityCallback(availabilityCallback, null)
        cameraManager?.registerTorchCallback(torchCallback, null)
        cameraManager?.openCamera("0", callback, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager?.unregisterAvailabilityCallback(availabilityCallback)
        cameraManager?.unregisterTorchCallback(torchCallback)

    }

    val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String?, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
            println("onTorchModeChanged=====================$enabled")
        }

        override fun onTorchModeUnavailable(cameraId: String?) {
            super.onTorchModeUnavailable(cameraId)
            println("onTorchModeUnavailable========================")
        }
    }

    val availabilityCallback = object : CameraManager.AvailabilityCallback() {
        override fun onCameraAvailable(cameraId: String?) {
            println("onCameraAvailable============")
        }

        override fun onCameraUnavailable(cameraId: String?) {
            println("onCameraUnavailable=========================")
        }
    }
    var camera1: CameraDevice?=null
    val callback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            println("onOpened===================${camera.id}")
            createCamerePreviewSession(camera)
            camera1=camera
        }

        override fun onDisconnected(camera: CameraDevice?) {
            println("onDisconnected======================")
            camera?.close()
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            println("onError=================$error")
            onDisconnected(camera)
        }
    }

    val captureCallback=object :CameraCaptureSession.CaptureCallback(){

        override fun onCaptureProgressed(session: CameraCaptureSession?, request: CaptureRequest?, partialResult: CaptureResult?) {
            super.onCaptureProgressed(session, request, partialResult)
            println("onCaptureProgressed================")
        }

        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
            super.onCaptureCompleted(session, request, result)
            if(count%30==0)
                println("onCaptureCompleted=============$count")
        }

        override fun onCaptureFailed(session: CameraCaptureSession?, request: CaptureRequest?, failure: CaptureFailure?) {
            super.onCaptureFailed(session, request, failure)
            println("onCaptureFailed====================$failure")
        }
    }
    var imageReader:ImageReader?=null
    fun createCamerePreviewSession(camera: CameraDevice) {
        ttv.surfaceTexture.apply {
            //            setDefaultBufferSize(1024,768)
            val surface = Surface(this)
            imageReader= ImageReader.newInstance(1024,768,ImageFormat.JPEG,1)
            println("imagereader=====================$imageReader")
             builder=camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    builder.apply {
                this?.addTarget(surface)
                camera.createCaptureSession(arrayListOf(surface,imageReader?.surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession?) {
                        println("onConfigureFailed=================")
                    }

                    override fun onConfigured(session: CameraCaptureSession?) {
                        println("onConfigured==============")
                        builder?.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        session?.setRepeatingRequest(builder?.build(),captureCallback,backgroundHandler)
                        cameraSession=session
                    }
                }, null)
            }
        }
    }
    var builder:CaptureRequest.Builder?=null
    private var cameraSession:CameraCaptureSession?=null
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread?.looper)
    }
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e("========", e.toString())
        }

    }
    var take=false;
    fun takePIC(v: View) {
        take=true;
        cameraSession?.capture(builder?.build(),captureCallback,null)


    }
    private fun savePic(){
        imageReader?.apply {
            val buffer=this.acquireLatestImage().planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            val file=File(Environment.getExternalStorageDirectory(),"1.jpg")
            println("file===============${file.absolutePath}")
            val fos=FileOutputStream(file)
            fos.write(bytes)
            fos.close()
        }
    }
    fun reset(v: View) {

    }
}
