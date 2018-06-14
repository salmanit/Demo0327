package com.charliesong.demo0327.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.charliesong.demo0327.R
import com.charliesong.demo0327.util.UtilNormal
import com.charliesong.demo0327.article.ArticleActivityDetail
import com.charliesong.demo0327.kt.Student
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
var url="https://upload.jianshu.io/users/upload_avatars/8706116/0df51b97-56d2-4363-9d7f-ecd305b9722b.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(if(Random().nextInt(10)>5) R.style.AppTheme else R.style.AppTheme2 )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(object :RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect.bottom=5
            }
            var width=0
            var paint:Paint=Paint()
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                if(width==0){
                    paint.color=Color.LTGRAY
                    width=rv.measuredWidth
                }
                var childCount=parent.childCount
                for(i in 0 until childCount){
                    var child=parent.getChildAt(i)
                    if(child!=null){
                      var holder=  parent.getChildViewHolder(child)
                        var top=holder.itemView.bottom*1f
                        var bottom=top+5
                        c.drawRect(0f,top,1208f,bottom,paint)
                    }
                }
            }
        })
        rv.adapter = object : BaseRvAdapter<ActivityInfo>() {
            override fun getLayoutID(viewType: Int): Int {
                return R.layout.item_main_activity
            }

            override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                var activityInfo=getItemData(position)
                var des=if(activityInfo.labelRes==0){ if(activityInfo.nonLocalizedLabel==null)"大哥，你忘了给activity设置lable" else activityInfo.nonLocalizedLabel} else getString(activityInfo.labelRes)
                holder.setText(R.id.tv_des,des)
                holder.setText(R.id.tv_cla,activityInfo.name)
                holder.itemView.setOnClickListener { startActivity(Intent().setClassName(packageName,activityInfo.name)) }
                var iv_wifi=holder.getView<ImageView>(R.id.iv_wifi);
                iv_wifi.setImageLevel(position%5)

//                val typedValue=TypedValue();
//                theme.resolveAttribute(R.attr.dif_item_nothing,typedValue,true)
//                holder.getView<TextView>(R.id.tv_nothing).setTextColor(resources.getColor(typedValue.resourceId))

                holder.getView<TextView>(R.id.tv_nothing).apply {
                    setText("$position")
                }
            }
        }

        hello.also {  }
        getActivitys()
        requestExternalStorage()
//        UtilNormal.loadCircle(this,url,iv_test)
        UtilNormal.loadCircleImage(url, iv_test)
    }
    var hello:Student?=null;
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
    fun getAdapter(): BaseRvAdapter<ActivityInfo> {
        return  rv.adapter as BaseRvAdapter<ActivityInfo>
    }

    fun getActivitys(){
        Observable.create<List<ActivityInfo>> {
            var packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            var result=packageInfo.activities.filter { return@filter !TextUtils.equals(MainActivity::class.java.name,it.name)
            &&!TextUtils.equals(ArticleActivityDetail::class.java.name,it.name)}.reversed()
            it.onNext(result)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getAdapter().initData(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("night")
        menu.add("light")
        return true
//        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "night"->{
//                delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                setTheme(R.style.AppTheme2)
            }
            "light"->{
//                delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                setTheme(R.style.AppTheme)
            }
        }
        recreate()
        return super.onOptionsItemSelected(item)
    }
}
