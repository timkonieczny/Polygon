package com.timkonieczny.polygon;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{

    private float mScreenRatio;

    private Thread[] mPolygonThreads, mObstacleSetThreads;

    public static boolean SCREEN_TOUCHED;
    protected static float X;              // OpenGL Coordinate System
    protected static float Y;
    protected static long TSLF;
    private long mTimeBeforeDrawing;
    private int mElapsedTime;
    private boolean mFirstFrame;
    private int innerCircleIndex;

    private Polygon[] mPolygons;
    private Triangle mTriangle;
    private ColorTheme[] mThemes;
    private ObstacleSet[] mObstacleSets;
    private int mObstacleSetIndex;

    private Random random;

    private int outerThemeIndex, innerThemeIndex;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        gl10.glViewport(0, 0, width, height);
        mScreenRatio =(float)height/width;

        mThemes = new ColorTheme[]{
                new ColorTheme(
                        new float[]{1.0f, 1.0f, 1.0f, 1.0f},    // white circle
                        new float[]{0.0f, 0.0f, 1.0f, 1.0f},    // blue obstacle
                        new float[]{1.0f, 1.0f, 0.0f, 1.0f}     // yellow triangle
                ),
                new ColorTheme(
                        new float[]{1.0f, 0.0f, 0.0f, 1.0f},    // red
                        new float[]{1.0f, 1.0f, 0.0f, 1.0f},    // yellow
                        new float[]{0.0f, 0.0f, 0.0f, 1.0f}     // black
                ),
                new ColorTheme(
                        new float[]{0.0f, 1.0f, 1.0f, 1.0f},    // cyan
                        new float[]{1.0f, 0.0f, 1.0f, 1.0f},    // magenta
                        new float[]{1.0f, 1.0f, 1.0f, 1.0f}     // white
                )
        };

        innerThemeIndex =0;
        random = new Random();
        chooseTheme();

        mPolygons=new Polygon[2];

        mPolygonThreads = new Thread[]{
            new Thread(
                new Runnable(){
                    public void run(){
                        mPolygons[0]=new Polygon(mScreenRatio, mThemes[innerThemeIndex]);
                        mPolygons[0].isExpanded=true;
                    }
                }
            ),
            new Thread(
                new Runnable(){
                    public void run(){
                        mPolygons[1]=new Polygon(mScreenRatio, mThemes[outerThemeIndex]);
                    }
                }
            )
        };

        for(Thread i: mPolygonThreads){
            i.start();
        }

        mObstacleSets=new ObstacleSet[3];

        mObstacleSetThreads = new Thread[]{
            new Thread(
                new Runnable(){
                    public void run(){
                        mObstacleSets[0]=new ObstacleSet(mScreenRatio, mThemes[outerThemeIndex], 0);
                    }
                }
            ),
            new Thread(
                new Runnable(){
                    public void run(){
                        mObstacleSets[1]=new ObstacleSet(mScreenRatio, mThemes[outerThemeIndex], 1);
                    }
                }
            ),
            new Thread(
                new Runnable(){
                    public void run(){
                        mObstacleSets[2]=new ObstacleSet(mScreenRatio, mThemes[outerThemeIndex], 2);
                    }
                }
            )
        };

        for(Thread i: mObstacleSetThreads){
            i.start();
        }

        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glFrustumf(-1, 1, -mScreenRatio, mScreenRatio, 3, 4);  // apply the projection matrix
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        TSLF =0;
        mTimeBeforeDrawing=0;
        mElapsedTime=0;
        mFirstFrame=true;
        innerCircleIndex = 0;

        mTriangle = new Triangle(mScreenRatio, mThemes[outerThemeIndex]);

        mObstacleSetIndex=0;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        TSLF =SystemClock.elapsedRealtime()- mTimeBeforeDrawing;
        if(TSLF*0.001>0.3){      // fix for massive frame bumps right after onCreate() is being called
            TSLF=20;
        }
        mTimeBeforeDrawing=SystemClock.elapsedRealtime();

        if(!mFirstFrame) {
            mElapsedTime+= TSLF;
            if (mElapsedTime >= 10000) {
                mElapsedTime = 0;
                mPolygons[innerCircleIndex].isExpanded =false;
                if(innerCircleIndex ==0) {
                    innerCircleIndex = 1;
                }else {
                    innerCircleIndex =0;
                }
                mPolygons[innerCircleIndex].scalingFactor=0.0f;

                chooseTheme();
                mPolygons[innerCircleIndex].updateColor(mThemes[innerThemeIndex], 0);
                for(ObstacleSet i: mObstacleSets){
                    i.updateColor(mThemes[outerThemeIndex]);
                }

                mTriangle.updateColor(mThemes[outerThemeIndex], 2);
            }
        }else{
            mFirstFrame=false;
            try {
                for(Thread i:mPolygonThreads){
                    i.join();
                }

                for(Thread i:mObstacleSetThreads){
                    i.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mTimeBeforeDrawing=SystemClock.elapsedRealtime();
        }

        if(innerCircleIndex ==0) {
            mPolygons[1].draw(gl10);
            mPolygons[0].draw(gl10);
        }else{
            mPolygons[0].draw(gl10);
            mPolygons[1].draw(gl10);
        }

        mTriangle.draw(gl10);

        if(mObstacleSets[mObstacleSetIndex].mObstacles[mObstacleSets[mObstacleSetIndex].mObstacles.length-1].isExpanded){

            mObstacleSets[mObstacleSetIndex].resetValues();
            if(mObstacleSetIndex<2) {
                mObstacleSetIndex++;
            }else{
                mObstacleSetIndex=0;
            }
        }

        mObstacleSets[mObstacleSetIndex].draw(gl10);
    }

    private void chooseTheme(){
        outerThemeIndex=innerThemeIndex;
        while (innerThemeIndex==outerThemeIndex) {
            innerThemeIndex = random.nextInt(mThemes.length);
        }
    }
}