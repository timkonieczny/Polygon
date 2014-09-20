package com.timkonieczny.polygon;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleSet {

    protected Obstacle[] mObstacles;
    protected Obstacle[] mObstacleShadows;
    private static float[] CURRENT;
    private boolean isAddRGBSet, isFading;
    private boolean[] isRGBFaded, addOrSub;

    public ObstacleSet(float screenRatio, ColorTheme theme, int setIndex) {

        ObstacleSet.CURRENT =new float[]{theme.theme[3][0],theme.theme[3][1],theme.theme[3][2]};
        addOrSub=new boolean[3];
        isRGBFaded=new boolean[3];

        switch (setIndex){
            case 0:         // dependant on index, number of Obstacles being created for that very set
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                };

                mObstacleShadows = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                };

//                mObstacles = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
//                };
//
//                mObstacleShadows = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
//                };
            case 1:
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                };

                mObstacleShadows = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                };

//                mObstacles = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
//                };
//
//                mObstacleShadows = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
//                };
            case 2:
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f, false),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
                };

                mObstacleShadows = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f, true),
                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
                };

//                mObstacles = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f, false),
//                };
//
//                mObstacleShadows = new Obstacle[]{
//                        new Obstacle(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f, true),
//                };
        }
        isAddRGBSet=false;
    }

    private void fadeColor(){

        if(!isAddRGBSet) {
            addOrSub[0] = ObstacleSet.CURRENT[0] <= mObstacleShadows[0].rgba[0];
            addOrSub[1] = ObstacleSet.CURRENT[1] <= mObstacleShadows[0].rgba[1];
            addOrSub[2] = ObstacleSet.CURRENT[2] <= mObstacleShadows[0].rgba[2];
            isAddRGBSet =true;
        }

        fadeRGB(0);
        fadeRGB(1);
        fadeRGB(2);

        for (Obstacle i : mObstacleShadows) {
            for (int j = 0; j < 4 * i.coords.length / 3; j += 4) {
                i.colorBuffer.put(j, ObstacleSet.CURRENT[0]);
                i.colorBuffer.put(j + 1, ObstacleSet.CURRENT[1]);
                i.colorBuffer.put(j + 2, ObstacleSet.CURRENT[2]);
            }
        }
    }

    private void fadeRGB(int i){
        if(addOrSub[i]){
            if(ObstacleSet.CURRENT[i]<mObstacleShadows[0].rgba[i]){
                ObstacleSet.CURRENT[i]+= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT[i]=mObstacleShadows[0].rgba[i];
                isRGBFaded[i] =true;
            }
        }else{
            if(ObstacleSet.CURRENT[i]>mObstacleShadows[0].rgba[i]){
                ObstacleSet.CURRENT[i]-= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT[i]=mObstacleShadows[0].rgba[i];
                isRGBFaded[i] =true;
            }
        }
    }

    protected void draw(GL10 gl10){
        if(isFading) {
            if (!isRGBFaded[0] || !isRGBFaded[1] || !isRGBFaded[2]) {
                fadeColor();
            }else{
                isFading =false;
            }
        }

        mObstacleShadows[0].draw(gl10, true);
        for(int i=1; i<mObstacleShadows.length; i++){
            mObstacleShadows[i].draw(gl10, mObstacles[i-1].offsetPermission);
        }
        mObstacles[0].draw(gl10, true);
        for(int i=1; i<mObstacles.length; i++){
            mObstacles[i].draw(gl10, mObstacles[i-1].offsetPermission);
        }
    }

    protected void updateColor(ColorTheme theme){
        for(Obstacle i: mObstacles){
            i.updateColor(theme.theme[1]);
        }


        for(Obstacle i: mObstacleShadows){
            i.updateColor(theme.theme[3]);
        }

        isFading=true;
        isRGBFaded[0]=isRGBFaded[1]=isRGBFaded[2]=false;
        isAddRGBSet=false;
    }

    protected void resetValues(){
        for(Obstacle i: mObstacles){
            i.resetValues();
        }
        for(Obstacle i: mObstacleShadows){
            i.resetValues();
        }
    }
}
