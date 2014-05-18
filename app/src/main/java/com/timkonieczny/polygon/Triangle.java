package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Triangle extends Shape {

    private final boolean mIsShadow;
    protected boolean isFading;
    private float mCurrentR, mCurrentG, mCurrentB;
    private boolean mIsRFaded,mIsGFaded,mIsBFaded, mAddR, mAddG, mAddB, mIsAddRGBSet;

    protected float[] theme;

    public Triangle(float screenRatio, float[] theme, boolean isShadow) {
        super(screenRatio);

        coords = new float[]{
         0.0f,  0.0f, 0.0f,// top
        -0.6f, -1.0f, 0.0f,// bottom left
         0.6f, -1.0f, 0.0f // bottom right
        };

        initializeBuffers(theme);
        mIsShadow=isShadow;
        isFading =false;
        mIsRFaded=mIsGFaded=mIsBFaded=false;
    }

    protected void updateTheme(float[] theme){
        this.theme=theme;
        r=theme[0];
        g=theme[1];
        b=theme[2];
        a=theme[3];
    }

    private void fadeColor(){

        if(!mIsAddRGBSet) {
            mAddR = mCurrentR <= r;
            mAddG = mCurrentG <= g;
            mAddB = mCurrentB <= b;
            mIsAddRGBSet=true;
        }

        if(mAddR){
            if(mCurrentR<r){
                mCurrentR+= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentR=r;
                mIsRFaded=true;
            }
        }else{
            if(mCurrentR>r){
                mCurrentR-= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentR=r;
                mIsRFaded=true;
            }
        }

        if(mAddG){
            if(mCurrentG<g){
                mCurrentG+= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentG=g;
                mIsGFaded=true;
            }
        }else{
            if(mCurrentG>g){
                mCurrentG-= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentG=g;
                mIsGFaded=true;
            }
        }

        if(mAddB){
            if(mCurrentB<b){
                mCurrentB+= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentB=b;
                mIsBFaded=true;
            }
        }else{
            if(mCurrentB>b){
                mCurrentB-= GLRenderer.TSLF * 0.001;
            }else{
                mCurrentB=b;
                mIsBFaded=true;
            }
        }

        for(int i=0; i< 4* coords.length/3; i+=4){
            colorBuffer.put(i, mCurrentR);
            colorBuffer.put(i+1, mCurrentG);
            colorBuffer.put(i+2, mCurrentB);
            colorBuffer.put(i+3, 1.0f);
        }
    }

    public void draw(GL10 gl10) {

        if(isFading){
            if(!mIsRFaded||!mIsGFaded||!mIsBFaded) {
                fadeColor();
            }else{
                isFading=false;
            }
        }else{
            mIsRFaded=mIsGFaded=mIsBFaded=false;
            mIsAddRGBSet=false;
        }

        gl10.glLoadIdentity();   // reset the matrix to its default state
        if(!mIsShadow) {
            GLU.gluLookAt(gl10, MainActivity.SENSOR_X * 2, 0.0f, 3.0f, MainActivity.SENSOR_X * 2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }else{
            GLU.gluLookAt(gl10, MainActivity.SENSOR_X+0.1f, 0.0f, 3.0f, MainActivity.SENSOR_X+0.1f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }
        gl10.glTranslatef(0.0f, screenRatio -2.0f, 0.0f);
        gl10.glScalef(0.1f, 0.1f, 1.0f);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glShadeModel(GL10.GL_FLAT);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl10.glVertexPointer(coordsPerVertex, GL10.GL_FLOAT, 0, vertexBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, coords.length / coordsPerVertex);
    }
}
