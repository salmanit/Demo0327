package com.charliesong.demo0327.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.widget.Toast
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.view.*
import android.widget.FrameLayout
import com.charliesong.demo0327.util.UtilNormal
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * Created by charlie.song on 2018/3/30.
 */
open class BaseActivity : AppCompatActivity() {

    var supportLeftEdge = true
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
//        findViewById<View>(android.R.id.content)?.fitsSystemWindows=true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        (window.decorView as FrameLayout).apply {
            var originalView = this.getChildAt(0)
            println("getbg===========${originalView.background}")
            originalView.setBackgroundColor(Color.WHITE)
            this.removeView(originalView)
            var addView = com.charliesong.demo0327.draghelper.LeftEdgeTouchCloseLayout(this@BaseActivity)
            addView.addView(originalView, originalView.layoutParams)
            this.addView(addView,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT)
        }
    }

    var needFits = false

    override fun onStart() {
        super.onStart()

        supportActionBar ?: if (needFits) fitsSystemWindwowState()
    }

    private fun fitsSystemWindwowState() {
        findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)?.fitsSystemWindows = true
    }

    fun changeStateColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = if (color <= 0) Color.TRANSPARENT else color
        } else {
            if (color > 0) {
                window.decorView
            }
        }
    }

    var statusHeight = 0
    fun getStatusHeight(context: Context): Int {
        if (statusHeight <= 0) {
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
                statusHeight = context.resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }


    fun defaultSetTitle(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(title)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    var toast: Toast? = null;
    fun showToast(msg: String) {
        if (toast != null) {
            toast?.cancel();
        }
        toast = Toast.makeText(this, msg + "", Toast.LENGTH_SHORT);
        toast?.show();

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet): View? {
                var view = delegate.createView(parent, name, context, attrs);
//                println("onCreateView56=====$name===========${view}")
//                (0 until attrs.attributeCount).forEach {
//                    println("${name}=====$it==========${attrs.getAttributeName(it)}")
//                }
                return view
            }

            override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
                return null
            }
        })
        super.onCreate(savedInstanceState)
//        UtilNormal.setCustomDensity(this,MyApplication.getInstance())
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