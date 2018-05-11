package com.charliesong.demo0327

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v4.view.LayoutInflaterFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import java.util.*
import android.R.attr.name




/**
 * Created by charlie.song on 2018/3/30.
 */
open class  BaseActivity: AppCompatActivity() {


    var  toast: Toast?=null;
    fun showToast(msg:String){
        if(toast!=null){
            toast?.cancel();
        }
        toast=Toast.makeText(this,msg+"",Toast.LENGTH_SHORT);
        toast?.show();

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater,object :LayoutInflater.Factory2{
            override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet): View? {
                var view=delegate.createView(parent,name,context,attrs);
                if(view!=null&&view is TextView){
//                    view.setTextColor(Color.RED)
                }
//                println("name==" + name + "===" + attrs.idAttribute + "==" + attrs.classAttribute)
//                for (i in 0 until attrs.attributeCount) {
//                    val key = attrs.getAttributeName(i)
//
//                    if ("id".equals(key, ignoreCase = true)) {
//                        try {
//                            val value = attrs.getAttributeValue(i)
//                            println("id================$value")
//                        } catch (e: Exception) {
//
//                            e.printStackTrace()
//                        }
//
//                    } else {
//
//                        println("i=$i==name======$key")
//                    }
//                }
                return  view
            }

            override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
                return  null
            }
        })

//        LayoutInflaterCompat.setFactory(layoutInflater,object:LayoutInflaterFactory{
//            override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
//            return  null
//            }
//        })

        super.onCreate(savedInstanceState)

    }


    fun requestExternalStorage() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}