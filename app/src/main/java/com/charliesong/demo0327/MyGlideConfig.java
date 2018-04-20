package com.charliesong.demo0327;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.InputStream;

/**
 * Created by charlie.song on 2018/4/8.
 * 加上@GlideModule注解以便Glide识别
 */
@GlideModule
public class MyGlideConfig extends AppGlideModule {
    int diskCacheSize=500*1024*1024;
    private File diskCacheFolder=new File(Environment.getExternalStorageDirectory(),"demo0327");
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）
        //设置磁盘缓存保存在外部存储，且指定缓存大小
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, diskCacheSize));
        //设置磁盘缓存保存在自己指定的目录下，且指定缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(new DiskLruCacheFactory.CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return diskCacheFolder;
            }
        }, diskCacheSize));
        System.out.println("=====================apply options");
        //内存缓存配置（不建议配置，Glide会自动根据手机配置进行分配）
        //设置内存缓存大小
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        //设置Bitmap池大小
//        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSize));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        //替换组件,如下替换网络请求库
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
