package com.charliesong.demo0327.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.support.v7.widget.AppCompatTextView
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * Created by charlie.song on 2018/3/30.
 */
open class  BaseActivity: AppCompatActivity() {


    fun defaultSetTitle(title:String){
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(title)
            setDisplayHomeAsUpEnabled(true)
        }
    }
    var  toast: Toast?=null;
    fun showToast(msg:String){
        if(toast!=null){
            toast?.cancel();
        }
        toast=Toast.makeText(this,msg+"",Toast.LENGTH_SHORT);
        toast?.show();

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater,object :LayoutInflater.Factory2{
            override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet): View? {
                var view=delegate.createView(parent,name,context,attrs);
//                println("onCreateView56=====$name===========${view}")
//                (0 until attrs.attributeCount).forEach {
//                    println("${name}=====$it==========${attrs.getAttributeName(it)}")
//                }
                return  view
            }

            override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
                return  null
            }
        })
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