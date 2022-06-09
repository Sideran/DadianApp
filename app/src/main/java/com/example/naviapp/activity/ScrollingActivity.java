package com.example.naviapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.example.naviapp.R;
import com.example.naviapp.databinding.ActivityScrollingBinding;
import com.example.naviapp.entity.DictEntity;

import com.example.naviapp.entity.PointEntity;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;
import com.example.pickerviewlibrary.picker.TeaPickerView;
import com.example.pickerviewlibrary.picker.entity.KVparmer;
import com.example.pickerviewlibrary.picker.entity.PickerData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private EditText showname,showcode,showaddress,showowner,showblock,showdirection,showheight,showlatlon,servicePersonName,editTextPhone,ipAddress,info;
    private Spinner spinnertype,pointype,manufactory,watcharea;
    private String spinnertypestr,pointTypestr,manufactorystr,facilityId,areastr,dataScope;
    private String roundCameraDirection="全景";
    private Toolbar toolbar;
    private RadioGroup rediogroup;
    private RadioButton redioEnter,redioOuter;
    private AppBarLayout appBarLayout;
    private ImageView photo;
    private ImageButton listpicker;
    private ProgressDialog progressDialog;
    JSONArray Deptdata;
    String ancestor;
    Bitmap bm;
    KVparmer myDept;
    List<KVparmer> mFirstDatas=new ArrayList<>();
    Map<String, List<KVparmer>> mSecondDatas= new HashMap<>();
    Map<String, List<KVparmer>> mThirdDatas= new HashMap<>();
    private Display display;
    CollapsingToolbarLayout toolBarLayout;
    FloatingActionButton fab;
    PointEntity updatePoint;
    Intent intent;
    Button upload;
    Bundle bundle;
    TextWatcher textWatcher;
    boolean isChanged,isData=false;
    PointEntity point;
    String pointCode;
    Datapp app;
    TeaPickerView teaPickerView;
    ArrayList<String> dictm,dictu,dictp,dictpos;
    SpinnerAdapter spinnerM,spinnerU,spinnerP;
    ScrollingViewModel scrollingViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scrollingViewModel=new ViewModelProvider(this).get(ScrollingViewModel.class);
        setSupportActionBar(toolbar);
        //pointEntityList=Util.simulate(this,"point.json");
        updatePoint=new PointEntity();
        bundle= this.getIntent().getExtras();
        if(!bundle.getString("facilityId").equals(""))pointCode=bundle.getString("facilityId");
        initView();
        setlistener();
        getPointInfo();
//        OnChangelistener(binding.getRoot());

