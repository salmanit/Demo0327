package com.charliesong.demo0327.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_camera21.*
import java.io.File
import java.io.FileOutputStream
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("NewApi")
class FragmentCamera21:BaseFragment(){
    override fun getLayoutID(): Int {
      return  R.layout.fragment_camera21
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_capture.setOnClickListener {
            takePIC(it)
        }
        btn_reset.setOnClickListener {
            unlockFocus()
        }
    }

    private fun unlockFocus() {
        try {
            // Reset the auto-focus trigger
            builder?.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
//            setAutoFlash(previewRequestBuilder)
            cameraSession?.capture(builder?.build(), captureCallback,
                    backgroundHandler)
            // After this, the camera will go back to the normal state of preview.
            take=false
            cameraSession?.setRepeatingRequest(previewRequest, captureCallback,
                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e("==========", e.toString())
        }

    }
    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        println("onResume==============${ttv.isAvailable}")
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
//                    if(count%50==0)
//                        println("onSurfaceTextureUpdated=======================")
//                    count++
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
    private var sensorOrientation=0

    @SuppressLint("NewApi", "MissingPermission")
    fun initCamera() {
        cameraManager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
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
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
            println("sensorOrientation ==========$sensorOrientation")
        }
        val map = characteristics?.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        map?.getOutputSizes(ImageFormat.JPEG)?.forEach {
            println("support size======${it.width}/${it.height}")
        }
        map?.getInputSizes(ImageFormat.JPEG)?.forEach {
            println("input size============${it.width}/${it.height}")
        }
        cameraManager?.registerAvailabilityCallback(availabilityCallback, null)
//        cameraManager?.registerTorchCallback(torchCallback, null)//手电筒模式打开的情况下才可以用，否则有问题
        cameraManager?.openCamera("0", callback, null)
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraManager?.unregisterAvailabilityCallback(availabilityCallback)
//        cameraManager?.unregisterTorchCallback(torchCallback)

    }



    val availabilityCallback = object : CameraManager.AvailabilityCallback() {
        override fun onCameraAvailable(cameraId: String?) {
            println("onCameraAvailable============")
        }

        override fun onCameraUnavailable(cameraId: String?) {
            println("onCameraUnavailable=========================")
        }
    }


    var cameraDevice: CameraDevice?=null
    val callback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            println("onOpened===================${camera.id}")
            createCamerePreviewSession(camera)
            cameraDevice=camera
        }

        override fun onDisconnected(camera: CameraDevice?) {
            println("onDisconnected======================")
            camera?.close()
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            println("CameraDevice onError=================$error")
            onDisconnected(camera)
        }
    }

    val captureCallback=object : CameraCaptureSession.CaptureCallback(){

        override fun onCaptureProgressed(session: CameraCaptureSession?, request: CaptureRequest?, partialResult: CaptureResult) {
            super.onCaptureProgressed(session, request, partialResult)
            println("onCaptureProgressed================")
            prosess(partialResult)
        }

        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
            prosess(result)
        }

        override fun onCaptureFailed(session: CameraCaptureSession?, request: CaptureRequest?, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
            println("onCaptureFailed====================${failure.reason}")
        }

        fun prosess(result: CaptureResult){
            if(!take){
                return
            }
            val afState=result.get(CaptureResult.CONTROL_AF_STATE)
            println("af state=============${afState}")
            captureStillPicture()
//            if(afState==null){
//                captureStillPicture()
//            }else if(afState==CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED||afState==CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED){
//               val aeState=result.get(CaptureResult.CONTROL_AE_STATE)
//                println("ae state=========$aeState")
//                if(aeState==null||aeState==CaptureResult.CONTROL_AE_STATE_CONVERGED){
//                    take=false;
//                    captureStillPicture()
//                }else{
//
//                }
//            }
        }

    }

    fun captureStillPicture(){
        if (activity == null || cameraDevice == null) return
        val rotation = activity!!.windowManager.defaultDisplay.rotation
        val captureBuilder=cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {

            addTarget(imageReader?.surface)
            set(CaptureRequest.JPEG_ORIENTATION,
                    (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360)

            // Use the same AE and AF modes as the preview.
            set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
        }
        val captureCallback = object : CameraCaptureSession.CaptureCallback() {

            override fun onCaptureCompleted(session: CameraCaptureSession,
                                            request: CaptureRequest,
                                            result: TotalCaptureResult) {
                take=false;
                println("===============save...........")
//                unlockFocus()
            }
        }
        cameraSession?.apply {
            stopRepeating()
//            abortCaptures()
            this.capture(captureBuilder?.build(),captureCallback,backgroundHandler)
        }
    }

    private  var previewRequest: CaptureRequest?=null
    var imageReader: ImageReader?=null
    fun createCamerePreviewSession(camera: CameraDevice) {

        ttv.surfaceTexture.apply {
            //这里需要修改bufferSize为相机支持的大小
            setDefaultBufferSize(1920,1080)
            val surface = Surface(this)
            //ImageReader的宽高必须是相机支持的宽高，否则下边的session会创建失败
            imageReader= ImageReader.newInstance(1920,1080, ImageFormat.JPEG,1).apply {
                setOnImageAvailableListener(object :ImageReader.OnImageAvailableListener{
                    override fun onImageAvailable(reader: ImageReader?) {
                        savePic()
                    }
                },backgroundHandler)
            }
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
                        previewRequest=builder?.build()
                        session?.setRepeatingRequest(previewRequest,null,backgroundHandler)
                        cameraSession=session
                    }
                }, null)
            }
        }
    }
    var builder: CaptureRequest.Builder?=null
    private var cameraSession: CameraCaptureSession?=null
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
        builder?.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START)
        take=true;
        cameraSession?.capture(builder?.build(),captureCallback,backgroundHandler)

    }
    val simpleDateFormat=SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    private fun savePic(){
        imageReader?.apply {
            this.acquireNextImage()?.apply {
                val buffer=this.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                val file= File(Environment.getExternalStorageDirectory(),"${simpleDateFormat.format(Date(System.currentTimeMillis()))}.jpg")
                val fos= FileOutputStream(file)
                fos.write(bytes)
                println("file===============${file.absolutePath}=======${file.length()}===${bytes.size}")
                this.close()
                fos.close()
            }

        }
    }

//    val torchCallback = object : CameraManager.TorchCallback() {
//        override fun onTorchModeChanged(cameraId: String?, enabled: Boolean) {
//            super.onTorchModeChanged(cameraId, enabled)
//            println("onTorchModeChanged=====================$enabled")
//        }
//
//        override fun onTorchModeUnavailable(cameraId: String?) {
//            super.onTorchModeUnavailable(cameraId)
//            println("onTorchModeUnavailable========================")
//        }
//    }


    companion object {

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
}