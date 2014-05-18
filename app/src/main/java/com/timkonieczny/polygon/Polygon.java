package com.timkonieczny.polygon;

import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class Polygon extends Shape{

    private int mPoints = 52;   // 50 vertices + center + first vertex as start and ending (2x)
    protected float[] theme;
    private float mCurrentR, mCurrentG, mCurrentB;
    private boolean mIsRFaded,mIsGFaded,mIsBFaded, mAddR, mAddG, mAddB, mIsAddRGBSet;

    public Polygon(float screenRatio, float[] theme) {
        super(screenRatio);
        initialize();
        initializeBuffers(theme);
        this.theme=theme;
    }

    public Polygon(float screenRatio) {
        super(screenRatio);
        initialize();
        initializeBuffersIgnoreTheme();
    }

    private void initialize(){
        coords = new float[(mPoints) * coordsPerVertex];
        int j=1;

        for (int i= coordsPerVertex; i<=(mPoints-2)*3; i+= coordsPerVertex) {
            double sectionOfPi = j * (double) 1 / (mPoints - 2) * 2 * Math.PI;
            j++;

            coords[i] = (float) Math.cos(sectionOfPi);
            if(coords[i]==-1.0f|| coords[i]==1.0f){        // special cases of cos() und sin()
                coords[i+1] = 0.0f;
            }else {
                coords[i+1] = (float) Math.sin(sectionOfPi);
            }
            coords[i+2] = 0.0f;

        }

        coords[coords.length-3]= coords[3];     // first coordinate==last coordinate
        coords[coords.length-2]= coords[4];
        coords[coords.length-1]= coords[5];

        mCurrentR=mCurrentG=mCurrentB=1.0f;
    }

    private void initializeBuffersIgnoreTheme(){
        mVertexByteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
        mVertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = mVertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        colorByteBuffer = ByteBuffer.allocateDirect(16 * coords.length / 3);
        colorByteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = colorByteBuffer.asFloatBuffer();
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

    protected void updateTheme(float[] theme){
        this.theme=theme;
        r=theme[0];
        g=theme[1];
        b=theme[2];
        a=theme[3];
    }

    public void draw(GL10 gl10) {

        if(!isExpanded) {
            if (scalingFactor < fullExpansion) {
                scalingFactor += GLRenderer.TSLF*0.001;
                if(!mIsRFaded||!mIsGFaded||!mIsBFaded) {
                    fadeColor();
                }else{
                    mIsAddRGBSet=false;
                }
            } else {
                gl10.glClearColor(r, g, b, a);
                mCurrentR=mCurrentG=mCurrentB=1.0f;
                mIsRFaded=mIsGFaded=mIsBFaded=false;
                isExpanded = true;
            }
        }

        if(scalingFactor<0.3f){
            scalingFactor += GLRenderer.TSLF*0.001;
            if(scalingFactor>3.0f){     //Fix for big frame bumps
                scalingFactor=3.0f;
            }
        }

        gl10.glLoadIdentity();   // reset the matrix to its default state
        GLU.gluLookAt(gl10, MainActivity.SENSOR_X*2, 0.0f, 3.0f, MainActivity.SENSOR_X*2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        gl10.glTranslatef(0.0f, screenRatio -1.0f, 0.0f);
        gl10.glScalef(scalingFactor, scalingFactor, 1.0f);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glShadeModel(GL10.GL_FLAT);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, (mPoints));
    }
}