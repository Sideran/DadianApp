package com.example.naviapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import com.example.naviapp.R;
import com.example.naviapp.databinding.FragmentHomeBinding;
import com.example.naviapp.entity.PointEntity;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.Datapp;

import com.example.naviapp.utils.Util;

import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;
    private MapView map=null;
    int count =0;
    Datapp app;
    Context context;
    String str;
    private Display display;
    private double currentLat,currentLon;
    private List<PointEntity> makerlist;
    private Button showpoint;
    private AMap aMap;
    private Marker curShowWindowMarker;
    private boolean infoWindowShown = false;
    private MyLocationStyle myLocationStyle;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this.getActivity()).get(HomeViewModel.class);
        context=this.getActivity();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        app=(Datapp)this.getActivity().getApplication();
//        //判断是否登录
//        if (!app.isLogin()) {
//            Intent i = new Intent(this.getActivity(), LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivityForResult(i, 1);
//        }
        initview();
        setupUI(root);
        //隐私合规检查接口
        MapsInitializer.updatePrivacyShow(context,true,true);
        MapsInitializer.updatePrivacyAgree(context,true);
        display= getActivity().getWindowManager().getDefaultDisplay();
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        map.onCreate(savedInstanceState);
        mapOption();
        showpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showTop(context,display,"正在获取点位");
                HashMap<String,String> map1=new HashMap<>();
                map1.put("longitude",String.valueOf(currentLon));
                map1.put("latitude",String.valueOf(currentLat));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        homeViewModel.getNearPoint(context,map1);
                    }
                }).start();

            }
        });
//        str= Util.getJson(context,"point.json");
//        makerlist= JSON.parseArray(str,PointEntity.class);
        homeViewModel.getRetPoint().observe(this.getActivity(), new Observer<RetMessage>() {
            @Override
            public void onChanged(RetMessage retMessage) {
                if(retMessage.getCode()==200){
                    makerlist=JSON.parseArray(retMessage.getData(),PointEntity.class);
                    setMaker(makerlist);
                }
            }
        });
        return root;
    }
    private void mapOption(){
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = map.getMap();
        }
        AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {

//                arg0.setTitle("infowindow clicked");

            }
        };
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setOnInfoWindowClickListener(listener);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的填充颜色的方法
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentLat=location.getLatitude();
                currentLon=location.getLongitude();
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("tag","onMapClick");
                Log.e("tag","curShowWindowMarker="+curShowWindowMarker.isInfoWindowShown());
                //点击其它地方隐藏infoWindow
                if(curShowWindowMarker.isInfoWindowShown() && !infoWindowShown){
                    infoWindowShown = true;
                    return;
                }

                if(curShowWindowMarker.isInfoWindowShown() && infoWindowShown){
                    curShowWindowMarker.hideInfoWindow();
                }
            }
        });
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                curShowWindowMarker = marker;
                infoWindowShown = false;
                if(makerlist!=null){
//                    marker.get
                }
                return false;
            }
        });
//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(),R.drawable.location_marker)));

    }


    //隐藏软键盘
    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(context,v);  //Main.this是我的activity名
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public static void hideSoftKeyboard(Context context, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void initview(){
        map=binding.fragmentHome2.map;
        showpoint=binding.showpoint;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 ) {
            Log.e("tag",""+resultCode);
            //if (resultCode!= Activity.RESULT_OK) getActivity().finish();
        }
    }
//
//    /**
//     * 自定义infowinfow窗口
//     */
//    public void render(Marker marker, View view) {
//
//
//    }
//    /**
//     * 不能修改整个InfoWindow的背景和边框，返回null
//     */
//    @Override
//    public View getInfoContents(Marker marker) {
//        return null;
//    }
//    /**
//     * 监听自定义infowindow窗口的infowindow事件回调
//     */
//    public View getInfoWindow(Marker marker) {
//        if(infoWindow == null) {
//            infoWindow = LayoutInflater.from(context).inflate(
//                    R.layout.infowindow, null);
//        }
//        render(marker, infoWindow);
//        return infoWindow;
////加载custom_info_window.xml布局文件作为InfoWindow的样式
////该布局可在官方Demo布局文件夹下找到
//    }
    /**
     * 设置附近点位
     * @param makerlist
     */
    private void setMaker(List<PointEntity> makerlist){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (PointEntity i : makerlist) {
                    MarkerOptions markerOption = new MarkerOptions();
                    LatLng latLng = new LatLng(Double.parseDouble(i.getLatitude()), Double.parseDouble(i.getLongitude()));
                    markerOption.position(latLng);
                    markerOption.title("编号:"+i.getDeviceId()+" 名称:"+i.getName()+"\n经纬度"+i.getLongitude()+","+i.getLatitude()
                    ).snippet("地址:"+i.getAddress()+"\n所属:"+i.getOwner()+" 摄像头类型:"+i.getPtzType());
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_place_24));
                    markerOption.draggable(false);//设置Marker不可拖动
                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            //        markerOption.setFlat(true);//设置marker平贴地图效果
                    final Marker marker = aMap.addMarker(markerOption);
                }

            }
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map.onDestroy();
        //  摧毁Marker.
//        if (null != marker) {
//            marker.destroy();
//            marker=null;
//        }

    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
//        setMaker(makerlist);

    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }
}