package com.lkx.collectview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    final static private String TAG = "MainActivity";

    private int diameterPixels;
    private int lastSecond = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();
            int second = calendar.get(Calendar.SECOND);
            if (second != lastSecond) { //秒数有变化才重新绘制
                lastSecond = second;
                int minute = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                TextView textViewTime = findViewById(R.id.textViewTime);
                textViewTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
            }
            //250ms后再刷新一次页面
            sendEmptyMessageDelayed(0, 250);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // 隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮，不锁屏

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        diameterPixels = widthPixels < heightPixels ? widthPixels : heightPixels;
        Log.d(TAG, String.format("diameter %dpx %ddp", diameterPixels, DensityUtil.px2dip(this, diameterPixels)));
        fixOrientation(getResources().getConfiguration().orientation);

        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        fixOrientation(newConfig.orientation);
    }

    private void fixOrientation(int orientation) {
        ClockView clockView = findViewById(R.id.clockView);
        ViewGroup.LayoutParams params = clockView.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏处理
            Log.d(TAG, "横屏");
            params.height = diameterPixels;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) { // 竖屏处理
            Log.d(TAG, "竖屏");
            params.width = diameterPixels;
        }
        clockView.setLayoutParams(params);
    }
}
