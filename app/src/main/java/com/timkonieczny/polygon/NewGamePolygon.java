package com.timkonieczny.polygon;

import javax.microedition.khronos.opengles.GL10;

public class NewGamePolygon extends Polygon {

    public NewGamePolygon(float screenRatio, float[] theme) {
        super(screenRatio, theme);
        isExpanded=true;
    }

    @Override
    public void draw(GL10 gl10) {

        if(!isExpanded) {
            if (scalingFactor < fullExpansion) {
                scalingFactor += GLRenderer.TSLF*0.005;
                if(scalingFactor >= fullExpansion){
                    gl10.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
                }
            } else {
                isExpanded = true;
                scalingFactor=0.0f;
            }
        }

        drawGL(gl10);
    }
}
