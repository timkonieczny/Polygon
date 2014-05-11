package com.timkonieczny.polygon;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class GameGLSurfaceView extends GLSurfaceView{

    private int mWidth;
    private int mHeight;

    public GameGLSurfaceView(Context context){
        super(context);

        GLRenderer renderer = new GLRenderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        mWidth=getWidth();
        mHeight=getHeight();

        if (hasFocus) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent e) {
        if (e.getActionMasked()==MotionEvent.ACTION_DOWN){
            GLRenderer.SCREEN_TOUCHED =true;

            float currentX = e.getX();
            float currentY = e.getY();
            if(currentX <mWidth/2){
                GLRenderer.X =-1+(2* currentX /mWidth);
            }else{
                GLRenderer.X =(2* currentX /mWidth)-1;
            }

            if(currentY <mHeight/2){
                GLRenderer.Y =1-(2* currentY /mHeight);
            }else{
                GLRenderer.Y =1-(2* currentY /mHeight);
            }

            return true;
        }else {
            if(e.getActionMasked()==MotionEvent.ACTION_UP) {
                GLRenderer.SCREEN_TOUCHED = false;
            }
            return false;
        }
    }
}
