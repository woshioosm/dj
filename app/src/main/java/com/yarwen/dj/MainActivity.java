package com.yarwen.dj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ClipData.newIntent;
import static android.provider.MediaStore.*;

public class MainActivity extends AppCompatActivity  implements SensorEventListener {
    private OrientationEventListener mOrientationListener; // 屏幕方向改变监听器

    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnOpen=(Button)this.findViewById(R.id.btnOpen);

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL); //为传感器注册监听器

        btnOpen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //得到新打开Activity关闭后返回的数据
                //第二个参数为请求码，可以根据业务需求自己编号
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, 1);

                Intent intent = new Intent(MainActivity.this,CameraActivity.class);

                startActivity(intent);
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = (new DateFormat()).format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File("/sdcard/myImage/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/myImage/" + name;
            File file2 = new File(fileName);
            try {
                b = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);// 将图片显示在ImageView里

//        if (resultCode == Activity.RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            //获取相机返回的数据，并转换为图片格式
//            Bitmap bitmap = (Bitmap) bundle.get("data");
//
//        }

        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        ((TextView)findViewById(R.id.textView)).setText("x:" + x);
        ((TextView)findViewById(R.id.textView2)).setText("y:" + y);
        ((TextView)findViewById(R.id.textView3)).setText("z:" + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
