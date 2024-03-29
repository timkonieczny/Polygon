package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Triangle extends Shape {

    private final boolean mIsShadow;
    protected boolean isFading;

    public Triangle(float screenRatio, float[] theme2, boolean isShadow) {
        super(screenRatio);

        coords = new float[]{
         0.0f,  0.0f, 0.0f,// top
        -0.6f, -1.0f, 0.0f,// bottom left
         0.6f, -1.0f, 0.0f // bottom right
        };

        initializeBuffers(theme2);
        mIsShadow=isShadow;
        isFading =false;
        isRGBFaded[0] = isRGBFaded[1] = isRGBFaded[2] =false;
    }

    public void draw(GL10 gl10) {

        if(isFading){
            if(!isRGBFaded[0] ||!isRGBFaded[1] ||!isRGBFaded[2]) {
                fadeColor();
            }else{
                isFading=false;
            }
        }else{
            isRGBFaded[0] = isRGBFaded[1] = isRGBFaded[2] =false;
            isAddRGBSet =false;
        }

        gl10.glLoadIdentity();   // reset the matrix to its default state
        if(!mIsShadow) {
            GLU.gluLookAt(gl10, MainActivity.SENSOR_Y * 2, 0.0f, 3.0f, MainActivity.SENSOR_Y * 2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }else{
            GLU.gluLookAt(gl10, MainActivity.SENSOR_Y +0.1f, 0.0f, 3.0f, MainActivity.SENSOR_Y +0.1f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }
        gl10.glTranslatef(0.0f, screenRatio-2.0f, 0.0f);
//        gl10.glRotatef(90f,0.0f,0.0f,1.0f);
        gl10.glScalef(0.1f, 0.1f, 1.0f);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glShadeModel(GL10.GL_FLAT);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl10.glVertexPointer(coordsPerVertex, GL10.GL_FLOAT, 0, vertexBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, coords.length / coordsPerVertex);
    }
}
