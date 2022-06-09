package com.example.naviapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.example.naviapp.entity.PointEntity;
import com.example.naviapp.entity.UserInfoEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Util {


    public static class MaskUtil {

        /**
         * 遮罩层
         * @param message 遮罩层的文字显示
         * @param mActivity 使用的activity
         */
        public static ProgressDialog showProgressDialog(String message, Activity mActivity) {
            ProgressDialog mProgressDialog = null;
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(mActivity);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.setMessage(message);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            return mProgressDialog;
        }
    }

    public static Bitmap getBitMap(byte[] data, Camera camera, boolean mIsFrontalCamera) {

        // 获取照相后的bitmap
        Bitmap tmpBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.reset();
        if (mIsFrontalCamera) {
            matrix.setRotate(-90);
        } else {
            matrix.setRotate(90);
        }
        tmpBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(),
                tmpBitmap.getHeight(), matrix, true);


        return tmpBitmap;
    }
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void showTop(Context context,Display display,String message){
//        display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, height / 6);
        toast.show();

    }
    public static void showCenter(Context context,Display display,String message){
//        display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
    public static void showBottom(Context context,Display display,String message){
//        display = context.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, height / 6);
        toast.show();

    }
    @NonNull
    public static File getAppSpecificAlbumStorageDir(Context context, String albumName) {
        // 获取应用程序特定目录中的图片目录
        // 外部存储。
        String path=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ "/";
        File file = new File(path, albumName);
        if (file == null || !file.mkdirs()) {
            Log.d("FILE", "Directory not created");
        }
        return file;
    }
    public static Bitmap decodeBitmapForArray( byte[] bytes, int viewWidth, int viewHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray( bytes, 0, bytes.length, options);
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;
        //Log.e(TAG, "get Iamge form file,  path = " + path);
        //返回Bitmap对象
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.length, options);
    }
    /**
     * 获取json数据
     * @param fileName
     * @return
     */
    public static String getJson(Context context,String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
                Log.d("AAA", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<PointEntity> simulate(Context context,String fileName){
        String str=getJson(context,fileName);
        List<PointEntity> pointEntities= JSON.parseArray(str,PointEntity.class);
        return pointEntities;
    }
    public static List<UserInfoEntity> simulateUserInfo(Context context, String fileName){
        String str=getJson(context,fileName);
        List<UserInfoEntity> userInfos= JSON.parseArray(str,UserInfoEntity.class);
        return userInfos;
    }

    //https://blog.csdn.net/xx326664162/article/details/48376045
    public static Bitmap decodeBitmapForFile(String path, int viewWidth, int viewHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;
        //Log.e(TAG, "get Iamge form file,  path = " + path);
        //返回Bitmap对象
        return BitmapFactory.decodeFile(path, options);
    }
    private static int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
        int inSampleSize = 1;
        if(viewWidth == 0 || viewWidth == 0){
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }

    /**
     * 长半径a=6378137 米
     */
    public static double EARTH_RADIUS = 6378137;
    /**
     * 短半径b=6356752.3142
     */
    public static double b = 6356752.3142;
    /**
     * 扁率f=1/298.2572236
     */
    public static double f = 1 / 298.2572236;

    /**
     * 已知一点经纬度，方位角，距离，求另一点经纬度
     * 通过三角函数求终点坐标-球面坐标系
     * </summary>
     * <param name="angle">角度</param>
     *<param name="startPoint">起点</param>
     * <param name="distance">距离（米）</param>
     * <returns>终点坐标</returns>
     */
    public static double[] getEndPointByTrigonometric(double angle, double[] startPoint, double distance) {
        double lon=startPoint[0];
        double lat=startPoint[1];

        double alpha1 = rad(angle);
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(rad(lat));
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1));
        double sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (EARTH_RADIUS * EARTH_RADIUS - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

        double cos2SigmaM = 0;
        double sinSigma = 0;
        double cosSigma = 0;
        double sigma = distance / (b * A), sigmaP = 2 * Math.PI;
        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
                    - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = distance / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda - (1 - C) * f * sinAlpha
                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

        double revAz = Math.atan2(sinAlpha, -tmp); // final bearing

//        System.out.println(revAz);
//        System.out.println(lon + deg(L) + "," + deg(lat2));

        double[] endPoint=new double[2];
        endPoint[0]=lon + deg(L);
        endPoint[1]= deg(lat2);
        return endPoint;
    }

    /**
     * 度换成弧度
     * @param d 度
     * @return 弧度
     */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 弧度换成度
     * @param x 弧度
     * @return 度
     */
    public static double deg(double x) {
        return x * 180 / Math.PI;
    }


}
