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
    protected static float SENSOR_X, SENSOR_Y, SENSOR_Z;

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
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST);
        mGLSurfaceView.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        SENSOR_X=event.values[1];
        SENSOR_Y=event.values[0];
        SENSOR_Z=event.values[2];
        Log.d("MainActivity","x="+Float.toString(SENSOR_X)+" y="+Float.toString(SENSOR_Y)+" z="+Float.toString(SENSOR_Z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
