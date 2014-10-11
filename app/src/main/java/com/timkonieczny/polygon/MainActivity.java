package com.timkonieczny.polygon;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorEventListener{

    private GLSurfaceView mGLSurfaceView;
    private SensorManager mSensorManager;
    protected static float SENSOR_Y;
    private Sensor mAccelerometer;
    private float rawData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGLSurfaceView=new GameGLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }

    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }

    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mGLSurfaceView.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        rawData = rawData + 0.15f * (event.values[0] - rawData);    // low pass filter (adjustment with 0.15f value)

        if(rawData>5){
            SENSOR_Y=0.5f;
        }else if(rawData<-5){
            SENSOR_Y=-0.5f;
        }else{
            SENSOR_Y = rawData /10;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
