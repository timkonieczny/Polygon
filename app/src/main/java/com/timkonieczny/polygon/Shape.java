package com.timkonieczny.polygon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Shape {

    protected final int coordsPerVertex;

    protected float[] rgba;
    protected FloatBuffer colorBuffer;
    protected float coords[];

    protected FloatBuffer vertexBuffer;
    protected ByteBuffer mVertexByteBuffer;
    protected ByteBuffer colorByteBuffer;

    protected float scalingFactor;
    protected final float screenRatio;
    protected final float fullExpansion;   // scaling factor for circle expanding over entire screen
    protected boolean isExpanded;

    protected boolean isAddRGBSet;
    protected boolean[] isRGBFaded, addOrSub;
    protected float[] current;

    public Shape(float screenRatio){
        coordsPerVertex=3;
        this.screenRatio =screenRatio;
        fullExpansion =(float)Math.sqrt((double)(2.0f+4* this.screenRatio * this.screenRatio -4* this.screenRatio));
        scalingFactor=0.0f;
        current=new float[3];
        isRGBFaded=new boolean[3];
        addOrSub=new boolean[3];
    }

    public void updateColor(float[] theme){
        rgba=theme;

        for(int i=0; i< 4* coords.length/3; i+=4){
            colorBuffer.put(i, rgba[0]);
            colorBuffer.put(i + 1, rgba[1]);
            colorBuffer.put(i + 2, rgba[2]);
            colorBuffer.put(i + 3, rgba[3]);
        }
    }

    public void initializeBuffers(float[] theme){
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

    protected void fadeColor(){

        if(!isAddRGBSet) {
            addOrSub[0] = current[0] <= rgba[0];
            addOrSub[1] = current[1] <= rgba[1];
            addOrSub[2] = current[2] <= rgba[2];
            isAddRGBSet =true;
        }

        fadeRGB(0);
        fadeRGB(1);
        fadeRGB(2);

        for(int i=0; i< 4* coords.length/3; i+=4){
            colorBuffer.put(i, this.current[0]);
            colorBuffer.put(i+1, current[1]);
            colorBuffer.put(i+2, current[2]);
        }
    }

    private void fadeRGB(int i){
        if(addOrSub[i]){
            if(current[i]<rgba[i]){
                current[i]+= GLRenderer.TSLF * 0.001;
            }else{
                current[i]=rgba[i];
                isRGBFaded[i] =true;
            }
        }else{
            if(current[i]>rgba[i]){
                current[i]-= GLRenderer.TSLF * 0.001;
            }else{
                current[i]=rgba[i];
                isRGBFaded[i] =true;
            }
        }
    }
}
