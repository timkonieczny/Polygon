package com.timkonieczny.polygon;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{

    private float mScreenRatio;

    private Thread mPolygonThread;

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
    private Obstacle[] mObstacles;
    private ColorTheme[] mThemes;

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

        mPolygonThread=new Thread(  //TODO: individual threads for every polygon
            new Runnable(){
                public void run(){
                    mPolygons=new Polygon[]{new Polygon(mScreenRatio, mThemes[innerThemeIndex]), new Polygon(mScreenRatio, mThemes[outerThemeIndex])};
                }
            }
        );

        mPolygonThread.start();

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
        mObstacles=new Obstacle[]{new Obstacle(mScreenRatio, 0.5f, mThemes[outerThemeIndex], 0)};  // pieSize=1 equals half a circle
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        TSLF =SystemClock.elapsedRealtime()- mTimeBeforeDrawing;

        if(TSLF>=100){      // fix for massive frame bumps right after onCreate() is being called
            TSLF=20;        // TODO: different value for different performances?
        }
        mTimeBeforeDrawing=SystemClock.elapsedRealtime();

        if(!mFirstFrame) {
            mElapsedTime+= TSLF;
            if (mElapsedTime >= 10000) {
                mElapsedTime = 0;
                mPolygons[innerCircleIndex].expand =true;
                if(innerCircleIndex ==0) {
                    innerCircleIndex = 1;
                }else {
                    innerCircleIndex =0;
                }
                mPolygons[innerCircleIndex].scalingFactor=0.0f;

                chooseTheme();
                mPolygons[innerCircleIndex].refreshColor(mThemes[innerThemeIndex], 0);
                mObstacles[0].refreshColor(mThemes[outerThemeIndex], 1);
                mTriangle.refreshColor(mThemes[outerThemeIndex], 2);
            }
        }else{
            mFirstFrame=false;
            try {
                mPolygonThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(innerCircleIndex ==0) {
            mPolygons[1].draw(gl10);
            mPolygons[0].draw(gl10);
        }else{
            mPolygons[0].draw(gl10);
            mPolygons[1].draw(gl10);
        }

        mTriangle.draw(gl10);

        mObstacles[0].draw(gl10);
    }

    private void chooseTheme(){
        outerThemeIndex=innerThemeIndex;
        while (innerThemeIndex==outerThemeIndex) {
            innerThemeIndex = random.nextInt(mThemes.length);
        }
    }
}