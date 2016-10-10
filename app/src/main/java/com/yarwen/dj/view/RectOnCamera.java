package com.yarwen.dj.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.yarwen.dj.R;

import java.io.File;

/**
 * Created by PC19 on 2016/4/7.
 */
public class RectOnCamera extends View {
    private static final String TAG = "CameraSurfaceView";
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private RectF mRectF;
    // 圆
    private Point centerPoint;
    private int radio;

    public RectOnCamera(Context context) {
        this(context, null);
    }

    public RectOnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectOnCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        initView(context);
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        int marginLeft = (int) (mScreenWidth * 0.15);
        int marginTop = (int) (mScreenHeight * 0.25);
        mRectF = new RectF(marginLeft, marginTop, mScreenWidth - marginLeft, mScreenHeight - marginTop);

        centerPoint = new Point(mScreenWidth / 2, mScreenHeight / 2);
        radio = (int) (mScreenWidth * 0.1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = null;
        try {
            String pathString = "/sdcard/myImage/1476088265546.jpg";
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);


            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int newWidht = canvas.getWidth();
            int newHeight = canvas.getHeight();

            Matrix matrix = new Matrix();
//        matrix.postScale(canvas.getHeight()/width,canvas.getWidth()/height);
            matrix.postRotate(90);


            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);


//        canvas.translate(canvas.getWidth(),0);
            Rect mSrcRect = new Rect(0, 0, width, height);
            Rect mDestRect = new Rect(0, 0, newWidht, newHeight);
            mPaint.setAlpha(0x90);
            canvas.drawBitmap(resizedBitmap, mSrcRect, mDestRect, mPaint);

        } catch (Exception e) {
            // TODO: handle exception
        }
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(mRectF, mPaint);
//        mPaint.setColor(Color.WHITE);
//        Log.i(TAG, "onDraw");
//        canvas.drawCircle(centerPoint.x,centerPoint.y, radio,mPaint);// 外圆
//        canvas.drawCircle(centerPoint.x,centerPoint.y, radio - 20,mPaint); // 内圆
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                centerPoint = new Point(x, y);
                invalidate();
                if (mIAutoFocus != null) {
                    mIAutoFocus.autoFocus();
                }
                return true;
        }
        return true;
    }

    private IAutoFocus mIAutoFocus;

    /**
     * 聚焦的回调接口
     */
    public interface IAutoFocus {
        void autoFocus();
    }

    public void setIAutoFocus(IAutoFocus mIAutoFocus) {
        this.mIAutoFocus = mIAutoFocus;
    }
}
