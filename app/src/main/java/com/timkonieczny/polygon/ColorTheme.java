package com.timkonieczny.polygon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ColorTheme {
    //TODO: testen: List<Integer> items = Collections.unmodifiableList(Arrays.asList(0,1,2,3));
    protected float[][] theme;
    protected List<float[]> testTheme;
    protected float a0, a1, a2, a3, b0, b1, b2, b3, c0, c1, c2, c3, d0, d1, d2, d3;

    public ColorTheme(float[] color1, float[] color2, float[] color3, float[] color4){  // colors for outer Circle, obstacles, triangle and shadow
        theme = new float[][]{color1, color2, color3, color4};
        List<float[]> list = Arrays.asList(theme);

        a0=color1[0];   //Werte ändern sich NICHT!
        a1=color1[1];
        a2=color1[2];
        a3=color1[3];
        b0=color2[0];
        b1=color2[1];
        b2=color2[2];
        b3=color2[3];
        c0=color3[0];
        c1=color3[1];
        c2=color3[2];
        c3=color3[3];
        d0=color4[0];
        d1=color4[1];
        d2=color4[2];
        d3=color4[3];




        testTheme = Collections.unmodifiableList(list);
    }
}
