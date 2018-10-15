package com.charliesong.demo0327.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_camera2.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Long
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FragmentCamera2 : BaseFragment() {


    override fun getLayoutID(): Int {
        return R.layout.fragment_camera2
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iv_close.setOnClickListener {
            activity?.finish();
        }
        iv_switchCamera.setOnClickListener {
            println("click==============switch")
            switchCamera()
        }
        iv_takePhoto.setOnClickListener {
            capture();
        }

    }

    private fun capture() {
        cameraDevice?:return
       val previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                CameraDevice.TEMPLATE_STILL_CAPTURE
        ).apply {
           addTarget(imageReader?.surface)
           set(CaptureRequest.CONTROL_AF_TRIGGER,
                   CameraMetadata.CONTROL_AF_TRIGGER_START)
           setAutoFlash(this)
       }
        val captureCallback = object : CameraCaptureSession.CaptureCallback() {

            override fun onCaptureCompleted(session: CameraCaptureSession,
                                            request: CaptureRequest,
                                            result: TotalCaptureResult) {
                println("======================capture completed")
                unlockFocus()//重新开始预览，具体的要干嘛可以实际处理
            }
        }

        captureSession?.apply {
            stopRepeating()//停掉预览
            capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);//capture只会拍摄一次，不会不停的请求数据
        };
    }

    private fun unlockFocus() {
        try {
            captureSession?.setRepeatingRequest(previewRequest, null,
                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }

    }


    private fun switchCamera() {
        if (mCameraIdLists.isEmpty()) {
            return
        }
        cameraIdIndex = (cameraIdIndex + 1) % mCameraIdLists.size
        cameraDevice?.close()
        openCamera(ttv.width, ttv.height)
    }


    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        println("onResume======================${ttv.isAvailable}")
        if (ttv.isAvailable) {
            openCamera(ttv.width, ttv.height)
        } else {
            ttv.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }


    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit

    }


    private fun openCamera(width: Int, height: Int) {
        val permission = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
//            requestCameraPermission()
            return
        }
        val manager = activity?.applicationContext?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        manager ?: return
        mCameraIdLists = manager.cameraIdList;
        if (mCameraIdLists.isEmpty()) {
            return
        }
        iv_switchCamera.visibility = if (mCameraIdLists.size > 1) View.VISIBLE else View.INVISIBLE
        setUpCameraOutputs(width, height)
        configureTransform(width, height)
        try {
            // Wait for camera to open - 2.5 seconds is sufficient
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(mCameraIdLists[cameraIdIndex], stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }

    }

    var mCameraIdLists = arrayOf<String>()
    private var imageReader: ImageReader? = null
    /**
     * Orientation of the camera sensor
     */
    private var sensorOrientation = 0

    private lateinit var previewSize: Size
    private var flashSupported = false

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private fun setUpCameraOutputs(width: Int, height: Int) {
        val manager = activity?.applicationContext?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val characteristics = manager.getCameraCharacteristics(mCameraIdLists[cameraIdIndex])

            val map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return

            // For still image captures, we use the largest available size.
            val largest = Collections.max(
                    Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
                    CompareSizesByArea())
            imageReader = ImageReader.newInstance(largest.width, largest.height,
                    ImageFormat.JPEG, /*maxImages*/ 2).apply {
                setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
                    override fun onImageAvailable(reader: ImageReader?) {
                        savePic()
                    }
                }, backgroundHandler)
            }

            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            val displayRotation = activity?.windowManager?.defaultDisplay?.rotation ?: 0

            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
            val swappedDimensions = areDimensionsSwapped(displayRotation)

            val displaySize = Point()
            activity!!.windowManager.defaultDisplay.getSize(displaySize)
            val rotatedPreviewWidth = if (swappedDimensions) height else width
            val rotatedPreviewHeight = if (swappedDimensions) width else height
            var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
            var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH
            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) maxPreviewHeight = MAX_PREVIEW_HEIGHT

            // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
            // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
            // garbage capture data.
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                    rotatedPreviewWidth, rotatedPreviewHeight,
                    maxPreviewWidth, maxPreviewHeight,
                    largest)

            // We fit the aspect ratio of TextureView to the size of preview we picked.
