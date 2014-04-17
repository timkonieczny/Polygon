package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Polygon extends Shape{

    private final int mCoordsPerVertex = 3;


    private int mPoints = 52;   // 50 vertices + center + first vertex as start and ending (2x)
    private double mSectionOfPi;

    protected boolean expand;

    public Polygon(float screenRatio, ColorTheme theme) {
        super(screenRatio);

        expand =false;


        mCoords = new float[(mPoints) * mCoordsPerVertex];
        int j=1;

        for (int i= mCoordsPerVertex; i<=(mPoints-2)*3; i+= mCoordsPerVertex) {
            mSectionOfPi = j*(double)1/(mPoints-2)*2*Math.PI;
            j++;

            mCoords[i] = (float) Math.cos(mSectionOfPi);
            if(mCoords[i]==-1.0f|| mCoords[i]==1.0f){        //Regelung für Spezialfälle von cos() und sin()
                mCoords[i+1] = 0.0f;
            }else {
                mCoords[i+1] = (float) Math.sin(mSectionOfPi);
            }
            mCoords[i+2] = 0.0f;

        }

        mCoords[mCoords.length-3]= mCoords[3];     //Endpunkt ist der Startpunkt
        mCoords[mCoords.length-2]= mCoords[4];
        mCoords[mCoords.length-1]= mCoords[5];

        initializeBuffers(theme, 1);
    }

    public void draw(GL10 gl10) {
        if(expand) {
            if (scalingFactor < fullExpansion) {
                scalingFactor += GLRenderer.TSLF*0.001;
            } else {
                gl10.glClearColor(r, g, b, a);
                expand = false;
            }
        }

        if(scalingFactor<0.3f){
            scalingFactor += GLRenderer.TSLF*0.001;
            if(scalingFactor>3.0f){     //Fix for big frame bumps
                scalingFactor=3.0f;
            }
        }

        gl10.glLoadIdentity();   // reset the matrix to its default state
        GLU.gluLookAt(gl10, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 3.0f);

        gl10.glTranslatef(0.0f, screenRatio -1.0f, 0.0f);
        gl10.glScalef(scalingFactor, scalingFactor, 1.0f);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glShadeModel(GL10.GL_FLAT);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, (mPoints));
    }
}