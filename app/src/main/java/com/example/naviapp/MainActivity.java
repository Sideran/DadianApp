package com.example.naviapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.naviapp.activity.AutoUpdater;
import com.example.naviapp.activity.SettingInfoActivity;
import com.example.naviapp.activity.SettingsActivity;
import com.example.naviapp.databinding.ActivityMainBinding;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.entity.UserInfoEntity;
import com.example.naviapp.paging3.Repository;
import com.example.naviapp.ui.dashboard.DashboardFragment;
import com.example.naviapp.ui.home.HomeFragment;
import com.example.naviapp.ui.login.LoginActivity;
import com.example.naviapp.ui.notifications.NotificationsFragment;
import com.example.naviapp.ui.record.RecordFragment;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;
import com.example.naviapp.utils.UtilHttp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Datapp app;
    Toolbar toolbar;
    SharedPreferences sp;
    private HomeFragment homeFragment;
    private  DashboardFragment dashboardFragment;
    private  NotificationsFragment notificationsFragment;
    private RecordFragment recordFragment;
    private TextView username,email;
    Context context;
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
   UserInfoEntity userinfo;
    ActionBarDrawerToggle mDrawerToggle;
    ViewPager2 viewPager2;
    private long mExitTime = System.currentTimeMillis();  //为当前系统时间，单位：毫秒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragment=new HomeFragment();
        dashboardFragment=new DashboardFragment();
        notificationsFragment=new NotificationsFragment();
        recordFragment=new RecordFragment();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        app = (Datapp) this.getApplication();
        context=this;
        sp = getSharedPreferences("naviinfo", MODE_PRIVATE);
        requestPermission();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView= binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_record, R.id.navigation_notifications)
                .build();
        ImmersionBar.with(this).titleBar(binding.activityMain2.toolbar).init();
        toolbar=binding.activityMain2.toolbar;
        View headerView=binding.navView0.getHeaderView(0);
        username=(TextView)headerView.findViewById(R.id.slideusername);
        email=(TextView)headerView.findViewById(R.id.slideemail);
        viewPager2 = binding.activityMain2.viewPager2;
        updateApp();
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 4;
            }

            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return homeFragment;
                    case 1:
                        return dashboardFragment;
                    case 2:
                        return recordFragment;
                    default:
                        return notificationsFragment;

                }
            }

        });
        // 当底部导航栏切换页面时，改变ViewPager的显示
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_home:
                        viewPager2.setCurrentItem(0, true);
                        setTitle("地图");
                        break;
                    case R.id.navigation_dashboard:
                        viewPager2.setCurrentItem(1, true);
                        setTitle("搜索");
                        break;
                    case R.id.navigation_record:
                        viewPager2.setCurrentItem(2,true);
                        setTitle("录入");
                        break;
                    case R.id.navigation_notifications:
                        viewPager2.setCurrentItem(3, true);
                        setTitle("消息");
                        break;

                }
                return true;
            }
        });
        viewPager2.setOffscreenPageLimit(1);
        // 当ViewPager切换页面时，改变底部导航栏的状态
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navView.getMenu().getItem(position).setChecked(true);
                switch (position) {
                    case 0:
                        setTitle("地图");
                        viewPager2.setUserInputEnabled(false);
                        break;
                    case 1:
                        setTitle("搜索");
                        viewPager2.setUserInputEnabled(true);
                        break;
                    case 2:
                        setTitle("录入");
                        viewPager2.setUserInputEnabled(true);
                        break;
                    case 3:
                        setTitle("消息");
                        viewPager2.setUserInputEnabled(true);
                        break;
                }
            }
        });
        setSupportActionBar(binding.activityMain2.toolbar);
        //mDrawerLayout.setDrawerListener(new MyDrawerListener());//设置drawer的开关监听
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout,binding.activityMain2.toolbar, R.string.openDrawerContentDesc, R.string.closeDrawerContentDesc);
        //mDrawerToggle.syncState();
        //绑定侧边栏监听事件
        //binding.drawerLayout.addDrawerListener(mDrawerToggle);


        //绑定侧边栏菜单点击事件
        binding.navView0.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_gallery:
