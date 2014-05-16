package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Obstacle extends Shape{

    protected int points;   // must be even

    protected float angle;
    protected float scalingOffset;

    protected boolean offsetPermission=false;
    private final float scalingDefault, angleDefault;

    private Polygon mGameOverPolygon;

    public Obstacle(float screenRatio, float pieSize, float[] theme, float angleOffset, float scalingOffset){
        super(screenRatio);
        angleDefault=angleOffset;
        scalingDefault=scalingOffset;
        points =(int)(pieSize*50);
        double innerRadius=1.0;
        double outerRadius=1.2;

        scalingFactor=0.0f;

        resetValues();

        if(points %2==1){
            points++;
        }

        mCoords = new float[(points) * coordsPerVertex];
        int j=0;

        for (int i= 0; i<mCoords.length-6; i+= coordsPerVertex*2) {

            double sectionOfPi = j*(double)1/(points -2)*pieSize*(2*Math.PI);
            j++;

            mCoords[i] = (float) (innerRadius*Math.cos(sectionOfPi));      // x
            mCoords[i+3] = (float) (outerRadius*Math.cos(sectionOfPi));    // x

            if(mCoords[i]==(float)-innerRadius|| mCoords[i]==(float)innerRadius){        //Regelung für Spezialfälle von cos() und sin()
                mCoords[i+1] = 0.0f;    // y
                mCoords[i+4] = 0.0f;    // y
            }else {
                mCoords[i+1] = (float) (innerRadius*Math.sin(sectionOfPi));    // y
                mCoords[i+4] = (float) (outerRadius*Math.sin(sectionOfPi));    // y
            }

            mCoords[i+2] = 0.0f;    // z
            mCoords[i+5] = 0.0f;    // z
        }

        mCoords[mCoords.length-6]=(float) (innerRadius*Math.cos(j*(double)1/(points -2)*pieSize*(2*Math.PI)));      // x    Letztes Koordinatenpaar
        mCoords[mCoords.length-3]=(float) (outerRadius*Math.cos(j*(double)1/(points -2)*pieSize*(2*Math.PI)));      // x
        mCoords[mCoords.length-5]=1.0f; //y
        mCoords[mCoords.length-2]=1.2f; //y
        mCoords[mCoords.length-4]=0.0f; //z
        mCoords[mCoords.length-1]=0.0f; //z

        initializeBuffers(theme);

        mGameOverPolygon = new Polygon(screenRatio,new float[]{0.0f,0.0f,0.0f,0.5f});
    }

    public void resetValues(){
        scalingOffset =scalingDefault;
        angle =angleDefault;
        isExpanded =false;
        scalingFactor=0.0f;
        offsetPermission=false;
    }

    public void draw(GL10 gl10, boolean foreignOffsetPermission) {
        if(GLRenderer.SCREEN_TOUCHED) {
            if (GLRenderer.X <= 0) {
                angle += GLRenderer.TSLF/2;
            } else {
                angle -= GLRenderer.TSLF/2;
            }
        }

        if (foreignOffsetPermission) {
            if (scalingFactor < fullExpansion) {
                if(GLRenderer.TSLF<100){
                    scalingFactor += GLRenderer.TSLF*0.001;
                }else{
                    scalingFactor += 0.05;  // fix for big frame bumps
                }

                if(scalingFactor>= scalingOffset){
                    offsetPermission=true;
                }
            }else{
                isExpanded =true;
            }

            if(scalingFactor>0.8f&&scalingFactor<1.0f&& angle <280.0f&& angle >90.0f){    // collision TODO: imprecise?
                // TODO: game over
                mGameOverPolygon.scalingFactor=0.5f;
                mGameOverPolygon.draw(gl10);
            }

            gl10.glLoadIdentity();   // reset the matrix to its default state
            GLU.gluLookAt(gl10, MainActivity.SENSOR_X*2, 0.0f, 3.0f, MainActivity.SENSOR_X*2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

            gl10.glTranslatef(0.0f, screenRatio - 1.0f, 0.0f);
            gl10.glRotatef(angle, 0.0f, 0.0f, 1.0f);

            gl10.glScalef(scalingFactor, scalingFactor, 1.0f);
            gl10.glEnable(GL10.GL_BLEND);
            gl10.glShadeModel(GL10.GL_FLAT);
            gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
            gl10.glVertexPointer(coordsPerVertex, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (points));
        }
    }
}