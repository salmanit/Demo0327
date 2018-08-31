package com.charliesong.demo0327.app

import com.charliesong.demo0327.BuildConfig
import com.charliesong.demo0327.util.NormalUtil
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit


object MyAPIManager {

    private val host = "http://www.wanandroid.com"
    private fun getRetrofit(): Retrofit {
        //缓存容量
        val SIZE_OF_CACHE = (50 * 1024 * 1024).toLong() // 10 MiB
        //缓存路径
        val cacheFile = "${MyApplication.getInstance().getCacheDir()}/http"
        val cache = Cache(File(cacheFile), SIZE_OF_CACHE)
        // Cookie 持久化
//        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(InitApp.AppContext))
        //利用okhttp实现缓存
        val clientBuilder = OkHttpClient.Builder()
//                .cookieJar(CookieJarImpl(PersistentCookieStore(MyApplication.getApp())))
//                //有网络时的拦截器
                .addNetworkInterceptor(rewrite_response_interceptor)
//                //没网络时的拦截器
                .addInterceptor(rewrite_response_interceptor_offline)
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(interceptor)
        }
        return Retrofit.Builder().baseUrl(host)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create()) //结果解析为gson对象用这个
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //如果要用rx接口返回Observable加上这个
                .build()
    }

    private var myAPI: MyAPI? = null
    fun getAPI(): MyAPI {
        return myAPI
                ?: getRetrofit().create(MyAPI::class.java)
    }

    private val rewrite_response_interceptor = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())

        //如果接口里写了@Headers("Cache-Control: max-age=60")缓存时间，那么可以用下边的代码把接口的缓存时间添加到相应的结果里。
//        var requestCacheControl = chain.request().header("Cache-Control")
//        var haveSetCacheTime = requestCacheControl != null && (requestCacheControl.contains("max-age"))
//        if (haveSetCacheTime) {
//            return@Interceptor originalResponse.newBuilder()
//                    .addHeader("Cache-Control",requestCacheControl)
//                    .removeHeader("Pragma").build()
//        }
        val cacheControl = originalResponse.header("Cache-Control")
        //这里判断的都是服务器返回的缓存条件
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            var build = originalResponse.newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + 5)//单位是秒，就是5秒内不请求服务器
//                    .addHeader("Accept-Encoding","identity")
//                    .addHeader("Connection", "close")//27以上的模拟器返回数据一直失败，搜索这些方法都没用
            build.build()
        } else {
            originalResponse
        }
    }

    private val rewrite_response_interceptor_offline = Interceptor { chain ->
        var request = chain.request()
        if (!NormalUtil.isNetworkConnected(MyApplication.getInstance())) {
            request = request.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached")//这个不靠谱，没网络的时候不加载缓存
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        var originalResponse = chain.proceed(request)
        if (NormalUtil.isNetworkConnected(MyApplication.getInstance())) {
//            val cacheControl = request.cacheControl().toString()
//            originalResponse.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + 0)//单位是秒
//                    .removeHeader("Pragma")
//                    .build()
            originalResponse
        } else {
            val maxTime = 4 * 24 * 60 * 60
            originalResponse.newBuilder()
                    //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxTime")
                    .removeHeader("Pragma")
                    .build()

        }
    }

}