//                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    ttv.setAspectRatio(previewSize.width, previewSize.height)
//                } else {
//                    ttv.setAspectRatio(previewSize.height, previewSize.width)
//                }

            // Check if the flash is supported.
            flashSupported =
                    characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

            // We've found a viable camera and finished setting up member variables,
            // so we don't need to iterate through other available cameras.
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: Exception) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            println("setUpCameraOutputs========================error")
        }

    }


    private fun areDimensionsSwapped(displayRotation: Int): Boolean {
        var swappedDimensions = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
            }
            else -> {
                Log.e(TAG, "Display rotation is invalid: $displayRotation")
            }
        }
        return swappedDimensions
    }


    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        activity ?: return
        val rotation = activity!!.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            val scale = Math.max(
                    viewHeight.toFloat() / previewSize.height,
                    viewWidth.toFloat() / previewSize.width)
            with(matrix) {
                setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                postScale(scale, scale, centerX, centerY)
                postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            }
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        ttv.setTransform(matrix)
    }


    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            Log.i(TAG, "StateCallback onOpened====================")
            cameraOpenCloseLock.release()
            this@FragmentCamera2.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            Log.i(TAG, "StateCallback onDisconnected====================")
            cameraOpenCloseLock.release()
            cameraDevice.close()
            this@FragmentCamera2.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            Log.i(TAG, "StateCallback onError==================$error")
            onDisconnected(cameraDevice)
            this@FragmentCamera2.activity?.finish()
        }

    }
    private var previewRequestBuilder: CaptureRequest.Builder? = null

    private var captureSession: CameraCaptureSession? = null
    private var previewRequest: CaptureRequest? = null

    private fun createCameraPreviewSession() {
        try {
            val texture = ttv.surfaceTexture

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)

            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
            )
            previewRequestBuilder?.addTarget(surface)

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (cameraDevice == null) return

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(previewRequestBuilder!!)

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder!!.build()
                                captureSession?.setRepeatingRequest(previewRequest,
                                        null, backgroundHandler)
                            } catch (e: CameraAccessException) {
                                Log.e(TAG, e.toString())
                            }

                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            println("createCaptureSession onConfigureFailed==================")
                        }
                    }, null)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }

    }

    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
        if (flashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
        }
    }

    private var cameraDevice: CameraDevice? = null

    private val cameraOpenCloseLock = Semaphore(1)

    private var cameraIdIndex = 0

    private val TAG = this::class.java.name
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private var backgroundThread: HandlerThread? = null

    /**
     * A [Handler] for running tasks in the background.
     */
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
            Log.e(TAG, e.toString())
        }

    }

    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
            imageReader?.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }
    val simpleDateFormat= SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    private fun savePic() {

        imageReader?.acquireNextImage()?.apply {
            val buffer = this.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            var output: FileOutputStream? = null
            try {
                val file= File(Environment.getExternalStorageDirectory(),"${simpleDateFormat.format(Date(System.currentTimeMillis()))}.jpg")
                output = FileOutputStream(file).apply {
                    write(bytes)
                }
                println("=======================save picture==${file.absolutePath}===${file.length()}")

                activity?.apply {
                    runOnUiThread {
                        this@FragmentCamera2.childFragmentManager.beginTransaction().replace(R.id.camera_root,FragmentCaptureShow().apply {
                        picFile=file
                        },"show...").addToBackStack("...").commit() }
                    }

            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            } finally {
                this.close()
                output?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        Log.e(TAG, e.toString())
                    }
                }
            }
        }?:return

    }

    companion object {

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        /**
         * Camera state: Showing camera preview.
         */
        private val STATE_PREVIEW = 0

        /**
         * Camera state: Waiting for the focus to be locked.
         */
        private val STATE_WAITING_LOCK = 1

        /**
         * Camera state: Waiting for the exposure to be precapture state.
         */
        private val STATE_WAITING_PRECAPTURE = 2

        /**
         * Camera state: Waiting for the exposure state to be something other than precapture.
         */
        private val STATE_WAITING_NON_PRECAPTURE = 3

        /**
         * Camera state: Picture was taken.
         */
        private val STATE_PICTURE_TAKEN = 4

        /**
         * Max preview width that is guaranteed by Camera2 API
         */
        private val MAX_PREVIEW_WIDTH = 1920

        /**
         * Max preview height that is guaranteed by Camera2 API
         */
        private val MAX_PREVIEW_HEIGHT = 1080

        @JvmStatic
        private fun chooseOptimalSize(
                choices: Array<Size>,
                textureViewWidth: Int,
                textureViewHeight: Int,
                maxWidth: Int,
                maxHeight: Int,
                aspectRatio: Size
        ): Size {

            // Collect the supported resolutions that are at least as big as the preview Surface
            val bigEnough = ArrayList<Size>()
            // Collect the supported resolutions that are smaller than the preview Surface
            val notBigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth && option.height <= maxHeight &&
                        option.height == option.width * h / w) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the
            // largest of those not big enough.
            if (bigEnough.size > 0) {
                return Collections.min(bigEnough, CompareSizesByArea())
            } else if (notBigEnough.size > 0) {
                return Collections.max(notBigEnough, CompareSizesByArea())
            } else {
                Log.e("===", "Couldn't find any suitable preview size")
                return choices[0]
            }
        }
    }


    internal class CompareSizesByArea : Comparator<Size> {

        // We cast here to ensure the multiplications won't overflow
        override fun compare(lhs: Size, rhs: Size) =
                Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

    }


}