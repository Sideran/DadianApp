package com.example.naviapp.ui.home;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.entity.PostEntity;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.UtilHttp;

import java.io.IOException;
import java.util.HashMap;

public class HomeViewModel extends ViewModel {
    private Datapp app;
    private final MutableLiveData<String> mText;
    private MutableLiveData<RetMessage> retMessage = new MutableLiveData<>();
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    LiveData<RetMessage> getRetMessage() {
        return retMessage;
    }
    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<RetMessage> getRetPoint(){return retpoint;}
    //增加的
    private MutableLiveData<Integer> LikedNumber;
    private MutableLiveData<RetMessage> retpoint= new MutableLiveData<>();
    public MutableLiveData<Integer> getLikedNumber() {
        if(LikedNumber ==null)
        {
            LikedNumber = new MutableLiveData<>();
            LikedNumber.setValue(0);
        }
        return LikedNumber;
    }
    public void addLikedNumber(int n)
    {
        LikedNumber.setValue(LikedNumber.getValue()+n);
    }

    public void postPost(Context context,String postCode,String postName) {
        // TODO: revoke authentication
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        PostEntity postEntity=new PostEntity(postCode,postName,0,"0");
        String jsPost= JSON.toJSONString(postEntity);
        RetMessage ret;
        try {
            ret = UtilHttp.post(context,"http://"+app.getIPAddr()+"/system/post", jsPost);
            if (ret!=null){
                retMessage.postValue(ret);
            }
        } catch (Exception e) {
            ret=new RetMessage();
            ret.setMsg(e.getMessage());
            ret.setCode(-1);
            retMessage.postValue(ret);
        }
    }

    public void getNearPoint(Context context, HashMap<String,String> map){
        AppCompatActivity activity=(AppCompatActivity)context;
        app=(Datapp)activity.getApplication();
        try {
            RetMessage ret=UtilHttp.getParam(context,app.getIPAddr()+"/RecordCamera/attribute/android/latlon",map);

            retpoint.postValue(ret);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}