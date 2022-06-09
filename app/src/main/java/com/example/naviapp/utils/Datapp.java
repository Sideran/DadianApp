package com.example.naviapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.preference.PreferenceManager;

import com.amap.api.maps.model.LatLng;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import com.amap.api.maps2d.model.LatLng;


public class Datapp extends Application {
    private  Lock appLocks= new ReentrantLock();;
    private static final String TAG = "tag";
    private SharedPreferences sp;
    SharedPreferences sharedPreferences;
    boolean isLogin=false;
    boolean isLogining=false;
    private String userName;
    private String password;
    private String token;
    private String IPAddr;
    private String temptLocation;
    private String Direction;
    private String fileUrl;
    private String timeCode;
    private String nickName;
    private String deptName;
    private String dataScope;
    private String deptId;
    private String phoneNum;
    private String sex;
    private String email;
    private static Context context;
    private String userId;
    private Boolean isRefreshing;
    private LatLng latLng;
    private float TotalHeight;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userId",userId);
        editor.commit();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("sex",sex);
        editor.commit();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("phoneNum",phoneNum);
        editor.commit();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",email);
        editor.commit();
    }



    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("dataScope",dataScope);
        editor.commit();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("nickName",nickName);
        editor.commit();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("deptName",deptName);
        editor.commit();
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("deptId",deptId);
        editor.commit();
    }

    public  Lock getAppLocks() {
        return appLocks;
    }

    public boolean isLogining() {
        return isLogining;
    }

    public void setLogining(boolean logining) {
        isLogining = logining;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public Boolean getRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(Boolean refreshing) {
        isRefreshing = refreshing;
    }

    public String getTemptLocation() {
        return temptLocation;
    }

    public void setTemptLocation(String temptLocation) {
        this.temptLocation = temptLocation;
    }

    public static Context getContext() {
        return context;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("fileUrl",fileUrl);
        editor.commit();
    }

    public float getTotalHeight() {
        return TotalHeight;
    }

    public void setTotalHeight(float totalHeight) {
        TotalHeight = totalHeight;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",login);
        editor.commit();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName",userName);
        editor.commit();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password",password);
        editor.commit();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public String getIPAddr() {
        return IPAddr;
    }

    public void setIPAddr(String IPAddr) {
        this.IPAddr = IPAddr;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("IPAddr",IPAddr);
        editor.commit();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        sp = getSharedPreferences("naviinfo", MODE_PRIVATE);
        this.isLogin=sp.getBoolean("isLogin",false);
        this.userName=sp.getString("userName","userName");
        this.password=sp.getString("password","");
        this.token=sp.getString("token","");
        this.IPAddr=sp.getString("IPAddr","http://124.128.86.125:6060/prod-api");//http://172.17.33.30:8082
        this.context=getApplicationContext();
        nickName=sharedPreferences.getString("nickName","UserName");
        deptName=sharedPreferences.getString("deptName","Department");
        deptId=sharedPreferences.getString("deptId","100");
        phoneNum=sharedPreferences.getString("phoneNum","");
        email=sharedPreferences.getString("email","");
        sex=sharedPreferences.getString("sex","2");
        userId=sharedPreferences.getString("userId","");
        dataScope=sharedPreferences.getString("dataScope","0");
        this.isRefreshing=false;
        timeCode="";
        latLng=null;//new LatLng(0.00,0.00);
        TotalHeight=0.00f;
        fileUrl=sharedPreferences.getString("fileUrl",context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ "/PointPic");
        temptLocation="";
        Direction="";
    }

}
