package com.studentsteam.sonic.tools;

/**
 * Class for linear interpolation
 */
public class Lerp {
    public static float interpolate(float point1,float point2,float alpha){
        return point1 + alpha * (point2 - point1);
    }
}
