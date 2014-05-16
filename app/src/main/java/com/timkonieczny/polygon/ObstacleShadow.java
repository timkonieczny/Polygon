package com.timkonieczny.polygon;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleShadow extends Obstacle{
    public ObstacleShadow(float screenRatio, float pieSize, float[] theme, float angleOffset, float scalingOffset){
        super(screenRatio, pieSize, theme, angleOffset, scalingOffset);
    }

    @Override
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

            gl10.glLoadIdentity();   // reset the matrix to its default state
            GLU.gluLookAt(gl10, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

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
