package com.example.naviapp.paging3.api

import android.content.Intent
import android.icu.util.TimeUnit
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.example.naviapp.entity.RetMessage
import com.example.naviapp.entity.UserInfoEntity
import com.example.naviapp.paging3.model.PointInfo
import com.example.naviapp.paging3.model.PointInfoRespo
import com.example.naviapp.ui.login.LoginActivity
import com.example.naviapp.utils.Datapp
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.HashMap

interface GetPointPage {
    @GET("RecordCamera/attribute/android/facilitylist")
    suspend fun searchRepos(@Query("pageNum") page: Int,
                            @Query("pageSize") perPage: Int,
                            @QueryMap point: HashMap<String,String>,
                            @Header("Authorization") token:String): PointInfoRespo

//    @GET("RecordCamera/attribute/android")
//    suspend fun getPageInfo(@Header("Authorization")token: String,@Query("deviceid")deviceid:String):PointInfo
    @Multipart
    @POST("RecordCamera/attribute/android")
    suspend fun uploadPoint(@Header("Authorization")token: String,
                            @QueryMap jsonObject:JSONObject,
                            @Part pic:MultipartBody.Part):JsonObject

    @GET("RecordCamera/attribute/android/tree")
    suspend fun getDeptTree(@Header("Authorization") token:String): JsonObject

    @GET("RecordCamera/dict/datatype")
    suspend fun getDict(@Header("Authorization")token: String,@Query("dataType")dataType:String):JsonObject

    @GET("system/dept/list")
    suspend fun getDeptList(@Header("Authorization")token: String,@Query("deptId")deptId:String): JsonObject

    @PUT("system/user/profile")
    suspend fun modifyInfo(@Header("Authorization")token: String,@Body user:UserInfoEntity): JsonObject

    @PUT("system/user/profile/updatePwd")
    suspend fun modifyPwd(
        @Header("Authorization") token: String,
        @Query("oldPassword") oldPassword: String,
        @Query("newPassword") newPassword: String
    ): JsonObject

    companion object {
        //        private const val BASE_URL = "https://api.github.com/"

        fun create(): GetPointPage {
            val app=Datapp.getContext() as Datapp;
            var url =app.ipAddr;
            if(!url.lowercase().endsWith("/")) url= "$url/"
            val client:OkHttpClient = OkHttpClient.Builder().addInterceptor(TInterceptor).build()

            return Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GetPointPage::class.java)
        }

    }
    object TInterceptor:Interceptor{
        val app=Datapp.getContext() as Datapp
        var isRefreshing:Boolean=false
        //private val locks: Lock = ReentrantLock()
        override fun intercept(chain: Interceptor.Chain): Response {
            val request=chain.request()
                    .newBuilder().build()
//                    .header("Authorization","Bearer"+app.token)
            val response = chain.proceed(request)
            val bodyString= response.body()?.string()
            try {
                val retMessage= JSON.parseObject(bodyString,RetMessage::class.java)
                if (app.appLocks.tryLock()) {
                    try {
                        if ((retMessage.code == 401) && (!app.isLogining)) {
                            app.isLogining = true
                            app.isLogin = false
                            app.token = ""
                            var intent = Intent(Datapp.getContext(), LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            Datapp.getContext().startActivity(intent)
                        }
                    }catch (e:Exception ){
                        e.printStackTrace()
                    } finally {
                        app.appLocks.unlock()// 即使出现异常或错误也能释放锁
                    }
                }else{

                }
            }catch (e:Exception ){
                e.printStackTrace()
            }
            val contentType: MediaType?=response.body()?.contentType()
            val  wrappeBody=ResponseBody.create(contentType,bodyString)
            return response.newBuilder().body(wrappeBody).build()
        }

//        @Synchronized
//        @Throws(InterruptedException::class)
//        fun getRefreshToken() {
//            if (!isRefreshing) {
//                isRefreshing = true
//                Log.e("refreshToken", "Refreshing token...")
//                tokenContainer.getRefreshToken {
//                    synchronized(this@TInterceptor) {
//                        isRefreshing = false
//                        Log.e("refreshToken", "Proto response on success and token updated with token -> " + tokenContainer.getToken())
//                        this@TInterceptor.notifyAll()
//                    }
//                }
//            }
//            Log.e("refreshToken", "lock thread -> " + Process.getThreadPriority(Process.myTid()) + this.toString())
//            this.wait()
//            Log.e("refreshToken", "unlock thread -> " + Process.getThreadPriority(Process.myTid()) + this.toString())
//        }
    }


}