package com.timkonieczny.polygon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameGLSurfaceView extends GLSurfaceView{

    private GLRenderer mRenderer;
    private int mWidth;
    private int mHeight;
    private float mCurrentX;
    private float mCurrentY;

    public GameGLSurfaceView(Context context){
        super(context);

        mRenderer = new GLRenderer();
        setRenderer(mRenderer);

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

            mCurrentX=e.getX();
            mCurrentY=e.getY();
            if(mCurrentX<mWidth/2){
                GLRenderer.X =-1+(2*mCurrentX/mWidth);
            }else{
                GLRenderer.X =(2*mCurrentX/mWidth)-1;
            }

            if(mCurrentY<mHeight/2){
                GLRenderer.Y =1-(2*mCurrentY/mHeight);
            }else{
                GLRenderer.Y =1-(2*mCurrentY/mHeight);
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
