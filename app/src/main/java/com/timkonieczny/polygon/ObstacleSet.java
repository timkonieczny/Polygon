package com.timkonieczny.polygon;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleSet {

    protected Obstacle[] mObstacles;
    protected Obstacle[] mObstacleShadows;
    protected static float CURRENT_R, CURRENT_G, CURRENT_B;
    protected boolean isAddRGBSet, isRFaded, isGFaded, isBFaded, isFading, addR, addG, addB;

    public ObstacleSet(float screenRatio, ColorTheme theme, int setIndex) {

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
        }
        isAddRGBSet=false;
    }

    private void fadeColor(){

        if(!isAddRGBSet) {
            addR = ObstacleSet.CURRENT_R <= mObstacleShadows[0].rgba[0];
            addG = ObstacleSet.CURRENT_G <= mObstacleShadows[0].rgba[1];
            addB = ObstacleSet.CURRENT_B <= mObstacleShadows[0].rgba[2];
            isAddRGBSet =true;
        }

        if(addR){
            if(ObstacleSet.CURRENT_R<mObstacleShadows[0].rgba[0]){
                ObstacleSet.CURRENT_R+= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_R=mObstacleShadows[0].rgba[0];
                isRFaded =true;
            }
        }else{
            if(ObstacleSet.CURRENT_R>mObstacleShadows[0].rgba[0]){
                ObstacleSet.CURRENT_R-= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_R=mObstacleShadows[0].rgba[0];
                isRFaded =true;
            }
        }

        if(addG){
            if(ObstacleSet.CURRENT_G<mObstacleShadows[0].rgba[1]){
                ObstacleSet.CURRENT_G+= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_G=mObstacleShadows[0].rgba[1];
                isGFaded =true;
            }
        }else{
            if(ObstacleSet.CURRENT_G>mObstacleShadows[0].rgba[1]){
                ObstacleSet.CURRENT_G-= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_G=mObstacleShadows[0].rgba[1];
                isGFaded =true;
            }
        }

        if(addB){
            if(ObstacleSet.CURRENT_B<mObstacleShadows[0].rgba[2]){
                ObstacleSet.CURRENT_B+= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_B=mObstacleShadows[0].rgba[2];
                isBFaded =true;
            }
        }else{
            if(ObstacleSet.CURRENT_B>mObstacleShadows[0].rgba[2]){
                ObstacleSet.CURRENT_B-= GLRenderer.TSLF * 0.001;
            }else{
                ObstacleSet.CURRENT_B=mObstacleShadows[0].rgba[2];
                isBFaded =true;
            }
        }

        for(int i=0; i<mObstacleShadows.length; i++){
            for(int j=0; j< 4* mObstacleShadows[i].coords.length/3; j+=4){
                mObstacleShadows[i].colorBuffer.put(j, CURRENT_R);
                mObstacleShadows[i].colorBuffer.put(j + 1, CURRENT_G);
                mObstacleShadows[i].colorBuffer.put(j + 2, CURRENT_B);
//                mObstacleShadows[i].colorBuffer.put(j + 3, 1.0f);
            }
        }
    }

    protected void draw(GL10 gl10){
        if(isFading) {
            if (!isRFaded || !isGFaded || !isBFaded) {
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
        isRFaded=isGFaded=isBFaded=false;
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
