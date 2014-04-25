package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Obstacle extends Shape{
    private final int mCoordsPerVertex = 3;

    private int mPoints;   // must be even
    private double mSectionOfPi;

    private float mAngle;

    private float mScalingFactor;

    public Obstacle(float screenRatio, float pieSize, ColorTheme theme, float angleOffset){
        super(screenRatio);
        mAngle=0.0f;
        mPoints=(int)(pieSize*50);
        double innerRadius=1.0;
        double outerRadius=1.2;

        mScalingFactor=0.0f;

        if(mPoints%2==1){
            mPoints++;
        }

        mCoords = new float[(mPoints) * mCoordsPerVertex];
        int j=1;

        for (int i= 0; i<mCoords.length; i+= mCoordsPerVertex*2) {

            mSectionOfPi = j*(double)1/mPoints*pieSize*(2*Math.PI);
            j++;

            mCoords[i] = (float) (innerRadius*Math.cos(mSectionOfPi));
            mCoords[i+3] = (float) (outerRadius*Math.cos(mSectionOfPi));

            if(mCoords[i]==(float)-innerRadius|| mCoords[i]==(float)innerRadius){        //Regelung für Spezialfälle von cos() und sin()
                mCoords[i+1] = 0.0f;
                mCoords[i+4] = 0.0f;
            }else {
                mCoords[i+1] = (float) (innerRadius*Math.sin(mSectionOfPi));
                mCoords[i+4] = (float) (outerRadius*Math.sin(mSectionOfPi));
            }

            mCoords[i+2] = 0.0f;
            mCoords[i+5] = 0.0f;
        }

        initializeBuffers(theme, 1);
    }

    public void draw(GL10 gl10) {
        if(GLRenderer.SCREEN_TOUCHED) {
            if (GLRenderer.X <= 0) {
                mAngle -= GLRenderer.TSLF/4;
            } else {
                mAngle += GLRenderer.TSLF/4;
            }
        }

        if (mScalingFactor < fullExpansion) {
            mScalingFactor += GLRenderer.TSLF*0.001;
        }else{
            mScalingFactor=0.0f;
        }

        gl10.glLoadIdentity();   // reset the matrix to its default state
        GLU.gluLookAt(gl10, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 3.0f);

        gl10.glTranslatef(0.0f, screenRatio -1.0f, 0.0f);
        gl10.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);

        gl10.glScalef(mScalingFactor, mScalingFactor, 1.0f);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glShadeModel(GL10.GL_FLAT);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl10.glVertexPointer(mCoordsPerVertex, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (mPoints));
    }
}

