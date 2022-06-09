package com.example.naviapp.activity;

import android.content.Context;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.naviapp.entity.DictEntity;
import com.example.naviapp.entity.PointEntity;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;
import com.example.naviapp.utils.UtilHttp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollingViewModel extends ViewModel {
    private MutableLiveData<RetMessage> retMessage=new MutableLiveData<>();
    private MutableLiveData<RetMessage> retPictrue=new MutableLiveData<>();
    private MutableLiveData<RetMessage> getPictrue=new MutableLiveData<>();
    private MutableLiveData<PointEntity> livepointInfo=new MutableLiveData<>();
    private MutableLiveData<List<DictEntity>> dictm=new MutableLiveData<>();
    private MutableLiveData<List<DictEntity>> dictu=new MutableLiveData<>();
    private MutableLiveData<List<DictEntity>> dictp=new MutableLiveData<>();
    private MutableLiveData<List<DictEntity>> dictpos=new MutableLiveData<>();
    private MutableLiveData<RetMessage> deptRet=new MutableLiveData<>();
    public LiveData<RetMessage> getDeptRet(){return deptRet;}
    public LiveData<List<DictEntity>> getDictm(){return dictm;}
    public LiveData<List<DictEntity>> getDictu(){return dictu;}
    public LiveData<List<DictEntity>> getDictp(){return dictp;}
    public LiveData<List<DictEntity>> getDictPos(){return dictpos;}
    public LiveData<RetMessage> getPic(){return getPictrue;}
    public LiveData<RetMessage> getRetMessage(){return retMessage;}
    public LiveData<RetMessage> getRetPictrue(){return retPictrue;}
    public LiveData<PointEntity>  getPointInfo(){return livepointInfo;}
    private Datapp app;
    public void getInfo(Context context,String facilityId){
        AppCompatActivity activity=(AppCompatActivity)context;
        app=(Datapp)activity.getApplication();
        try {
            RetMessage ret= UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/attribute/android/facility/"+facilityId);
            if(ret.getCode()==200){
                PointEntity pointInfo= JSON.parseObject(ret.getData(), PointEntity.class);
                livepointInfo.postValue(pointInfo);

            }else {
                Util.showToast(context,"未能获取点位信息");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Util.showToast(context,"访问失败");
        }
    }
    public void updatePointInfo(Context context, PointEntity pointEntity, File file){
        try {
            String jsBody= JSONObject.toJSONString(pointEntity);
              RetMessage ret=UtilHttp.put(context,app.getIPAddr()+"/RecordCamera/facility/",jsBody);
            if (ret!=null&&ret.getCode()==200){
                retMessage.postValue(ret);
            }else {
                retMessage.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RetMessage retmsg=UtilHttp.putfile(context, app.getIPAddr()+"/RecordCamera/attribute/android/picfile",file,pointEntity.getFacilityId());
            if (retmsg!=null){
                retPictrue.postValue(retmsg);
            }else {
                retPictrue.postValue(retmsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Util.showToast(context,"图片过大，上传异常");
        }

    }
    public void updateDataInfo(Context context, PointEntity pointEntity){
        try {
            String jsBody= JSONObject.toJSONString(pointEntity);
            RetMessage ret=UtilHttp.put(context,app.getIPAddr()+"/RecordCamera/facility/",jsBody);
            if (ret!=null&&ret.getCode()==200){
                retMessage.postValue(ret);
            }else {
                retMessage.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getDictData(Context context){
        try{
            Map<String,String> map1=new HashMap<>();
            map1.put("dataType","设备厂商");
            RetMessage ret=UtilHttp.getParam(context,app.getIPAddr()+"/RecordCamera/dict/datatype",map1);
            if (ret!=null){
                List<DictEntity> dicts1=JSON.parseArray(ret.getData(),DictEntity.class);
                dictm.postValue(dicts1);
            }else {
//                dictm.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            RetMessage ret=UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/dict/datatype?dataType=点位类型");
            if (ret!=null){
                List<DictEntity> dicts2=JSON.parseArray(ret.getData(),DictEntity.class);
                dictu.postValue(dicts2);
            }else {
//                dictu.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            RetMessage ret=UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/dict/datatype?dataType=摄像机类型");
            if (ret!=null){
                List<DictEntity> dicts3=JSON.parseArray(ret.getData(),DictEntity.class);
                dictp.postValue(dicts3);
            }else {
//                dictp.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            RetMessage ret=UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/dict/datatype?dataType=监控范围");
            if (ret!=null&&ret.getCode()==200){
                List<DictEntity> dicts4=JSON.parseArray(ret.getData(),DictEntity.class);
                dictpos.postValue(dicts4);
            }else {
//                dictp.postValue(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getDeptName(Context context){
        try {
            RetMessage retMessage=UtilHttp.get(context,app.getIPAddr()+"/system/dept/list");///RecordCamera/attribute/android/tree
            deptRet.postValue(retMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void getImage(Context context, String facilityId){
        try {
            RetMessage ret=UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/attribute/android/getimage/"+facilityId);
            getPictrue.postValue(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
