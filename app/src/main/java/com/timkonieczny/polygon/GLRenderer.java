package com.timkonieczny.polygon;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{

    public static boolean SCREEN_TOUCHED;
    protected static float X;              // OpenGL Coordinate System
    protected static float Y;
    private float mScreenRatio;
    protected static long TSLF;
    private long mTimeBeforeDrawing;
    private int mElapsedTime;
    private boolean mFirstFrame;
    private int innerCircleIndex, outerCircleIndex;

    private Polygon[] mPolygons;
    private Triangle mTriangle;
    private Obstacle mObstacle;
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

        mScreenRatio=(float)height/width;

        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glFrustumf(-1, 1, -mScreenRatio, mScreenRatio, 3, 4);  // apply the projection matrix
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        TSLF =0;
        mElapsedTime=0;
        mFirstFrame=true;
        innerCircleIndex = 0;
        outerCircleIndex = 1;
        random = new Random();


        mThemes = new ColorTheme[]{
                new ColorTheme(
                        new float[]{1.0f, 1.0f, 1.0f, 1.0f},    // white circle
                        new float[]{0.0f, 0.0f, 1.0f, 1.0f},    // blue obstacle obstacles
                        new float[]{1.0f, 1.0f, 0.0f, 1.0f}     // yellow triangle triangle
                ),
                new ColorTheme(
                        new float[]{1.0f, 0.0f, 0.0f, 1.0f},    // red      inner and outer
                        new float[]{1.0f, 1.0f, 0.0f, 1.0f},    // yellow
                        new float[]{0.0f, 0.0f, 0.0f, 1.0f}     // black
                ),
                new ColorTheme(
                        new float[]{0.0f, 1.0f, 1.0f, 1.0f},    // cyan
                        new float[]{1.0f, 0.0f, 1.0f, 1.0f},    // magenta
                        new float[]{1.0f, 1.0f, 1.0f, 1.0f}     // white
                )
        };

        outerThemeIndex = innerThemeIndex =0;
        chooseTheme();
        System.out.println("inner theme: "+innerThemeIndex+" outer theme: "+outerThemeIndex);//        inneres                                         äußeres
        mPolygons=new Polygon[]{new Polygon(mScreenRatio, mThemes[innerThemeIndex]), new Polygon(mScreenRatio, mThemes[outerThemeIndex])};    // TODO: color rotation
        System.out.println("[inner: r=]"+mThemes[innerThemeIndex].theme[0][0]+" g="+mThemes[innerThemeIndex].theme[0][1]+" b="+mThemes[innerThemeIndex].theme[0][2]+" a="+mThemes[innerThemeIndex].theme[0][3]);
        System.out.println("[outer: r=]"+mThemes[outerThemeIndex].theme[0][0]+" g="+mThemes[outerThemeIndex].theme[0][1]+" b="+mThemes[outerThemeIndex].theme[0][2]+" a="+mThemes[outerThemeIndex].theme[0][3]);
        mTriangle = new Triangle(mScreenRatio, mThemes[outerThemeIndex]);
        mObstacle = new Obstacle(mScreenRatio, 0.5f, mThemes[outerThemeIndex]); // pieSize=1 equals half a circle
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        TSLF =SystemClock.elapsedRealtime()- mTimeBeforeDrawing;
        mTimeBeforeDrawing=SystemClock.elapsedRealtime();

        if(!mFirstFrame) {
            mElapsedTime+= TSLF;
            if (mElapsedTime >= 10000) {
                mElapsedTime = 0;
                mPolygons[innerCircleIndex].expand =true;
                if(innerCircleIndex ==0) {
                    innerCircleIndex = 1;
                    outerCircleIndex = 0;
                }else {
                    innerCircleIndex =0;
                    outerCircleIndex = 1;
                }
                mPolygons[innerCircleIndex].scalingFactor=0.0f;

                chooseTheme();
                mPolygons[innerCircleIndex].refreshColor(mThemes[innerThemeIndex], 0);
                mObstacle.refreshColor(mThemes[outerThemeIndex], 1);
                mTriangle.refreshColor(mThemes[outerThemeIndex], 2);
            }
        }else{
            mFirstFrame=false;
        }

        if(innerCircleIndex ==0) {
            mPolygons[1].draw(gl10);
            mPolygons[0].draw(gl10);
        }else{
            mPolygons[0].draw(gl10);
            mPolygons[1].draw(gl10);
        }

        mTriangle.draw(gl10);

        mObstacle.draw(gl10);
    }

    private ColorTheme chooseTheme(){
        outerThemeIndex=innerThemeIndex;
        while (innerThemeIndex==outerThemeIndex) {
            innerThemeIndex = random.nextInt(mThemes.length);
        }
        return mThemes[innerThemeIndex];
    }
}