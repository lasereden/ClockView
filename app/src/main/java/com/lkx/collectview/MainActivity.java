package com.lkx.collectview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

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
                getSupportActionBar().setTitle(String.format("%02d:%02d:%02d", hour, minute, second));
            }
            //250ms后再刷新一次页面
            sendEmptyMessageDelayed(0, 250);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮，不锁屏
        mHandler.sendEmptyMessage(0);
    }
}
