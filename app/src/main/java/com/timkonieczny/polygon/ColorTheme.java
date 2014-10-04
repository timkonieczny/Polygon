package com.timkonieczny.polygon;

public class ColorTheme {
    protected float[][] theme;

    public ColorTheme(float[] color1, float[] color2, float[] color3, float[] color4){  // colors for outer Circle, obstacles, triangle and shadow
        theme = new float[][]{color1, color2, color3, color4};
    }
}