//        showaddress.addTextChangedListener(textWatcher);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= appBarLayout.getHeight() * 4 /5) {
                    toolBarLayout.setTitle("摄像头点位详情");//设置标题
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
//                    photo.setVisibility(View.INVISIBLE);
                    toolBarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    toolBarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                } else {
//                    photo.setVisibility(View.VISIBLE);
                    toolBarLayout.setTitle("详情");
                    //toolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
                }
            }
        });

        scrollingViewModel.getPointInfo().observe(this, new Observer<PointEntity>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(PointEntity pointEntity) {
                if(pointEntity.getName()!=null&!"".equals(pointEntity.getName()))showname.setText(pointEntity.getName());
                if(pointEntity.getDeviceId()!=null&!"".equals(pointEntity.getDeviceId()))showcode.setText(pointEntity.getDeviceId());
                if(pointEntity.getAddress()!=null&&!"".equals(pointEntity.getAddress()))showaddress.setText(pointEntity.getAddress());
                if(pointEntity.getOwner()!=null&&!"".equals(pointEntity.getOwner()))showowner.setText(pointEntity.getOwner());
                //if(pointEntity.getDeptId()!=null&&!"".equals(pointEntity.getDeptId())) updatePoint.setDeptId(pointEntity.getDeptId());
                if(pointEntity.getBlock()!=null&&!"".equals(pointEntity.getBlock())) showblock.setText(pointEntity.getBlock());
                if(pointEntity.getHeight()!=null&&!"".equals(pointEntity.getHeight()))showheight.setText(pointEntity.getHeight()+"cm");
                if(pointEntity.getIpAddress()!=null&&!"".equals(pointEntity.getIpAddress()))ipAddress.setText(pointEntity.getIpAddress());
                if(pointEntity.getLatitude()!=null&&!"".equals(pointEntity.getLatitude())&&!"".equals(pointEntity.getLongitude()))showlatlon.setText(pointEntity.getLongitude()+",\n"+pointEntity.getLatitude());
                if(pointEntity.getDirectionType()!=null&&!"".equals(pointEntity.getDirectionType())){
                    showdirection.setText(pointEntity.getDirectionType());
                    if(!pointEntity.getDirectionType().equals(roundCameraDirection))
                        app.setDirection(pointEntity.getDirectionType());
                }
                if(pointEntity.getManufacturer()!=null&&!"".equals(pointEntity.getManufacturer())) manufactorystr=pointEntity.getManufacturer();
                if(pointEntity.getUseType()!=null&&!"".equals(pointEntity.getUseType())) pointTypestr=pointEntity.getUseType();
                if(pointEntity.getPtzType()!=null&&!"".equals(pointEntity.getPtzType())) spinnertypestr=pointEntity.getPtzType();
                if(pointEntity.getPositionType()!=null&&!"".equals(pointEntity.getPositionType())) areastr = pointEntity.getPositionType();
                if(pointEntity.getRoomType()!=null&&!"".equals(pointEntity.getRoomType())){
                    if ("室内".equals(pointEntity.getRoomType())) {redioEnter.setChecked(true);}
                    if("室外".equals(pointEntity.getRoomType())) {redioOuter.setChecked(true);}
                }
                if(pointEntity.getOnDuty()!=null&&!"".equals(pointEntity.getOnDuty())) servicePersonName.setText(pointEntity.getOnDuty());
                if(pointEntity.getDutyPhone()!=null&&!"".equals(pointEntity.getDutyPhone())) editTextPhone.setText(pointEntity.getDutyPhone());
                if(pointEntity.getInfo()!=null&&!"".equals(pointEntity.getInfo())) info.setText(pointEntity.getInfo());
                getDictInfo();
                isChanged=false;
                progressSwitch(false);
            }
        });
        scrollingViewModel.getRetMessage().observe(this, new Observer<RetMessage>() {
            @Override
            public void onChanged(RetMessage retMessage) {

                if (retMessage!=null&&retMessage.getCode()==200){
                    isChanged=false;
                    Util.showCenter(ScrollingActivity.this,display,"数据保存成功");
                    isData=true;
                }else {
                    showNotice(ScrollingActivity.this,retMessage.getMsg());
                    //Util.showCenter(ScrollingActivity.this,display,"数据保存失败请重试");
                }

            }
        });
        scrollingViewModel.getRetPictrue().observe(this, new Observer<RetMessage>() {
            @Override
            public void onChanged(RetMessage retMessage) {
                isChanged=false;
                progressDialog.dismiss();
                if (retMessage!=null&&retMessage.getCode()==200){
                    Util.showTop(ScrollingActivity.this,display,"图片保存成功");
                    if(isData){
                        finish();
                    }
                }else{
                    showNotice(ScrollingActivity.this,retMessage.getMsg());
                }
            }
        });
        scrollingViewModel.getDictm().observe(this, new Observer<List<DictEntity>>() {
            @Override
            public void onChanged(List<DictEntity> dictEntities) {
                dictm=new ArrayList<>();
                dictm.add("设备厂商");
                for (DictEntity dict:dictEntities){
                    dictm.add(dict.getDataName());
                }
                spinnerM=new ArrayAdapter<String>(ScrollingActivity.this,R.layout.spinner_item,dictm);
                manufactory.setAdapter(spinnerM);
                if(!"".equals(manufactorystr)) {
                    for(String str :dictm){
                        if(str.equals(manufactorystr)){
                            int pos=dictm.indexOf(str);
                            manufactory.setSelection(pos);}
                    }
                }
            }
        });
        scrollingViewModel.getDictu().observe(this, new Observer<List<DictEntity>>() {
            @Override
            public void onChanged(List<DictEntity> dictEntities) {
                dictu=new ArrayList<>();
                dictu.add("点位类型");
                for (DictEntity dict:dictEntities){
                    dictu.add(dict.getDataName());
                }
                spinnerU=new ArrayAdapter<String>(ScrollingActivity.this,R.layout.spinner_item,dictu);
                pointype.setAdapter(spinnerU);
                if(!"".equals(pointTypestr)){
                    for (String str : dictu){
                        if(str.equals(pointTypestr)){
                            int pos=dictu.indexOf(str);
                            pointype.setSelection(pos);
                        }
                    }
                }
            }
        });
        scrollingViewModel.getDictp().observe(this, new Observer<List<DictEntity>>() {
            @Override
            public void onChanged(List<DictEntity> dictEntities) {
                dictp=new ArrayList<>();
                dictp.add("摄像机类型");
                for (DictEntity dict:dictEntities){
                    dictp.add(dict.getDataName());
                }
                spinnerP=new ArrayAdapter<String>(ScrollingActivity.this,R.layout.spinner_item,dictp);
                spinnertype.setAdapter(spinnerP);
                if(!"".equals(spinnertypestr)){
                    for (String str: dictp){
                        if (str.equals(spinnertypestr)){
                            int pos=dictp.indexOf(str);
                            spinnertype.setSelection(pos);
                        }
                    }
                }
            }
        });
        scrollingViewModel.getDictPos().observe(this, new Observer<List<DictEntity>>() {
            @Override
            public void onChanged(List<DictEntity> dictEntities) {
                dictpos=new ArrayList<>();
                dictpos.add("监控范围");
                for (DictEntity dict:dictEntities){
                    dictpos.add(dict.getDataName());
                }
                SpinnerAdapter postionAdapter=new ArrayAdapter<String>(ScrollingActivity.this,R.layout.spinner_item,dictpos);
                watcharea.setAdapter(postionAdapter);
                if(!"".equals(areastr)){
                    for (String str : dictpos){
                        if(str.equals(areastr)){
                        int pos = dictpos.indexOf(str);
                        watcharea.setSelection(pos);}
                    }
                }
            }
        });
        scrollingViewModel.getPic().observe(this, new Observer<RetMessage>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(RetMessage retMessage) {
                if(retMessage.getCode()==200){
//                    byte[] bytes= Base64.getDecoder().decode(retMessage.getData().replace("\r\n", ""));
//                    bm= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    bm=Util.decodeBitmapForArray(android.util.Base64.decode(retMessage.getData(), android.util.Base64.DEFAULT),960,640);
                    if(bm!=null)
                        photo.setImageBitmap(bm);
                }else {
                    Util.showTop(ScrollingActivity.this,display,retMessage.getMsg());
                }
            }
        });
        scrollingViewModel.getDeptRet().observe(this, new Observer<RetMessage>() {
            @Override
            public void onChanged(RetMessage retMessage) {
                if(retMessage.getCode()==200){
                    try {
                        Deptdata= JSON.parseArray(retMessage.getData());
                        if("1".equals(dataScope)){
                            ancestor="0,"+Deptdata.getJSONObject(0).getString("deptId");
                            for(Object i : Deptdata){
                                String first=JSON.parseObject(i.toString()).getString("ancestors");
                                if(ancestor.equals(first)){
                                    KVparmer kVparmer=new KVparmer(JSON.parseObject(i.toString()).getString("deptId"),JSON.parseObject(i.toString()).getString("deptName"));
                                    mFirstDatas.add(kVparmer);
                                }
                            }
                        }else {
                            ancestor=Deptdata.getJSONObject(0).getString("ancestors");
                            mFirstDatas.add(myDept);
                        }
                        intiPickerView();
                    }catch (Exception e){
                        Log.d("picker",e.getMessage());
                    }
                }else {
                    Util.showCenter(ScrollingActivity.this,display,retMessage.getMsg());
                }
            }
        });
    }
    private void getPointInfo(){
       new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        scrollingViewModel.getInfo(ScrollingActivity.this,pointCode);

                    }
                }
        ).start();

    }
    private void getDictInfo(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        scrollingViewModel.getDictData(ScrollingActivity.this);
                        scrollingViewModel.getDeptName(ScrollingActivity.this);
                    }
                }
        ).start();
    }

    private void initView(){
        app=(Datapp)this.getApplication();
//        latLng=new LatLng();
        photo=binding.photo;
        upload=binding.conentScroll.upload;
        appBarLayout=binding.appBar;
        assert binding.conentScroll.position1 != null;
        watcharea=binding.conentScroll.position1.watcharea;
        spinnertype=binding.conentScroll.position1.spinnertype;
        assert binding.conentScroll.Guoatrribute1 != null;
        manufactory=binding.conentScroll.Guoatrribute1.manufactory;
        pointype=binding.conentScroll.Guoatrribute1.pointype;
        showname=binding.conentScroll.Guoatrribute1.showname;
        showaddress=binding.conentScroll.position1.showaddress;
        showcode=binding.conentScroll.Guoatrribute1.showcode;
        showdirection=binding.conentScroll.position1.showdirection;
        showlatlon=binding.conentScroll.position1.showlatlon;
        showowner=binding.conentScroll.Guoatrribute1.showowner;
        showblock=binding.conentScroll.Guoatrribute1.block;
        showheight=binding.conentScroll.position1.showheight;
        ipAddress=binding.conentScroll.Guoatrribute1.ipAddress;
        assert binding.conentScroll.management1 != null;
        info=binding.conentScroll.management1.info;
        servicePersonName=binding.conentScroll.management1.servicePersonName;
        editTextPhone=binding.conentScroll.management1.editTextPhone;
        toolbar = binding.toolbar;
        rediogroup=binding.conentScroll.position1.radiogroup;
        redioOuter=binding.conentScroll.position1.radioOuter;
        redioEnter=binding.conentScroll.position1.radioEnter;
        listpicker=binding.conentScroll.Guoatrribute1.listpicker;
        assert binding.conentScroll.imageView3 != null;
        binding.conentScroll.imageView3.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        binding.conentScroll.Guoatrribute1.Guoatrribute.setVisibility(View.VISIBLE);
        assert binding.conentScroll.imageView2 != null;
        binding.conentScroll.imageView2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        assert binding.conentScroll.imageView1 != null;
        binding.conentScroll.imageView1.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        toolBarLayout = binding.toolbarLayout;
        progressSwitch(true);
        //toolBarLayout.setTitle("摄像头点位详情");
        fab = binding.measureFab;
        manufactory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(dictm!=null&&!dictm.get(position).equals("设备厂商"))
                    updatePoint.setManufacturer(manufactory.getSelectedItem().toString());
                else updatePoint.setManufacturer("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pointype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dictu!=null&&!dictu.get(position).equals("点位类型")){
                    updatePoint.setUseType(dictu.get(position));
                }else updatePoint.setUseType("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dictp!=null&&!dictp.get(position).equals("摄像机类型")) {
                    updatePoint.setPtzType(spinnertype.getSelectedItem().toString());
                    if(dictp.get(position).equals("球机")||dictp.get(position).equals("半球")||dictp.get(position).equals("4G布控球")){
                        showdirection.setText(roundCameraDirection);
                    }else {
                        showdirection.setText(app.getDirection());
                    }
                }else updatePoint.setPtzType("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        watcharea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dictpos!=null&&!dictpos.get(position).equals("监控范围")){
                    updatePoint.setPositionType(watcharea.getSelectedItem().toString());
                }else updatePoint.setPositionType("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextWatcher afterTextChangedListener2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                isChanged = true;
            }
        };
        showname.addTextChangedListener(afterTextChangedListener2);

        showaddress.addTextChangedListener(afterTextChangedListener2);
        //showdirection.addTextChangedListener(afterTextChangedListener2);
        showlatlon.addTextChangedListener(afterTextChangedListener2);
        showcode.addTextChangedListener(afterTextChangedListener2);
        showowner.addTextChangedListener(afterTextChangedListener2);
        ipAddress.addTextChangedListener(afterTextChangedListener2);
        info.addTextChangedListener(afterTextChangedListener2);
        servicePersonName.addTextChangedListener(afterTextChangedListener2);
        editTextPhone.addTextChangedListener(afterTextChangedListener2);

    }

    public void setlistener(){
        layoutOnClickListener listener=new layoutOnClickListener();
        RadioOnCheckedListener onCheckedListener=new RadioOnCheckedListener();
        photo.setOnClickListener(listener);
        rediogroup.setOnCheckedChangeListener(onCheckedListener);
        binding.conentScroll.linearlayout1.setOnClickListener(listener);
        binding.conentScroll.linearlayout2.setOnClickListener(listener);
        binding.conentScroll.linearlayout3.setOnClickListener(listener);
        binding.conentScroll.upload.setOnClickListener(listener);
        fab.setOnClickListener(listener);
    }

    private void intiPickerView(){
        //设置数据有多少层级
        PickerData data=new PickerData();
        data.setFirstDatas(mFirstDatas);
        data.setInitSelectText("请选择");
        teaPickerView =new TeaPickerView(this,data);
        teaPickerView.setScreenH(3)
                .setDiscolourHook(true)
                .setRadius(25)
                .setContentLine(true)
                .setRadius(25)
                .build();
        listpicker.setOnClickListener(v -> {
            //显示选择器
            teaPickerView.show(listpicker);
        });
        //选择器点击事件
        teaPickerView.setOnPicker1ClickListener(pickerData -> {
            showowner.setText(pickerData.getFirstText());
            updatePoint.setDeptId(pickerData.getFistId().toString());
            //Toast.makeText(ScrollingActivity.this,pickerData.getFirstText(),Toast.LENGTH_SHORT).show();
            Boolean nextFlour=false;
            ArrayList<KVparmer> secondData=new ArrayList<>();
            for(Object i :Deptdata){
                String secondAnc=JSON.parseObject(i.toString()).getString("ancestors");
                String ancestor1=ancestor+","+pickerData.getFistId().toString();
                if(ancestor1.equals(secondAnc)){
                    nextFlour=true;
                    secondData.add(new KVparmer(JSON.parseObject(i.toString()).getString("deptId"),JSON.parseObject(i.toString()).getString("deptName")));
                }
            }
            if(nextFlour){
                teaPickerView.setList(2,secondData);
            }else {
                teaPickerView.dismiss();//关闭选择器
            }

        });
        teaPickerView.setOnPicker2ClickListener(pickerData -> {
            showblock.setText(pickerData.getSecondText());
            teaPickerView.dismiss();
        });
    }

    class RadioOnCheckedListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton checkedRadioButton = (RadioButton)rediogroup.findViewById(checkedId);
            boolean checked = checkedRadioButton.isChecked();
            if(checked){
                switch (checkedId){
                    case R.id.radioEnter:
                        updatePoint.setRoomType("室内");
                        break;
                    case R.id.radioOuter:
                        updatePoint.setRoomType("室外");
                        break;
                }
            }
        }
    }

    class layoutOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case  R.id.measure_fab:
                    Intent intent1=new Intent(ScrollingActivity.this,MeasureActivity.class);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    break;
                case R.id.upload:
                    uploadPointInfo();
                    break;
                case R.id.photo:
                    if(bm!=null){
                        bigImageLoader(bm);
                    }break;
                case R.id.linearlayout1:
                    if(binding.conentScroll.Guoatrribute1.Guoatrribute.getVisibility()==View.VISIBLE){
                        binding.conentScroll.Guoatrribute1.Guoatrribute.setVisibility(View.GONE);
                        binding.conentScroll.imageView1.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    }else {
                        binding.conentScroll.Guoatrribute1.Guoatrribute.setVisibility(View.VISIBLE);
                        binding.conentScroll.imageView1.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }break;
                case R.id.linearlayout2:
                    if(binding.conentScroll.position1.position.getVisibility()==View.VISIBLE){
                        binding.conentScroll.position1.position.setVisibility(View.GONE);
                        binding.conentScroll.imageView2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    }else {
                        binding.conentScroll.position1.position.setVisibility(View.VISIBLE);
                        binding.conentScroll.imageView2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }break;
                case R.id.linearlayout3:
                    if(binding.conentScroll.management1.management.getVisibility()==View.VISIBLE){
                        binding.conentScroll.management1.management.setVisibility(View.GONE);
                        binding.conentScroll.imageView3.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    }else {
                        binding.conentScroll.management1.management.setVisibility(View.VISIBLE);
                        binding.conentScroll.imageView3.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }break;
            }
        }
    }
    private void progressSwitch(Boolean b){
        if(b) {
            binding.conentScroll.Guoatrribute1.guoProgressbar.setVisibility(View.VISIBLE);
            binding.conentScroll.position1.posProgressBar.setVisibility(View.VISIBLE);
            binding.conentScroll.management1.manageProgressBar.setVisibility(View.VISIBLE);

        }else {
            binding.conentScroll.Guoatrribute1.guoProgressbar.setVisibility(View.GONE);
            binding.conentScroll.position1.posProgressBar.setVisibility(View.GONE);
            binding.conentScroll.management1.manageProgressBar.setVisibility(View.GONE);
        }
    }


    private void uploadPointInfo(){
        updatePoint.setFacilityId(pointCode);
        if(ipAddress.getText().length()>0) updatePoint.setIpAddress(ipAddress.getText().toString());
        if(showaddress.getText().length()>0) updatePoint.setAddress(showaddress.getText().toString());
//             if(showaddress.getText().length()==0) ToastDialog(this,"必填项未完成","请检查地址是否填写");
        if(showcode.getText().length()>0){ updatePoint.setDeviceId(showcode.getText().toString());}
        if(showheight.getText().length()>0) updatePoint.setHeight(showheight.getText().toString().replace("cm",""));
        if(showdirection.getText().length()>0) updatePoint.setDirectionType(showdirection.getText().toString());
        if(app.getLatLng()!=null) {updatePoint.setLatitude( Double.toString(app.getLatLng().latitude));
            updatePoint.setLongitude(Double.toString(app.getLatLng().longitude));}

//        else if (app.getLatLng()==null&&!showlatlon.equals("")){
//            updatePoint.setLongitude();
//        }
        if(showname.getText().length()>0) updatePoint.setName(showname.getText().toString());
          //ToastDialog(this,"必填项未完成","请检查名称是否填写");
        if(showowner.getText().length()>0) updatePoint.setOwner(showowner.getText().toString());
          //ToastDialog(this,"必填项未完成","请检查所属单位是否填写");
        if(showblock.getText().length()>0) updatePoint.setBlock(showblock.getText().toString());
        if(servicePersonName.getText().length()>0) updatePoint.setOnDuty(servicePersonName.getText().toString());
        if(editTextPhone.getText().length()>0) updatePoint.setDutyPhone(editTextPhone.getText().toString());
        if(info.getText().length()>0) updatePoint.setInfo(info.getText().toString());
        File file =new File(app.getFileUrl() + "/" + pointCode + ".jpg");
        if(showname.getText().length()>0&&showowner.getText().length()>0){
            if(!file.exists()){
//            showNotice(ScrollingActivity.this,"未拍照获取经纬度");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        scrollingViewModel.updateDataInfo(ScrollingActivity.this,updatePoint);
                    }
                }).start();
            }if(file.exists()){
                //三个spinner添加上
                progressDialog=Util.MaskUtil.showProgressDialog("保存中",ScrollingActivity.this);
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                scrollingViewModel.updatePointInfo(ScrollingActivity.this,updatePoint,file);
                            }
                        }
                ).start();
            }
