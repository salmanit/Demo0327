package com.charliesong.demo0327.util;

import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
}
