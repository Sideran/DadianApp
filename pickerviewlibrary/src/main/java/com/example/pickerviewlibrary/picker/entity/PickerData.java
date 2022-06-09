package com.example.pickerviewlibrary.picker.entity;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickerData {
    private List<KVparmer> mFirstDatas;
    private List<KVparmer> mSecondDatas;
    private List<KVparmer> mThirdDatas;
    private List<KVparmer> mFourthDatas;
    //private Map<String, List<String>> mSecondDatas = new HashMap<>();
    //private Map<String, List<String>> mThirdDatas = new HashMap<>();
    //private Map<String, List<String>> mFourthDatas = new HashMap<>();
    private Integer fistId;
    private Integer secondId;
    private Integer thirdId;
    private Integer fourthId;

    private KVparmer fistDept;
    private KVparmer secondDept;
    private KVparmer thirdDept;
    private KVparmer fourthDept;

    private String firstText="";
    private String secondText="";
    private String thirdText="";
    private String fourthText="";
    private String pickerTitleName="";
    private int height=0;

    /**
     * 获取当前的列表
     * @param index 当前层级
     * @param currText 当前选中的文字key
     * @return 返回当前的数据数组
     */
    public List<KVparmer> getCurrDatas(int index,String currText) {
        List<KVparmer> curr = new ArrayList<>();
        switch (index){
            case 1:
                curr=mFirstDatas;
                break;
            case 2:
                curr=mSecondDatas;//.get(currText);
                break;
            case 3:
                curr=mThirdDatas;//.get(currText);
                break;
            case 4:
                curr=mFourthDatas;//.get(currText);
                break;
        }
        return curr;
    }
    public void setInitSelectId(Integer fistId) {
        this.fistId = fistId;
    }
    public void setInitSelectText(String firstText) {
        this.firstText = firstText;
    }
    public void setInitSelectId(Integer fistId,Integer secondId) {
        this.fistId = fistId;
        this.secondId = secondId;
    }
    public void setInitSelectText(String firstText, String secondText) {
        this.firstText = firstText;
        this.secondText = secondText;
    }
    public void setInitSelectId(Integer fistId,Integer secondId,Integer thirdId) {
        this.fistId = fistId;
        this.secondId = secondId;
        this.thirdId = thirdId;
    }
    public void setInitSelectText(String firstText, String secondText, String thirdText) {
        this.firstText = firstText;
        this.secondText = secondText;
        this.thirdText = thirdText;
    }
    public void setInitSelectId(Integer fistId,Integer secondId,Integer thirdId,Integer fourthId) {
        this.fistId = fistId;
        this.secondId = secondId;
        this.thirdId = thirdId;
        this.fourthId = fourthId;
    }
    public void setInitSelectText(String firstText, String secondText, String thirdText, String fourthText) {
        this.firstText = firstText;
        this.secondText = secondText;
        this.thirdText = thirdText;
        this.fourthText = fourthText;
    }
    public void clearSelectText(int index) {
        Log.i("--","index:"+index);
        switch (index){
            case 1:
                secondText="";
                thirdText="";
                fourthText="";
                break;
            case 2:
                thirdText="";
                fourthText="";
                break;
            case 3:
                fourthText="";
                break;
        }
    }

    public String getSelectText() {
        return firstText+secondText+thirdText+fourthText;
    }


    public String getPickerTitleName() {
        return pickerTitleName;
    }

    public void setPickerTitleName(String pickerTitleName) {
        this.pickerTitleName = pickerTitleName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<KVparmer> getFirstDatas() {
        return mFirstDatas;
    }

    public void setFirstDatas(List<KVparmer> mFirstDatas) {
        this.mFirstDatas = mFirstDatas;
    }

    public List<KVparmer> getSecondDatas() {
        return mSecondDatas;
    }

    public void setSecondDatas(List<KVparmer> mSecondDatas) {
        this.mSecondDatas = mSecondDatas;
    }

    public List<KVparmer> getThirdDatas() {
        return mThirdDatas;
    }

    public void setThirdDatas(List<KVparmer> mThirdDatas) {
        this.mThirdDatas = mThirdDatas;
    }

    public List<KVparmer> getFourthDatas() {
        return mFourthDatas;
    }

    public void setFourthDatas(List<KVparmer> mFourthDatas) {
        this.mFourthDatas = mFourthDatas;
    }

    public String getFirstText() {
        return firstText;
    }

    public void setFirstText(String firstText) {
        this.firstText = firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getThirdText() {
        return thirdText;
    }

    public void setThirdText(String thirdText) {
        this.thirdText = thirdText;
    }

    public String getFourthText() {
        return fourthText;
    }

    public void setFourthText(String fourthText) {
        this.fourthText = fourthText;
    }

    public Integer getFistId() {
        return fistId;
    }

    public void setFistId(Integer fistId) {
        this.fistId = fistId;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public void setSecondId(Integer secondId) {
        this.secondId = secondId;
    }

    public Integer getThirdId() {
        return thirdId;
    }

    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
    }

    public Integer getFourthId() {
        return fourthId;
    }

    public void setFourthId(Integer fourthId) {
        this.fourthId = fourthId;
    }
}
