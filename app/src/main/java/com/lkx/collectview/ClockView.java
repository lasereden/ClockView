package com.lkx.collectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;

/**
 * 作者: LKX
 * 时间: 2019-10-24
 * 描述: 钟表View
 */
public class ClockView extends View {
    final static private int borderPadding = 0; // 外边框距离父view的padding
    final static private int scalePadding = 40; // 刻度距离父view的padding
    final static private int textPadding = 30;  // 数字距离父view的距离

    private Paint mPaint;
    private Context mContext;
    private int mHeight;
    private int mWidth;

    private Calendar mCalendar;
    private int lastSecond = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCalendar = Calendar.getInstance();
            int second = mCalendar.get(Calendar.SECOND);
            if (second != lastSecond) { //秒数有变化才重新绘制
                lastSecond = second;
                //重新绘制页面
                invalidate();
            }
            //250ms后再刷新一次页面
            sendEmptyMessageDelayed(0, 250);
        }
    };

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init() {
        mPaint = new Paint();
        mCalendar = Calendar.getInstance();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);

        //绘制外环
        /*mPaint.setStrokeWidth(dp2px(5));
        mPaint.setColor(Color.GRAY); // 画阴影
        canvas.drawCircle(mWidth / 2, mHeight / 2, mHeight / 2 - dp2px(borderPadding + 2), mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mHeight / 2 - dp2px(borderPadding), mPaint); // 画外框*/


        //绘制刻度,每次绘制完需要旋转一定的角度,然后继续绘制
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setColor(Color.GREEN & Color.LTGRAY);
                mPaint.setStrokeWidth(dp2px(3));
                canvas.drawLine(mWidth / 2, dp2px(scalePadding), mWidth / 2, dp2px(scalePadding + 13), mPaint);
            } else {
                mPaint.setColor(Color.GREEN & Color.GRAY);
                mPaint.setStrokeWidth(dp2px(1));
                canvas.drawLine(mWidth / 2, dp2px(scalePadding), mWidth / 2, dp2px(scalePadding + 10), mPaint);
            }
            //一共绘制60个刻度,每次旋转360°/60
            canvas.rotate(360 / 60, mWidth / 2, mHeight / 2);
        }

        mPaint.setColor(Color.GREEN & Color.LTGRAY); // 前景色

        //绘制刻度数字
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setTextSize(dp2px(24));
        for (int i = 0; i < 12; i++) {
            //保存当前画布状态
            canvas.save();
            //将要旋转的中心点定位到数字上,旋转的角度取决于当前数字倾斜的角度
            canvas.rotate(-360/12*i, mWidth / 2, dp2px(textPadding)- mPaint.measureText(String.valueOf(i==0?"12":i)) / 2);
            //绘制数字
            canvas.drawText(String.valueOf(i==0?"12":i), mWidth / 2, dp2px(textPadding), mPaint);
            //恢复被旋转的画布
            canvas.restore();
            //绕表盘中心旋转,绘制下一个刻度
            canvas.rotate(360 / 12, mWidth / 2, mHeight / 2);
        }

        //获取当前时间
        int second = mCalendar.get(Calendar.SECOND);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);

        //绘制时针
        mPaint.setStrokeWidth(dp2px(5));
        canvas.save();
        canvas.rotate((hour + (float) minute / 60) * 360 / 12, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, dp2px(scalePadding + 52), mWidth / 2, mHeight / 2 + dp2px(20), mPaint);
        canvas.restore();

        //绘制分针
        mPaint.setStrokeWidth(dp2px(3));
        canvas.save();
        canvas.rotate((minute + (float) second / 60) * 360 / 60, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, dp2px(scalePadding + 22), mWidth / 2, mHeight / 2 + dp2px(20), mPaint);
        canvas.restore();

        mPaint.setColor(Color.RED & Color.LTGRAY); // 秒针及中心圆点颜色

        //绘制秒针
        mPaint.setStrokeWidth(dp2px(1));
        canvas.save();
        canvas.rotate(second * (365 / 60), mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, dp2px(scalePadding + 12), mWidth / 2, mHeight / 2 + dp2px(20), mPaint);
        canvas.restore();

        //绘制中心圆点
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, dp2px(5), mPaint);
    }

    private int dp2px(int dp) {
        return DensityUtil.dip2px(mContext, dp);
    }
}
