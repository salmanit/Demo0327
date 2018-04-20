package com.charliesong.demo0327.util;

import android.content.Context;
import android.widget.ImageView;

import com.charliesong.demo0327.GlideApp;
import com.charliesong.demo0327.R;

/**
 * Created by charlie.song on 2018/4/9.
 */

public class UtilNormal {

    public static void loadCircle(Context context,String url, ImageView iv){

        GlideApp.with(context).load(url).circleCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher_round).into(iv);
    }
}
