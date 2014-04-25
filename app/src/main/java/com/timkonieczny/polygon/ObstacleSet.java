package com.timkonieczny.polygon;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleSet {

    private Obstacle[] mObstacles;

    public ObstacleSet(float screenRatio, ColorTheme theme, int setIndex) {

        switch (setIndex){
            case 0:         // dependant on index, number of Obstacles being created for that very set
                mObstacles = new Obstacle[5];
                mObstacles[0]=new Obstacle(screenRatio, 1.0f, theme, 0);    // TODO: set up values for every different set here
                mObstacles[1]=new Obstacle(screenRatio, 1.0f, theme, 0);
                mObstacles[2]=new Obstacle(screenRatio, 1.0f, theme, 0);
                mObstacles[3]=new Obstacle(screenRatio, 1.0f, theme, 0);
                mObstacles[4]=new Obstacle(screenRatio, 1.0f, theme, 0);
                break;
            case 1:
                mObstacles = new Obstacle[4];
                mObstacles[0]=new Obstacle(screenRatio, 1.0f, theme, 0);    // TODO: set up values for every different set here
                mObstacles[1]=new Obstacle(screenRatio, 1.0f, theme, 0);
                mObstacles[2]=new Obstacle(screenRatio, 1.0f, theme, 0);
                mObstacles[3]=new Obstacle(screenRatio, 1.0f, theme, 0);
                break;
        }
    }

    protected void draw(GL10 gl10){
        for(int i=0; i<mObstacles.length; i++){
            mObstacles[i].draw(gl10);
        }
    }
}
