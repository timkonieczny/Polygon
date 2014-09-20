package com.timkonieczny.polygon;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorEventListener{

    private GLSurfaceView mGLSurfaceView;
    private SensorManager mSensorManager;
    protected static float SENSOR_Y;
    private float[] mGravity,mGeomagnetic;
    float prevRotation=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
        mGLSurfaceView.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            if(orientation[2]>prevRotation+0.007 || orientation[2]<prevRotation-0.007) {
                float myFloat = orientation[2];
                myFloat = (float)((int)( myFloat *1000f ))/1000f;
                SENSOR_Y = myFloat/2;
                prevRotation = orientation[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
