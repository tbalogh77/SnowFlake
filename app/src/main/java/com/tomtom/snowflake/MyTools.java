package com.tomtom.snowflake;

/**
 * Created by t on 2017.08.03..
 */

public class MyTools {
    static float linInt( float fMin, float fMax, float t) {
        return fMin + (fMax-fMin) * t;
    }
    public static float rndInterval(float from, float to) {
        return (float)(from + Math.random() * (to - from));
    }
}
