package com.timkonieczny.polygon;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleSet {

    protected Obstacle[] mObstacles;
    protected ObstacleShadow[] mObstacleShadows;

    public ObstacleSet(float screenRatio, ColorTheme theme, int setIndex) {

        switch (setIndex){
            case 0:         // dependant on index, number of Obstacles being created for that very set
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                };

                mObstacleShadows = new ObstacleShadow[]{
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                };
            case 1:
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                };

                mObstacleShadows = new ObstacleShadow[]{
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                };
            case 2:
                mObstacles = new Obstacle[]{
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 180.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 270.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 0.0f, 0.2f),
                        new Obstacle(screenRatio, 0.5f, theme.theme[1], 90.0f, 0.2f),
                };

                mObstacleShadows = new ObstacleShadow[]{
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 180.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 270.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 0.0f, 0.2f),
                        new ObstacleShadow(screenRatio, 0.5f, theme.theme[3], 90.0f, 0.2f),
                };
        }
    }

    protected void draw(GL10 gl10){
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
        for(ObstacleShadow i: mObstacleShadows){
            i.updateColor(theme.theme[3]);
        }
    }

    protected void resetValues(){
        for(Obstacle i: mObstacles){
            i.resetValues();
        }
        for(ObstacleShadow i: mObstacleShadows){
            i.resetValues();
        }
    }
}
