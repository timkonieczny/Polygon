package com.timkonieczny.polygon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameGLSurfaceView extends GLSurfaceView{

    private int mWidth;
    private int mHeight;

    public GameGLSurfaceView(Context context){
        super(context);

        GLRenderer renderer = new GLRenderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        mWidth=getWidth();
        mHeight=getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
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
