package com.timkonieczny.polygon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Shape {

    protected final int coordsPerVertex;

    protected float r,g,b,a;
    protected FloatBuffer mColorBuffer;
    protected float mCoords[];

    protected FloatBuffer mVertexBuffer;
    private ByteBuffer mVertexByteBuffer;
    private ByteBuffer mColorByteBuffer;

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

        for(int i=0; i< 4*mCoords.length/3; i+=4){
            mColorBuffer.put(i, r);
            mColorBuffer.put(i+1, g);
            mColorBuffer.put(i+2, b);
            mColorBuffer.put(i+3, a);
        }
    }

    public void initializeBuffers(float theme[]){
        mVertexByteBuffer = ByteBuffer.allocateDirect(mCoords.length * 4);
        mVertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = mVertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(mCoords);
        mVertexBuffer.position(0);

        mColorByteBuffer = ByteBuffer.allocateDirect(16*mCoords.length/3);
        mColorByteBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = mColorByteBuffer.asFloatBuffer();
        updateColor(theme);
        mColorBuffer.position(0);
    }
}
