package com.charliesong.demo0327.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.charliesong.demo0327.R;
import com.charliesong.demo0327.base.GlideApp;
import com.charliesong.demo0327.base.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by charlie.song on 2018/4/9.
 */

public class UtilNormal  {

    public static void loadCircleImage(String url, ImageView iv){

        GlideApp.with(MyApplication.myApplication).load(url).circleCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher_round).into(iv);
    }


    public static void saveWords(String msg){
        saveWords(msg,"demo0327.txt",true);
    }
    public static void saveWords(String msg,String fileName,boolean append){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        File file=new File(Environment.getExternalStorageDirectory(),fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            FileOutputStream fos=new FileOutputStream(file,append);
            fos.write((msg+"\n").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUUID(Context context,String uuid){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("my_uuid",uuid).commit();
    }
    public static String getUUID(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context).getString("my_uuid","");
    }
    public static void getString(Context context,String key){
        PreferenceManager.getDefaultSharedPreferences(context).getString(key,"");
    }


    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;
    private static float defaultUIWidth=768f;//ui做图用到的宽度
    //此方法是以宽度来处理的
    public static void setCustomDensity(Activity activity, final Application application){

        DisplayMetrics appDisplayMetrics=application.getResources().getDisplayMetrics();

        if(sNonCompatDensity==0){
            sNonCompatDensity=appDisplayMetrics.density;
            sNonCompatScaledDensity=appDisplayMetrics.scaledDensity;

            application.registerComponentCallbacks(new ComponentCallbacks(){


                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if(newConfig!=null&&newConfig.fontScale>0){
                        sNonCompatScaledDensity=application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //这个需要固定屏幕方向的，因为屏幕旋转以后获取的宽和高交换了，或者自己根据屏幕方向这里东西处理是width还是高
       boolean portrait= activity.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT;
        float targetDensity=appDisplayMetrics.widthPixels/defaultUIWidth;
        if(!portrait){
            targetDensity=appDisplayMetrics.heightPixels/defaultUIWidth;
        }
        float targetScaledDensity=targetDensity*(sNonCompatScaledDensity/sNonCompatDensity);
        int targetDensityDpi= (int) (targetDensity*160);

        appDisplayMetrics.density=targetDensity;
        appDisplayMetrics.scaledDensity=targetScaledDensity;
        appDisplayMetrics.densityDpi=targetDensityDpi;


        DisplayMetrics activityDisplayMetrics=activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density=targetDensity;
        activityDisplayMetrics.scaledDensity=targetScaledDensity;
        activityDisplayMetrics.densityDpi=targetDensityDpi;

    }
}