//            else {ToastDialog(this,"经纬度未获取","摄像头经纬度请通过拍摄获取");}
        }else {
            showNotice(ScrollingActivity.this,"名称或所属单位未填写");
        }


    }
    //方法里直接实例化一个imageView不用xml文件，传入bitmap设置图片
    private void bigImageLoader(Bitmap bitmap){
        final Dialog dialog = new Dialog(ScrollingActivity.this);
        ImageView image = new ImageView(Datapp.getContext());
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        //将dialog周围的白块设置为透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //显示
        dialog.show();
        //点击图片取消
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
    }


    public  void showNotice(Context context,String message){
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.toast_notice, null);
        TextView textView=(TextView)v.findViewById(R.id.diary_toast_tv);
        textView.setText(message);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void exitDialog()
    {
        AlertDialog.Builder aa=new AlertDialog.Builder(this);
        aa.setTitle("确认不保存退出？");
        aa.setMessage("将不会保存更改的内容");
        aa.setIcon(R.drawable.ic_baseline_notification_important_24);
        aa.setPositiveButton("确定",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ScrollingActivity.this.finish();
            }
        });
        aa.setNegativeButton("取消", null);
        aa.create();
        aa.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK&&isChanged)
        {
            exitDialog();
        }else {
            finish();
        }
        return true;

    }
    @Override
    public void onResume(){
        super.onResume();
        display=ScrollingActivity.this.getWindowManager().getDefaultDisplay();
        if(app.isLogin()){
            dataScope=app.getDataScope();
            myDept=new KVparmer("100","");
            myDept.setKey(app.getDeptId());
            myDept.setValue(app.getDeptName());
        }
        if(!app.getFileUrl().equals("")){
            bm= Util.decodeBitmapForFile(app.getFileUrl()+"/"+pointCode+".jpg",640,960);
            if(bm!=null)
                photo.setImageBitmap(bm);
            else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        scrollingViewModel.getImage(ScrollingActivity.this,pointCode);
                    }
                }).start();
            }
        }
        if(app.getTotalHeight()!=0)showheight.setText(String.format("%.2f",app.getTotalHeight())+"cm");
        if(dictp!=null){
            if(spinnertype.getSelectedItem().toString().equals("球机")||spinnertype.getSelectedItem().toString().equals("半球")||spinnertype.getSelectedItem().toString().equals("4G布控球"))
            {
                    showdirection.setText(roundCameraDirection);
            } else if(spinnertype.getSelectedItem().toString().equals("摄像机类型")){
                if(!app.getDirection().equals("")){ showdirection.setText(app.getDirection());}
            }else {
                if(!app.getDirection().equals("")){ showdirection.setText(app.getDirection());}
            }
        }
        if(app.getLatLng()!=null) showlatlon.setText(app.getLatLng().longitude+",\n"+app.getLatLng().latitude);
        if(app.getTemptLocation()!=null&&!"".equals(app.getTemptLocation())&&showaddress.getText().length()==0){
            showaddress.setText(app.getTemptLocation());
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        app.setLatLng(null);
        app.setDirection("");
        app.setTotalHeight(0);
        app.setTemptLocation("");
    }

}