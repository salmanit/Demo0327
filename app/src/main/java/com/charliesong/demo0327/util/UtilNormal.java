package com.charliesong.demo0327.util;

import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.charliesong.demo0327.R;
import com.charliesong.demo0327.base.GlideApp;
import com.charliesong.demo0327.base.MyApplication;

/**
 * Created by charlie.song on 2018/4/9.
 */

public class UtilNormal  {

    public static void loadCircleImage(String url, ImageView iv){

        GlideApp.with(MyApplication.myApplication).load(url).circleCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher_round).into(iv);
    }
}
