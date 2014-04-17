package com.timkonieczny.polygon;

public class ColorTheme {

    protected float[][] theme;

    public ColorTheme(float[] color1, float[] color2, float[] color3){  // colors for outer Circle, obstacles and triangle
        theme = new float[][]{color1, color2, color3};
    }
}
