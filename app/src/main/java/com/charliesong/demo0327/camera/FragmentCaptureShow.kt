package com.charliesong.demo0327.camera


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_capture_show.*
import java.io.File


class FragmentCaptureShow : BaseFragment() {
    override fun getLayoutID(): Int {
        return R.layout.fragment_capture_show
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var fileUri:Uri?=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 Provider主机地址和清单文件中保持一致   参数3 共享的文件
            fileUri = FileProvider.getUriForFile(activity!!, "com.charlie.fileProvider", picFile!!)
        } else{
            fileUri = Uri.fromFile(picFile)
        }
        iv_show.setImageURI(fileUri)
         fab_ok.setOnClickListener {
            activity?.apply {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("file",picFile)
                })
                finish()
            }
         }
        fab_cancel.setOnClickListener {
            picFile?.delete()
           parentFragment?.childFragmentManager?.popBackStack()
        }
    }

     var picFile:File?=null
}
