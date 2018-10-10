package com.charliesong.demo0327.camera

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.content.ContentUris
import android.os.Environment
import android.support.annotation.RequiresApi
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

object UriUtils {

    fun getFilePath(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getPath_above19(context, uri)
        } else {
            return getFilePath_below19(context, uri)
        }
    }

    fun getFilePath_below19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getPath_above19(context: Context, uri: Uri): String? {
        val pathHead = "file:///"
        // 1. DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 1.1 ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return pathHead + getDataColumn(context,
                        contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs)
            }// 1.3 MediaProvider
            // 1.2 DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) {//判断是否是google相册图片
                uri.lastPathSegment
            } else if (isGooglePlayPhotosUri(uri)) {//判断是否是Google相册图片
                getImageUrlWithAuthority(context, uri)
            } else {//其他类似于media这样的图片，和android4.4以下获取图片path方法类似
                getFilePath_below19(context, uri)
            }
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return pathHead + uri.path!!
        }// 3. 判断是否是文件形式 File
        // 2. MediaStore (and general)
        return ""
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        var path = ""
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            cursor = context.getContentResolver().query(uri, proj, selection, selectionArgs, null)
            cursor?.apply {
                //获得用户选择的图片的索引值
                val column_index = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                moveToFirst()
                //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
                path = getString(column_index)
            }
            println("p================$path")
        } catch (e: Exception) {

        } finally {
            cursor?.close()
        }
        return path
    }

    /**从相册中选择图片，如果手机安装了Google Photo，它的路径格式如下：
    content://com.google.android.apps.photos.contentprovider/0/1/mediakey%3A%2Flocal%253A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1754758324
    用原来的方式获取是不起作用的，path会是null，我们可以通过下面的形式获取*/
    fun getImageUrlWithAuthority(context: Context, uri: Uri): String? {
        var stream: InputStream? = null
        if (uri.authority != null) {
            try {
                stream= context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(stream)
                return writeToTempImageAndGetPathUri(context, bmp).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    stream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    fun writeToTempImageAndGetPathUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     */
    fun isGooglePlayPhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.contentprovider" == uri.authority
    }

}