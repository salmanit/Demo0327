package com.charliesong.demo0327

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.charliesong.demo0327.article.ArticleActivityDetail
import com.charliesong.demo0327.util.UtilNormal
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {
var url="https://upload.jianshu.io/users/upload_avatars/8706116/0df51b97-56d2-4363-9d7f-ecd305b9722b.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96"
    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(if(Random().nextInt(10)>5) R.style.AppTheme else R.style.AppTheme2 )
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        rv.layoutManager = LinearLayoutManager(this)
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

                val typedValue=TypedValue();
                theme.resolveAttribute(R.attr.dif_item_nothing,typedValue,true)
                holder.getView<TextView>(R.id.tv_nothing).setTextColor(resources.getColor(typedValue.resourceId))

            }
        }
        getActivitys()
        requestExternalStorage()
//        UtilNormal.loadCircle(this,url,iv_test)
        MyApplication.loadCircleImage(url,iv_test)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
    fun getAdapter():BaseRvAdapter<ActivityInfo>{
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
