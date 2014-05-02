package com.timkonieczny.polygon;

import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends ActionBarActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGLSurfaceView=new GameGLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }

    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }

    protected void onResume(){
        super.onResume();
        mGLSurfaceView.onResume();
    }
}
