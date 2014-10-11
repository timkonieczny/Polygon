package com.timkonieczny.polygon;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{

    private float mScreenRatio;

    private Thread[] mPolygonThreads, mObstacleSetThreads;

    protected static boolean START_NEW_GAME;
    private boolean mNewGameAnimationFinished=true;
    protected static boolean SCREEN_TOUCHED;
    protected boolean mGameOverScreenTouched;
    protected static float X;              // OpenGL Coordinate System
    protected static float Y;
    protected static long TSLF;
    private long mTimeBeforeDrawing;
    private int mElapsedTime;
    private boolean mFirstFrame;
    private int innerCircleIndex, outerCircleIndex;
    private int dontDrawObstacle;

    private Polygon[] mPolygons;
    private Polygon mNewGamePolygon;
    private Triangle mTriangle, mShadowTriangle;
    private ColorTheme[] mThemes;
    private ObstacleSet[] mObstacleSets;
    private int mObstacleSetIndex;

    private Random random;

    private int outerThemeIndex, innerThemeIndex;

    protected final float[] white= new float[]{1.0f,1.0f,1.0f,1.0f};

    protected static boolean GAME_OVER=false;

    private int mRefreshRate;

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
                        new float[]{0.20f, 0.71f, 0.91f, 1.00f},    // blue circle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white obstacle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white triangle
                        new float[]{0.00f, 0.60f, 0.80f, 1.00f}     // shadow
                ),
                new ColorTheme(
                        new float[]{0.27f, 0.40f, 0.80f, 1.00f},    // purple circle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white obstacle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white triangle
                        new float[]{0.60f, 0.20f, 0.80f, 1.00f}     // shadow
                ),
                new ColorTheme(
                        new float[]{0.60f, 0.80f, 0.00f, 1.00f},    // green circle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white obstacle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white triangle
                        new float[]{0.40f, 0.60f, 0.00f, 1.00f}     // shadow
                ),
                new ColorTheme(
                        new float[]{1.00f, 0.73f, 0.20f, 1.00f},    // orange circle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white obstacle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white triangle
                        new float[]{1.00f, 0.53f, 0.00f, 1.00f}     // shadow
                ),
                new ColorTheme(
                        new float[]{1.00f, 0.27f, 0.27f, 1.00f},    // red circle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white obstacle
                        new float[]{1.00f, 1.00f, 1.00f, 1.00f},    // white triangle
                        new float[]{0.80f, 0.00f, 0.00f, 1.00f}     // shadow
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
                        mPolygons[0]=new Polygon(mScreenRatio);
                        mPolygons[0].isExpanded=true;
                        mPolygons[0].updateColor(white);
                        mPolygons[innerCircleIndex].rgba=mThemes[innerThemeIndex].theme[0];
                    }
                }
            ),
            new Thread(
                new Runnable(){
                    public void run(){
                        mPolygons[1]=new Polygon(mScreenRatio, mThemes[outerThemeIndex].theme[0]);
                    }
                }
            ),
            new Thread(
                new Runnable(){
                    public void run(){
                        mNewGamePolygon=new NewGamePolygon(mScreenRatio, white);
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
        outerCircleIndex = 1;


        mTriangle = new Triangle(mScreenRatio, white,false);
        mShadowTriangle = new Triangle(mScreenRatio, mThemes[outerThemeIndex].theme[3],true);

        // Must set current to deep copy of rgba to avoid change of mThemes[outerThemeIndex].theme[3] which is referenced by rgba (shallow copy)
        mShadowTriangle.current=new float[]{mShadowTriangle.rgba[0],mShadowTriangle.rgba[1],mShadowTriangle.rgba[2],mShadowTriangle.rgba[3]};

        mObstacleSetIndex=0;

        mRefreshRate=10000;
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
            mElapsedTime += TSLF;
            if(!GAME_OVER) {
                if (mElapsedTime >= mRefreshRate) {
                    mElapsedTime = 0;
                    mPolygons[innerCircleIndex].isExpanded = false;
                    if (innerCircleIndex == 0) {
                        innerCircleIndex = 1;
                        outerCircleIndex = 0;
                    } else {
                        innerCircleIndex = 0;
                        outerCircleIndex = 1;
                    }

                    chooseTheme();

                    mPolygons[innerCircleIndex].scalingFactor = 0.0f;
                    mPolygons[innerCircleIndex].updateColor(white);
                    mPolygons[innerCircleIndex].rgba = mThemes[innerThemeIndex].theme[0];

                    for (ObstacleSet i : mObstacleSets) {
                        i.updateColor(mThemes[outerThemeIndex]);
                    }

                    mShadowTriangle.rgba = mThemes[outerThemeIndex].theme[3];
                    mShadowTriangle.isFading = true;

                }
                START_NEW_GAME =false;

                mGameOverScreenTouched =SCREEN_TOUCHED;
            }else{
                mElapsedTime = 0;
                if(START_NEW_GAME) {
                    SCREEN_TOUCHED= START_NEW_GAME=false;
//                    GAME_OVER=false;
//                    mObstacleSets[mObstacleSetIndex].mObstacles[mObstacleSets[mObstacleSetIndex].mObstacles.length-1].isExpanded=true;
                    mNewGamePolygon.isExpanded=false;
                    mPolygons[outerCircleIndex].isExpanded=false;
                    mPolygons[outerCircleIndex].scalingFactor=0.0f;
                    mNewGameAnimationFinished=false;
                    dontDrawObstacle=0;
                }
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

        if(mObstacleSets[mObstacleSetIndex].mObstacles[mObstacleSets[mObstacleSetIndex].mObstacles.length-1].isExpanded){

            mObstacleSets[mObstacleSetIndex].resetValues();
            if(mObstacleSetIndex<2) {
                mObstacleSetIndex++;
            }else{
                mObstacleSetIndex=0;
            }
        }


        if(!mNewGameAnimationFinished) {
            if(!mNewGamePolygon.isExpanded){
                if(dontDrawObstacle<mObstacleSets[mObstacleSetIndex].mObstacles.length&&mNewGamePolygon.scalingFactor*0.8f>=mObstacleSets[mObstacleSetIndex].mObstacles[mObstacleSets[mObstacleSetIndex].mObstacles.length-1-dontDrawObstacle].scalingFactor){
                    dontDrawObstacle++;
                }
                mObstacleSets[mObstacleSetIndex].draw(gl10,dontDrawObstacle);
                mNewGamePolygon.draw(gl10);
            }
            if (mNewGamePolygon.isExpanded && !mPolygons[outerCircleIndex].isExpanded) {
                mPolygons[outerCircleIndex].newGameDraw(gl10);
                if(mPolygons[outerCircleIndex].isExpanded){
                    mNewGameAnimationFinished=true;
                    GAME_OVER=false;
                    mObstacleSets[mObstacleSetIndex].mObstacles[mObstacleSets[mObstacleSetIndex].mObstacles.length-1].isExpanded=true;
                }
            }

            mPolygons[innerCircleIndex].draw(gl10);

            mShadowTriangle.draw(gl10);
            mTriangle.draw(gl10);

            //mObstacleSets[mObstacleSetIndex].draw(gl10);
        }else {
            mPolygons[outerCircleIndex].draw(gl10);
            mPolygons[innerCircleIndex].draw(gl10);

            mShadowTriangle.draw(gl10);
            mTriangle.draw(gl10);

            mObstacleSets[mObstacleSetIndex].draw(gl10,0);
        }
    }

    private void chooseTheme(){
        outerThemeIndex=innerThemeIndex;
        while (innerThemeIndex==outerThemeIndex) {
            innerThemeIndex = random.nextInt(mThemes.length);
        }
    }
}