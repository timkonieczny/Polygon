package com.timkonieczny.polygon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Shape {

    protected final int coordsPerVertex;

    protected float r,g,b,a;
    protected FloatBuffer colorBuffer;
    protected float coords[];

    protected FloatBuffer vertexBuffer;
    protected ByteBuffer mVertexByteBuffer;
    protected ByteBuffer colorByteBuffer;

    protected float scalingFactor;
    protected final float screenRatio;
    protected final float fullExpansion;   // scaling factor for circle expanding over entire screen
    protected boolean isExpanded;

    public Shape(float screenRatio){
        coordsPerVertex=3;
        this.screenRatio =screenRatio;
        fullExpansion =(float)Math.sqrt((double)(2.0f+4* this.screenRatio * this.screenRatio -4* this.screenRatio));
        scalingFactor=0.0f;
    }

    public void updateColor(float[] theme){

        r = theme[0];
        g = theme[1];
        b = theme[2];
        a = theme[3];

        for(int i=0; i< 4* coords.length/3; i+=4){
            colorBuffer.put(i, r);
            colorBuffer.put(i + 1, g);
            colorBuffer.put(i + 2, b);
            colorBuffer.put(i + 3, a);
        }
    }

    public void initializeBuffers(float theme[]){
        mVertexByteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
        mVertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = mVertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        colorByteBuffer = ByteBuffer.allocateDirect(16* coords.length/3);
        colorByteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = colorByteBuffer.asFloatBuffer();
        updateColor(theme);
        colorBuffer.position(0);
    }
}
