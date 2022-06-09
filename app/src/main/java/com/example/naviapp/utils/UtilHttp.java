package com.example.naviapp.utils;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.ui.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

public  class UtilHttp {
    public static void checkLogin(Context context){
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)(aa.getApplication());
        if (app.getAppLocks().tryLock()){
            try{
                if (!app.isLogining()) {
                    app.setLogining(true);
                    app.setLogin(false);
                    app.setToken("");
                    Intent i = new Intent(context, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            } finally {
                app.getAppLocks().unlock(); // 即使出现异常或错误也能释放锁
            }
        }
    }
    public static  RetMessage get(Context context,String url)  throws IOException{
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)(aa.getApplication());

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + app.getToken())
                .get()
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);

        if (retMessage.getCode()==401){
            checkLogin(context);
//            app.setLogin(false);
//            app.setToken("");
//            //app.setUserName("");
//            Intent i = new Intent(context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(i);
        }
        return retMessage;
    }

    public static  RetMessage getParam(Context context, String url, Map<String, String> params)  throws IOException{
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        final Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("Authorization","Bearer " + app.getToken())
                .get()
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);

        if (retMessage.getCode()==401){
            checkLogin(context);
//            app.setLogining(true);
//            app.setLogin(false);
//            app.setToken("");
//            //app.setUserName("");
//            Intent i = new Intent(context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(i);
        }
        return retMessage;
    }
    public static  RetMessage post(String url, String jsBody)  throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsBody);
        //config.headers['Authorization'] = 'Bearer ' + getToken()
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)//添加post请求
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);
        return retMessage;
    }
    public static  RetMessage post(Context context,String url, String jsBody)  throws IOException{
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsBody);
        //config.headers['Authorization'] = 'Bearer ' + getToken()
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + app.getToken())
                .post(requestBody)//添加post请求
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);
        if (retMessage.getCode()==401){
            checkLogin(context);
//            app.setLogin(false);
//            app.setToken("");
//            //app.setUserName("");
//            Intent i = new Intent(context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(i);
        }
        return retMessage;
    }

    public static  RetMessage put(Context context,String url, String jsBody)  throws IOException{
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsBody);
        //config.headers['Authorization'] = 'Bearer ' + getToken()
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + app.getToken())
                .put(requestBody)//添加post请求
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);
        if (retMessage.getCode()==401){
            checkLogin(context);
//            app.setLogin(false);
//            app.setToken("");
//            //app.setUserName("");
//            Intent i = new Intent(context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(i);
        }
        return retMessage;
    }
    public static  RetMessage putfile(Context context,String url, File file,String facilityId)  throws Exception{
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("facilityId", facilityId)                //post请求Key,value
                .addFormDataPart("picfile", file.getName(),requestBody)     //post请求Key,value
                .build();

        //config.headers['Authorization'] = 'Bearer ' + getToken()
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + app.getToken())
                .put(body)//添加post请求
                .build();
        final Response response = okHttpClient.newCall(request).execute();
        final String resultData = response.body().string();
        RetMessage retMessage= JSON.parseObject(resultData, RetMessage.class);//.parseArray(users,User.class);
        if (retMessage.getCode()==401){
            checkLogin(context);
        }
        return retMessage;
    }

}
