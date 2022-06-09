package com.example.naviapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.ServiceSettings;
import com.example.naviapp.R;
import com.example.naviapp.ui.view.MsCompassView;
import com.example.naviapp.ui.view.MySurfaceView;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeasureActivity extends AppCompatActivity implements SensorEventListener, SeekBar.OnSeekBarChangeListener {
    private Camera camera = null;
    private MySurfaceView mySurfaceView = null;
    private MsCompassView.setMSG smg;
    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private FrameLayout preview;
    private String path,tempLocation,pointCode;
    private float angle,distance,temp,total,temp2,direction;
    private double currentLat=0.0f,currentLon=0.0f;
    private Button point,record;
    private TextView mTvHeight, mTvDistance, mTvAngle,Theight;
    private SeekBar mPbHeight;
    private int progress;
    private Datapp app;
    private int count,count1;
    private Display display;
    private boolean heightAvilibale=true;
    private MsCompassView ms_cview;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    SharedPreferences sharedPreferences;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        getViews();
        setlistener();
        app=(Datapp)getApplication();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);
        getCameraInstance();
        getCurrentLocationLatLng();
    }
    //设置监听
    public void setlistener(){
        onClickListener listener=new onClickListener();
        point.setOnClickListener(listener);
        record.setOnClickListener(listener);

    }
    /**
     * 初始化定位并设置定位回调监听
     */
    private void getCurrentLocationLatLng()  {
        try {
            //隐私政策合规
            ServiceSettings.updatePrivacyShow(this,true,true);
            ServiceSettings.updatePrivacyAgree(this,true);

            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();

            // 同时使用网络定位和GPS定位,优先返回最高精度的定位结果,以及对应的地址描述信息
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。默认连续定位 切最低时间间隔为1000ms
            mLocationOption.setInterval(2000);
            //定位请求超时，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(30000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }catch (Exception e){
            e.printStackTrace();

        }

    }


    /**
     * 定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLocationType();
                    //获取纬度
                    currentLat = amapLocation.getLatitude();
                    //获取经度
                    currentLon = amapLocation.getLongitude();
                    //获取当前位置
                   tempLocation=amapLocation.getAddress();
//                    mTextViewLocation.setText(amapLocation.getCity());
                    ms_cview.setLatLon(currentLat,currentLon);
//                    Util.showToast(MeasureActivity.this,tempLocation);
//                    Log.d("currentLocation", "currentLat : " + currentLat + " currentLon : " + currentLon);
                    //获取精度信息
                    amapLocation.getAccuracy();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
        camera = null;
        sensorManager.unregisterListener((SensorEventListener) this); // 解除监听器注册
        if (mLocationClient != null) {
            //停止定位
            mLocationClient.stopLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        display=MeasureActivity.this.getWindowManager().getDefaultDisplay();
        if (camera == null) {
            camera = getCameraInstance();
        }
        //必须放在onResume中，不然会出现Home键之后，再回到该APP，黑屏
        mySurfaceView = new MySurfaceView(getApplicationContext(), camera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mySurfaceView);

        sensorManager.registerListener((SensorEventListener) this, gyroSensor, SensorManager.SENSOR_DELAY_UI); //为传感器注册监听器

        ms_cview = new MsCompassView(MeasureActivity.this);
        smg=new MsCompassView.setMSG();
        preview.addView(ms_cview);
        if (mLocationClient != null) {
            // 启动定位
            mLocationClient.startLocation();
        }
        ms_cview.setLatLon(currentLat,currentLon);
    }

    /*得到一相机对象*/
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = camera.open();
            //旋转90°
            camera.setDisplayOrientation(90);
            // 获取Camera parameters
            Camera.Parameters params = camera.getParameters();
            // 设置聚焦模式
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            // 设置Camera parameters
            camera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /**
         *  方向传感器提供三个数据，分别为azimuth、pitch和roll。
         *  azimuth：方位，返回水平时磁北极和Y轴的夹角，范围为0°至360°。
         *  0°=北，90°=东，180°=南，270°=西。
         *  pitch：x轴和水平面的夹角，范围为-180°至180°。
         *  当z轴向y轴转动时，角度为正值。
         *  roll：y轴和水平面的夹角，由于历史原因，范围为-90°至90°。
         *  当x轴向z轴移动时，角度为正值。
         */
        ms_cview.setDegree(sensorEvent.values[0]);
        angle = Math.abs(sensorEvent.values[1]);
        temp = (float) Math.abs(progress * Math.tan(angle * Math.PI / 180));
