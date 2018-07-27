package com.charliesong.demo0327.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.content.FileProvider
import java.io.File

object NormalUtil {

     fun installCheck(context: Context,file: File) {
        if (!file.exists()) {
            return
        }
        //下边这部分不一定需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.getPackageManager().canRequestPackageInstalls()) {

            } else {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return
            }
        }
        installApk(context, file)
    }

    //安装apk，兼容7.0
    private fun installApk(context: Context, file: File) {
        if (!file.exists()) {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        var uri: Uri
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 Provider主机地址和清单文件中保持一致   参数3 共享的文件
            uri = FileProvider.getUriForFile(context, "com.charlie.fileProvider", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        // intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        // 没有在Activity环境下启动Activity,设置下面的标签
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}