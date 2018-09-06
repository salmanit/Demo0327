package com.charliesong.demo0327.app

import com.charliesong.demo0327.bean.*
import io.reactivex.Observable
import retrofit2.http.*

interface MyAPI {

    /**获取文章列表从0开始*/
    @GET("/article/list/{page}/json")
    fun getArticles(@Path("page") page: Int): Observable<BaseObjData<DataBean<ArticleBean>>>

    /**获取项目列表,从1开始*/
    @GET("/project/list/{page}/json")
    fun getProjects(@Path("page") page: Int, @Query("cid") cid: Int): Observable<BaseObjData<DataBean<ArticleBean>>>

    /**获取首页banner数据*/
    @GET("/banner/json")
    fun getBanner(): Observable<BaseObjData<List<BannerAlphaBean>>>

    /**获取体系目录*/
    @GET("/tree/json")
    fun getTree(): Observable<BaseObjData<List<TreeBean>>>

    /**体系下的文章列表*/
    @Headers("Cache-Control: max-age=60")
    @GET("/article/list/{page}/json")
    fun getTreeArticle(@Path("page") page: Int, @Query("cid") cid: Int):Observable<BaseObjData<DataBean<ArticleBean>>>

    /**如果问号后有多个参数，可以使用map，省事*/
//    @GET("/article/list/{page}/json")
//    fun getTreeArticle2(@Path("page") page: Int, @QueryMap map: HashMap<String, Int>): Call<BaseListData<ArticleBean>>


    /**项目分类*/
    @GET("/project/tree/json")
    fun getAllTreeChildren(): Observable<BaseObjData<List<TreeBean>>>

    /**登陆*/
    @POST("/user/login")
    fun login(@Query("username")username : String, @Query("password")password : String):Observable<BaseObjData<Any?>>

    /**注册*/
    @POST("/user/register")
    fun register(@Query("username")username : String, @Query("password")password : String, @Query("repassword")repassword : String):Observable<BaseObjData<Any?>>

    /**我的收藏列表*/
    @GET("/lg/collect/list/{page}/json")
    fun getMyCollections(@Path("page") page:Int):Observable<BaseObjData<DataBean<ArticleBean>>>

    /**添加站内收藏*/
    @POST("/lg/collect/{id}/json")
    fun addCollection(@Path("id")id:Int):Observable<BaseObjData<Any?>>
    /**取消收藏*/
    @POST("/lg/uncollect_originId/{id}/json")
    fun cancelCollection(@Path("id")id:Int):Observable<BaseObjData<Any?>>
    /**我的收藏页面取消取消收藏*/
    @POST("/lg/uncollect/{id}/json")
    fun cancelMyCollection(@Path("id")id:Int,@Query("originId") originId:Int):Observable<BaseObjData<Any?>>

    /**常用网站*/
    @GET("/friend/json")
    fun getWebsiteUrls():Observable<BaseObjData<List<WebsiteUrl>>>

    /**热搜词汇*/
    @GET("/hotkey/json")
    fun getHotWords():Observable<BaseObjData<List<SearchHotWord>>>

    /**搜索,keywords如果多个关键词，用逗号隔开*/
    @POST("/article/query/{page}/json")
    fun getSearchResultByWords(@Path("page") page:Int ,@Query("k") keywords:String):Observable<BaseObjData<DataBean<ArticleBean>>>

    /**导航数据*/
    @GET("/navi/json")
    fun getThirdNavigation():Observable<BaseObjData<List<ThirdNavigation>>>

    @GET("/novelApi")
    fun  getHotNovels():Observable<BaseBean<ArrayList<NovelBean>>>

    @GET("/weather_mini?citykey=101010100")
    fun getWeather():Observable<BaseBeanWeather<WeatherBean>>

    /** {"data":{"collectIds":[],"email":"","icon":"","id":7280,"password":"aaaaaa","type":0,"username":"12345678912"},"errorCode":0,"errorMsg":""}*/
    // {"data":{"collectIds":[],"email":"","icon":"","id":7280,"password":"aaaaaa","type":0,"username":"12345678912"},"errorCode":0,"errorMsg":""}
}