//                        Toast.makeText(MainActivity.this,"该功能正在维护中",Toast.LENGTH_SHORT).show();
                        Intent myinfo=new Intent(MainActivity.this, SettingInfoActivity.class);
                        startActivity(myinfo);
                        break;
                    case R.id.nav_setting:
                        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.download_map:
                        //在Activity页面调用startActvity启动离线地图组件
                        startActivity(new Intent(getApplicationContext(),
                                com.amap.api.maps.offlinemap.OfflineMapActivity.class));
                        break;
                    case R.id.exit:
                        RetMessage ret;
                        app.setLogin(false);
                        app.setToken("");
                        try {
                            ret = UtilHttp.post(context,app.getIPAddr()+"/logout", "");
                            if (ret!=null){
                            }
                        } catch (Exception e) {
                        }
                        finish();
                        break;
                };
                item.setCheckable(false);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_place:
//                        Util.MaskUtil.showProgressDialog("加载中",MainActivity.this);
//                        getLocation();
//                        app.setIPAddr("http://192.168.31.184:8082");
                        if(app.getIPAddr().equals("http://192.168.31.184:8082")){

                            Util.showToast(MainActivity.this,app.getIPAddr()+"改成了172.17.33.30:8082");
                            app.setIPAddr("http://172.17.33.30:8082");
                            Repository.INSTANCE.reCreate();
                        }else if(app.getIPAddr().equals("http://172.17.33.30:8082")){
                            app.setIPAddr("http://192.168.31.184:8082");
                            Util.showToast(MainActivity.this,"改成了192.168.31.184:8082");
                            Repository.INSTANCE.reCreate();
                        }

                        break;
                    case R.id.action_settings:
                        Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                }

                return true;
            }
        });
        //判断是否登录
        if (app.getAppLocks().tryLock()){
            try{
                if (!app.isLogining() && !app.isLogin()) {
                    app.setLogining(true);
                    app.setLogin(false);
                    app.setToken("");
                    Intent i = new Intent(this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(i, 1);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            } finally {
                app.getAppLocks().unlock(); // 即使出现异常或错误也能释放锁
            }
        }


    }
    @Override
    public void onBackPressed() {
        Display display=MainActivity.this.getWindowManager().getDefaultDisplay();
        if(System.currentTimeMillis() - mExitTime < 1200) {
            MainActivity.this.finish();   //关闭本活动页面
        }
        else{
            Util.showCenter(MainActivity.this,display,"再按返回键退出！");
            mExitTime = System.currentTimeMillis();   //这里赋值最关键，别忘记
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meun_toolbar, menu);
//        MenuItem item=menu.findItem(R.id.app_bar_search);
//        searchView=(SearchView) MenuItemCompat.getActionView(item);
//        searchView.setSubmitButtonEnabled(true);
//        searchOption();
        return true;
    }


    /** activity创建完成后 */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
    }
    /** 菜单键点击的事件处理 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
//        if(id==R.id.app_bar_search){
//            viewPager2.setCurrentItem(1, true);
//            setTitle("Dashboard");
//            return true;
//        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    /** 设备配置改变时 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 0 ) {
            Log.e("tag",""+resultCode);
        }
    }

    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.CAMERA,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS

        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
            Util.showToast(this,"已获得权限，可以定位啦！");
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }
    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //设置权限请求结果
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean haspermission = false;
        if (100 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    haspermission = true;
                }
            }
            if (haspermission) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionDialog();
            } else {
                //全部权限通过，可以进行下一步操作
                AutoUpdater manager = new AutoUpdater(MainActivity.this);
                manager.CheckUpdate();
            }
        }
    }

    AlertDialog alertDialog;

    //打开手动设置应用权限
    private void permissionDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        alertDialog.show();
    }

    private void cancelPermissionDialog() {
        alertDialog.cancel();
    }
    //权限鉴定结束

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume(){
        super.onResume();

       if(app.isLogin()){
//            getUserInfo();
            username.setText(app.getNickName());
            email.setText(app.getDeptName());

        }
    }
    private void updateApp(){
        //检查更新
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.INTERNET};
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                if (permissionList.size() <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新
                    AutoUpdater manager = new AutoUpdater(MainActivity.this);
                    manager.CheckUpdate();
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "自动更新异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    //onResume()
    //onRestart
    //onCreate
}