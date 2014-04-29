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
        mAngle=angleOffset;
        mPoints=(int)(pieSize*50);
        double innerRadius=1.0;
        double outerRadius=1.2;

        mScalingFactor=0.0f;

        if(mPoints%2==1){
            mPoints++;
        }

        mCoords = new float[(mPoints) * mCoordsPerVertex];
        int j=0;

        for (int i= 0; i<mCoords.length-6; i+= mCoordsPerVertex*2) {

            mSectionOfPi = j*(double)1/(mPoints-2)*pieSize*(2*Math.PI);
            j++;
//            System.out.println(mSectionOfPi);

            mCoords[i] = (float) (innerRadius*Math.cos(mSectionOfPi));      // x
            mCoords[i+3] = (float) (outerRadius*Math.cos(mSectionOfPi));    // x

            if(mCoords[i]==(float)-innerRadius|| mCoords[i]==(float)innerRadius){        //Regelung für Spezialfälle von cos() und sin()
                mCoords[i+1] = 0.0f;    // y
                mCoords[i+4] = 0.0f;    // y
            }else {
                mCoords[i+1] = (float) (innerRadius*Math.sin(mSectionOfPi));    // y
                mCoords[i+4] = (float) (outerRadius*Math.sin(mSectionOfPi));    // y
            }

            mCoords[i+2] = 0.0f;    // z
            mCoords[i+5] = 0.0f;    // z

            System.out.println("x1="+mCoords[i]+" x2="+mCoords[i+3]+" y1="+mCoords[i+1]+" y2="+mCoords[i+4]+" z1="+mCoords[i+2]+" z2="+mCoords[i+5]);
        }

        mCoords[mCoords.length-6]=(float) (innerRadius*Math.cos(j*(double)1/(mPoints-2)*pieSize*(2*Math.PI)));      // x    Letztes Koordinatenpaar
        mCoords[mCoords.length-3]=(float) (outerRadius*Math.cos(j*(double)1/(mPoints-2)*pieSize*(2*Math.PI)));      // x
        mCoords[mCoords.length-5]=1.0f; //y
        mCoords[mCoords.length-2]=1.2f; //y
        mCoords[mCoords.length-4]=0.0f; //z
        mCoords[mCoords.length-1]=0.0f; //z

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