//        if (temp < 0) {
//            temp = -temp;
//        }
        if (count % 5 == 0) {
            mTvAngle.setText("角度：" + String.format(Locale.CHINA, "%.2f", angle));
//            mTvDistance.setText("距离：" + String.format(Locale.CHINA, "%.2f", temp) + " cm");
            direction=sensorEvent.values[0];
        }
        if(count1>=1) {
            float height = (float) (distance / (Math.tan((180 - angle) * Math.PI / 180)));
            temp2 = height + progress;
            if(count%5==0&&heightAvilibale) Theight.setText("物体高度："+String.format(Locale.CHINA,"%.2f",temp2));
        }

        count++;
    }
    class onClickListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.point:
                    if(progress!=0){
                        count1++;
                        if(count1>2){
                            count1=1;
                            heightAvilibale=true; }
                        if (temp < 0) temp = -temp;
                        if(count1==1){
                            distance=temp;
                            mTvDistance.setText("距离：" + String.format(Locale.CHINA, "%.2f", distance/100) + " m");
                        }
                        if(count1>1) {
                            total=temp2;
                            heightAvilibale=false;
                        }
                        //Toast.makeText(MeasureActivity.this,"第"+count1+"个点位已记录",Toast.LENGTH_SHORT).show();
                        Util.showBottom(MeasureActivity.this,display,"第"+count1+"个点位已记录");
                    }else{
                        showNotice(MeasureActivity.this,"未设置身高");
                    }
                    break;
                case R.id.record:
                    if(count1==2&&currentLat!=0.0f&&currentLon!=0.0f){
                        recordPic();
                    }else if(count1!=2){
                        showNotice(MeasureActivity.this,"未记录物体高度");
                    }else if(currentLat==0.0f||currentLon==0.0f){
                        showNotice(MeasureActivity.this,"经纬度尚未获取");
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void getViews() {
        bundle= this.getIntent().getExtras();
        pointCode=bundle.getString("facilityId");
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        mTvHeight = (TextView) findViewById(R.id.height);
        mTvDistance = (TextView) findViewById(R.id.distance);
        mTvDistance.setText("距离:");
        mTvAngle = (TextView) findViewById(R.id.angle);
        mPbHeight = (SeekBar) findViewById(R.id.height_progressbar);
        point=(Button)findViewById(R.id.point);
        record=(Button)findViewById(R.id.record);
        Theight=(TextView)findViewById(R.id.Theight);
        this.progress=Double.valueOf(String.valueOf(sharedPreferences.getString("height","0"))).intValue();
        if(progress!=0){
            mPbHeight.setProgress(progress);
            mTvHeight.setText(progress+"cm");
        }

        mPbHeight.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);
    }

    private void recordPic(){
        count1=0;
        //Util.showTop(MeasureActivity.this,display,"已记录，物体高度："+String.format(Locale.CHINA,"%.2f",total));
//       进行坐标精确转换  Math.abs(direction),distance
        double[] lnglat1 =new double[2];
        lnglat1[0]=currentLon;
        lnglat1[1]=currentLat;
        double[] convetLatLon=Util.getEndPointByTrigonometric(Math.abs(direction),lnglat1,distance/100);
        LatLng latLng=new LatLng(convetLatLon[1],convetLatLon[0]);
        app.setTotalHeight(total);
        app.setDirection(smg.dealDegree(direction));
        app.setLatLng(latLng);
        app.setTemptLocation(tempLocation);
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if(data!=null){
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 1;
                    Bitmap bm= Util.getBitMap(data,camera,false);//默认不用前置摄像头
                    saveFile(bm);

                }
            }
        });
    }
    @Nullable
    File getAppSpecificAlbumStorageDir(Context context, String albumName) {
        // 获取应用程序特定目录中的图片目录
        // 外部存储。
        path=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ "/";
        File file = new File(path, albumName);
        if (file == null || !file.mkdirs()) {
            Log.d("FILE", "Directory not created");
        }
        return file;
    }

    private String saveFile(Bitmap bm){
        FileOutputStream fos = null;
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");

        Date curDate = new Date(System.currentTimeMillis());

        String timeCode = app.getUserName()+formatter.format(curDate);
        try {
            File file;
            if(pointCode!=null&&!pointCode.equals(""))
                file =new File(getAppSpecificAlbumStorageDir(MeasureActivity.this,"PointPic"),pointCode+".jpg");
           else
                file =new File(getAppSpecificAlbumStorageDir(MeasureActivity.this,"PointPic"),timeCode+".jpg");
            fos = new FileOutputStream(file);
            boolean compress = bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
//            Util.showTop(MeasureActivity.this,display, compress ? "记录成功" : "记录失败");
            if(compress){
                // 给相册app发送广播,接收到后会更新图库
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//            intent.setData(Uri.fromFile(file));
//            sendBroadcast(intent);
            app.setFileUrl(path+"PointPic");
            app.setTimeCode(timeCode);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("height",progress+"");
            editor.commit();
//            Intent intent = new Intent(MeasureActivity.this,ScrollingActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            }
            finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
        }
        return "";
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        this.progress = progress;
        mTvHeight.setText(progress + "cm");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        if (mLocationClient != null) {
            //销毁定位客户端
            mLocationClient.onDestroy();
        }
    }

}
