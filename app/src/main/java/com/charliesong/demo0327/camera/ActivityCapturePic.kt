package com.charliesong.demo0327.camera

import android.app.Activity
import android.os.Bundle
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.activity_capture_pic.*
import java.io.File
import android.graphics.Bitmap
import android.view.View
import android.media.ExifInterface
import android.os.Build
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility




class ActivityCapturePic : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_pic)
        defaultSetTitle("camera")
        requestCamera()
    }
    companion object {
        private val REQUEST_SAVE_TO_FILE = 123//将图片保存在我们提供的file里
        private val REQUEST_RETURN_BITMAP_DATA = 124//返回bitmap的data数据
    }
    var fileUri: Uri? = null;
    var mPhotoFile: File? = null
    /**save picture to our provided file*/
    fun startCamera(v: View) {
        mPhotoFile = File(getExternalFilesDir("externalfilespath"), "${System.currentTimeMillis()}.jpg")
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 Provider主机地址和清单文件中保持一致   参数3 共享的文件
            fileUri = FileProvider.getUriForFile(baseContext, "com.charlie.fileProvider", mPhotoFile!!)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else{
            fileUri = Uri.fromFile(mPhotoFile)
        }

        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(captureIntent, REQUEST_SAVE_TO_FILE)
    }

    /**return bitmap data*/
    fun startCamera2(v: View) {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra("crop", "true")
//        intent.putExtra("aspectX", 2)
//        intent.putExtra("aspectY", 1)
//        intent.putExtra("outputX", windowManager.defaultDisplay.width)
//        intent.putExtra("outputY", windowManager.defaultDisplay.height)
//        intent.putExtra("scale", true)
//        intent.putExtra("return-data", true)//这句就是用来返回data的
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
//        intent.putExtra("noFaceDetection", true) // no face detection
        startActivityForResult(intent, REQUEST_RETURN_BITMAP_DATA)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("request=====$requestCode====result=${resultCode}=====${data}")
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_SAVE_TO_FILE -> {
                iv_result.setImageURI(fileUri)
                mPhotoFile?.absolutePath?.apply {
                    addGPSInfo(this)
                }
                println("file==========${mPhotoFile?.absolutePath}")
                uploadToServer()
            }
            REQUEST_RETURN_BITMAP_DATA -> {
                //  content://media/external/images/media/98430
                data?.apply {
                    val bitmap2 = data.getParcelableExtra("data") as Bitmap?
                    iv_result.setImageBitmap(bitmap2)
                    data.data?.apply {
                        val filePath=UriUtils.getFilePath(baseContext,this)
                        filePath?.apply {
                            println("file path===============${filePath}")
                            addGPSInfo(filePath)
                        }
                    }

                }

            }
        }

    }
    private fun uploadToServer(){
        IdentityManager.setDefaultIdentityManager(IdentityManager(this))
        AWSMobileClient.getInstance().initialize(this).execute();
        uploadWithTransferUtility();
    }

    fun uploadWithTransferUtility() {

        val transferUtility = TransferUtility.builder()
                .context(applicationContext)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                .build()

        val uploadObserver = transferUtility.upload(
                "public/"+mPhotoFile?.name,
                File(mPhotoFile?.absolutePath))
        println("===========================130")
        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (TransferState.COMPLETED === state) {
                    // Handle a completed upload.
                }
                println("state=============$state")
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                val percentDone = percentDonef.toInt()

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%")
            }

            override fun onError(id: Int, ex: Exception) {
                // Handle errors
                println("$id================${ex.message}")
            }

        })

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
//        if (TransferState.COMPLETED === uploadObserver.state) {
//            // Handle a completed upload.
//        }

        Log.d("YourActivity", "Bytes Transferred: " + uploadObserver.bytesTransferred)
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.bytesTotal)
    }
    private fun addGPSInfo(path:String){
        val exif = ExifInterface(path)//根据文件完整名字获取一个exif实例
        val longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
        val latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)) {
            val jd = 123.456789
            val wd = 33.987654321
            val longitude = gpsInfoConvert(jd)
            val latitude = gpsInfoConvert(wd)
            //设置一下4个gps相关属性
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, if (jd > 0) "E" else "W")
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, if (wd > 0) "N" else "S")
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
//            exif.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD,"manual")
            exif.saveAttributes();//保存属性到文件
        } else {
            println("==========$longitude======$latitude")
        }
        println("method==============${exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)}")
    }
    fun gpsInfoConvert(coordinate: Double): String {
        var coordinate = coordinate
        if (coordinate < -180.0 || coordinate > 180.0 ||
                java.lang.Double.isNaN(coordinate)) {
            throw IllegalArgumentException("coordinate=$coordinate")
        }
        val sb = StringBuilder()
        if (coordinate < 0) {//符号位丢掉
            coordinate = -coordinate
        }
        val degrees = Math.floor(coordinate).toInt()
        sb.append(degrees)
        sb.append("/1,")
        coordinate -= degrees.toDouble()
        coordinate *= 60.0
        val minutes = Math.floor(coordinate).toInt()
        sb.append(minutes)
        sb.append("/1,")
        coordinate -= minutes.toDouble()
        coordinate *= 60.0
        sb.append(Math.floor(coordinate).toInt())
        sb.append("/1")
        return sb.toString()
    }

}
