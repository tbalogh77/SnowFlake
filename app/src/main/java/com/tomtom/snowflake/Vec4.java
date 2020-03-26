/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

public class  Vec4 {

    public static final int mnValCount = 4;
    float vals[] = { 0.f, 0.f, 0.f, 0.f };
    float [] getVals() {
        return vals;
    }

    /**TOMTOMTOM*/
    public Vec4(Vec4 vOther) {
        this.set(vOther);
    }
    public Vec4(float aX, float aY, float aZ, float aW) {
        setX(aX);
        setY(aY);
        setZ(aZ);
        setW(aW);
    }
    public Vec4() {
        setX(0);
        setY(0);
        setZ(0);
        setW(0);
    }
    float x() {
        return vals[0];
    }
    void setX(float aX) {
        vals[0] = aX;
    }
    float y() {
        return vals[1];
    }
    void setY(float aY) {
        vals[1] = aY;
    }
    float z() {
        return vals[2];
    }
    void setZ(float aZ) {
        vals[2] = aZ;
    }
    float w() {
        return vals[3];
    }
    void setW(float aW) {
        vals[3] = aW;
    }
    void set(float aX, float aY, float aZ, float aW) {
        // initialize vertex byte buffer for shape coordinates
        setX(aX);
        setY(aY);
        setZ(aZ);
        setW(aW);
    }
    void set(Vec4 vOther) {
        for (int nIndex = 0; nIndex < mnValCount; nIndex++)
            vals[nIndex] = vOther.vals[nIndex];
    }
}