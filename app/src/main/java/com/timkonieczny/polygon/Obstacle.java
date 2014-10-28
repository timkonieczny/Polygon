package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Obstacle extends Shape{

    protected int points;   // must be even

    protected float angle;
    protected float scalingOffset;

    protected boolean offsetPermission=false;
    private final float scalingDefault, angleDefault;

    private final boolean mIsShadow;

    private float mOuterCollisionRadius;

    private float mLastFrameScalingFactor = 0;

    public Obstacle(float screenRatio, float pieSize, float[] theme, float angleOffset, float scalingOffset, boolean isShadow){
        super(screenRatio);     // pieSize between 0 and 2
        angleDefault=angleOffset;
        scalingDefault=scalingOffset;
        points =(int)(pieSize*50);
        double innerRadius=1.0;
        double outerRadius=1.2;
        mOuterCollisionRadius = 1f/1.2f;

        scalingFactor=0.0f;

        resetValues();

        if(points %2==1){
            points++;
        }

        coords = new float[(points) * coordsPerVertex];
        int j=0;

        for (int i= 0; i< coords.length-6; i+= coordsPerVertex*2) {

            double sectionOfPi = j*(double)1/(points -2)*pieSize*(2*Math.PI);
            j++;

            coords[i] = (float) (innerRadius*Math.cos(sectionOfPi));      // x
            coords[i+3] = (float) (outerRadius*Math.cos(sectionOfPi));    // x

            if(coords[i]==(float)-innerRadius|| coords[i]==(float)innerRadius){        //Regelung für Spezialfälle von cos() und sin()
                coords[i+1] = 0.0f;    // y
                coords[i+4] = 0.0f;    // y
            }else {
                coords[i+1] = (float) (innerRadius*Math.sin(sectionOfPi));    // y
                coords[i+4] = (float) (outerRadius*Math.sin(sectionOfPi));    // y
            }

            coords[i+2] = 0.0f;    // z
            coords[i+5] = 0.0f;    // z
        }

        coords[coords.length-6]=(float) (innerRadius*Math.cos(j*(double)1/(points -2)*pieSize*(2*Math.PI)));      // x    Letztes Koordinatenpaar
        coords[coords.length-3]=(float) (outerRadius*Math.cos(j*(double)1/(points -2)*pieSize*(2*Math.PI)));      // x
        coords[coords.length-5]=1.0f; //y
        coords[coords.length-2]=1.2f; //y
        coords[coords.length-4]=0.0f; //z
        coords[coords.length-1]=0.0f; //z

        initializeBuffers(theme);

        mIsShadow=isShadow;
    }

    public void resetValues(){
        scalingOffset =scalingDefault;
        angle =angleDefault;
        isExpanded =false;
        scalingFactor=0.0f;
        offsetPermission=false;
    }

    public void draw(GL10 gl10, boolean foreignOffsetPermission) {

        if(!GLRenderer.GAME_OVER) {
            if (GLRenderer.SCREEN_TOUCHED) {
                if (GLRenderer.X <= 0) {
                    angle += GLRenderer.TSLF / 2;
                } else {
                    angle -= GLRenderer.TSLF / 2;
                }
            }
        }
        if (foreignOffsetPermission) {
            if(!GLRenderer.GAME_OVER) {
                if (scalingFactor < fullExpansion) {

                    if (GLRenderer.TSLF < 100) {
                        scalingFactor += GLRenderer.TSLF * 0.001;
                    } else {
                        scalingFactor += 0.05;  // fix for big frame bumps
                    }

                    if(mLastFrameScalingFactor<0.8333333f && 0.8333333f<scalingFactor){     // fix for imprecise collision detection due to frame bumps
                        scalingFactor = 0.8333333f;
                    }

                    mLastFrameScalingFactor = scalingFactor;

                    if (scalingFactor >= scalingOffset) {
                        offsetPermission = true;
                    }
                } else {
                    isExpanded = true;
                    isRGBFaded[0] = isRGBFaded[1] = isRGBFaded[2] = false;
                    isAddRGBSet = false;
                }

                if (!mIsShadow && scalingFactor >= mOuterCollisionRadius && scalingFactor < 1.0f) {
                    collisionCheck(angle);
                }
            }
            gl10.glLoadIdentity();   // reset the matrix to its default state

            if(!mIsShadow) {
                GLU.gluLookAt(gl10, MainActivity.SENSOR_Y * 2, 0.0f, 3.0f, MainActivity.SENSOR_Y * 2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            }else{
                GLU.gluLookAt(gl10, MainActivity.SENSOR_Y +0.1f, 0.0f, 3.0f, MainActivity.SENSOR_Y +0.1f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            }
            gl10.glTranslatef(0.0f, screenRatio - 1.0f, 0.0f);
            gl10.glRotatef(angle, 0.0f, 0.0f, 1.0f);

            gl10.glScalef(scalingFactor, scalingFactor, 1.0f);
            gl10.glEnable(GL10.GL_BLEND);
            gl10.glShadeModel(GL10.GL_FLAT);
            gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
            gl10.glVertexPointer(coordsPerVertex, GL10.GL_FLOAT, 0, vertexBuffer);
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (points));
        }
    }

    private void collisionCheck(float angle){
        float collisionAngle;
        if(angle<0f){
            collisionAngle =360-(Math.abs(angle)%360);
        }else{
            collisionAngle =angle%360;
        }

        if (collisionAngle >= 180.0f && collisionAngle < 270.0f) {
            // TODO: game over
            GLRenderer.GAME_OVER=true;
        }
    }
}