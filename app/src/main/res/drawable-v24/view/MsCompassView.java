package com.example.camerainfo.view;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MsCompassView extends View implements Runnable{
    private Paint mTextPaint;
    private int msWidth,msHeight;

    private String msg  = "正北 0°";

    public MsCompassView(Context context) {
        this(context, null);
    }

    public MsCompassView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取屏幕高宽

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        msWidth = dm.widthPixels;
        msHeight = dm.heightPixels;

        // 初始化画笔

        mTextPaint = new Paint(); 
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(75);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        new Thread(this).start();
    }

    public MsCompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("朝向"+msg, msHeight/ 6 , msWidth / 3, mTextPaint);
    }

    // 更新指南针角度
    public void setDegree(float degree)
    {
        setMSG smg=new setMSG();
        msg=smg.dealDegree(degree);
    }

    public static class setMSG {
        private float dec = 0.0f;
        private String msg  = "";
        public String dealDegree(float degree){
            // 设置灵敏度
            if(Math.abs(dec - degree) >= 0)
            {
                dec = degree;
                int range = 10;
                String degreeStr = String.valueOf(Math.round(360-dec));

                // 指向正北
                if(dec > 360 - range || dec < range)
                {
                    msg = "正南 " + degreeStr + "°";
                }

                // 指向正东
                if(dec > 90 - range && dec < 90 + range)
                {
                    msg = "正西 " + degreeStr + "°";
                }

                // 指向正南
                if(dec > 180 - range && dec < 180 + range)
                {
                    msg = "正北 " + degreeStr + "°";
                }

                // 指向正西
                if(dec > 270 - range && dec < 270 + range)
                {
                    msg = "正东 " + degreeStr + "°";
                }

                // 指向东北
                if(dec >= range && dec <= 90 - range)
                {
                    msg = "西南 " + degreeStr + "°";
                }

                // 指向东南
                if(dec >=90 + range && dec <= 180-range)
                {
                    msg = "西北 " + degreeStr + "°";
                }

                // 指向西南
                if(dec >= 180+range && dec <= 270-range)
                {
                    msg = "东北 " + degreeStr + "°";
                }

                // 指向西北
                if(dec >= 270+range && dec <= 360 - range)
                {
                    msg = "东南 " + degreeStr + "°";
                }


            }
            return msg;
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            postInvalidate();
        }
    }

}
