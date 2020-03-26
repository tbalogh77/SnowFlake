/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

public class  Vec3 {

    public static final int mnValCount = 3;
    float vals[] = { 0.f, 0.f, 0.f };

    /**TOMTOMTOM**/
    public Vec3(float aX, float aY, float aZ) {
        setX(aX);
        setY(aY);
        setZ(aZ);
    }
    public Vec3() {
        setX(0);
        setY(0);
        setZ(0);
    }
    public Vec3(Vec3 other) {
        set(other);
    }
    public Vec3 set(float aX, float aY, float aZ) {
        setX(aX);
        setY(aY);
        setZ(aZ);
        return this;
    }
    public Vec3 set(Vec3 other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] = other.vals[i];
        }
        return this;
    }
    public Vec3 plus(Vec3 other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] += other.vals[i];
        }
        return this;
    }
    public Vec3 divideByElements(Vec3 other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] /= other.vals[i];
        }
        return this;
    }
    public Vec3 intElements() {
        for (int i=0; i<mnValCount; i++) {
            vals[i] = (int)vals[i];
        }
        return this;
    }
    public Vec3 multiByElements(Vec3 other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] *= other.vals[i];
        }
        return this;
    }
    public Vec3 minus(Vec3 other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] -= other.vals[i];
        }
        return this;
    }
    public Vec3 multi(float fMultiplier) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] *= fMultiplier;
        }
        return this;
    }
    public float dot(Vec3 other) {
        float res = 0;
        for (int i=0; i<mnValCount; i++) {
            res += vals[i] * other.vals[i];
        }
        return res;
    }
    public Vec3 div(float fMultiplier) {
        if (0.f == fMultiplier) return this;
        for (int i=0; i<mnValCount; i++) {
            vals[i] /= fMultiplier;
        }
        return this;
    }
    public Vec3 normalize() {
        float fLength = length();
        return div(fLength);
    }
    static Vec3 minus(Vec3 v1, Vec3 v2) {
        Vec3 retVec = new Vec3();
        for ( int i=0; i<mnValCount; i++) {
            retVec.vals[i] = v1.vals[i] - v2.vals[i];
        }
        return retVec;
    }
    static Vec3 plus(Vec3 v1, Vec3 v2) {
        Vec3 retVec = new Vec3();
        for ( int i=0; i<mnValCount; i++) {
            retVec.vals[i] = v1.vals[i] + v2.vals[i];
        }
        return retVec;
    }
    public float length() {
        float fLength = 0.f;
        for (int i=0; i<mnValCount; i++) {
            float fVal = vals[i];
            fLength += fVal * fVal;
        }
        //return 0.f != fLength ?  (float)Math.sqrt(fLength) : 0.f;
        return (float)Math.sqrt(fLength);
    }
    public void swapXY() {
        float tempX = x();
        setX(y());
        setY(tempX);
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
    float [] getVals() {
        return vals;
    }
}