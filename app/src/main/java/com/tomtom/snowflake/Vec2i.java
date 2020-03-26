package com.tomtom.snowflake;

/**
 * Created by t on 12/28/15.
 */
public class Vec2i {

    public static final int mnValCount = 2;
    int vals[] = { 0, 0 };
    public int [] getVals() {
        return vals;
    }

    /**TOMTOMTOM**/
    public Vec2i(int aX, int aY) {
        setX(aX);
        setY(aY);
    }
    public Vec2i() {
        setX(0);
        setY(0);
    }
    public Vec2i(Vec2i other) {
        set(other);
    }
    public Vec2i(Vec3 other) {
        set((int) other.x(), (int) other.y());
    }
    public Vec2i set(int aX, int aY) {
        setX(aX);
        setY(aY);
        return this;
    }
    public Vec2i set(Vec2i other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] = other.vals[i];
        }
        return this;
    }
    public Vec2i plus(Vec2i other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] += other.vals[i];
        }
        return this;
    }
    public Vec2i divideByElements(Vec2i other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] /= other.vals[i];
        }
        return this;
    }
    public Vec2i multiByElements(Vec2i other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] *= other.vals[i];
        }
        return this;
    }
    public Vec2i minus(Vec2i other) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] -= other.vals[i];
        }
        return this;
    }
    public Vec2i multi(int fMultiplier) {
        for (int i=0; i<mnValCount; i++) {
            vals[i] *= fMultiplier;
        }
        return this;
    }
    public Vec2i div(int fMultiplier) {
        if (0 == fMultiplier) return this;
        for (int i=0; i<mnValCount; i++) {
            vals[i] /= fMultiplier;
        }
        return this;
    }
    static Vec2i minus(Vec2i v1, Vec2i v2) {
        Vec2i retVec = new Vec2i();
        for ( int i=0; i<mnValCount; i++) {
            retVec.vals[i] = v1.vals[i] - v2.vals[i];
        }
        return retVec;
    }
    static Vec2i plus(Vec2i v1, Vec2i v2) {
        Vec2i retVec = new Vec2i();
        for ( int i=0; i<mnValCount; i++) {
            retVec.vals[i] = v1.vals[i] + v2.vals[i];
        }
        return retVec;
    }
    int x() {
        return vals[0];
    }

    void setX(int aX) {
        vals[0] = aX;
    }

    int y() {
        return vals[1];
    }

    void setY(int aY) {
        vals[1] = aY;
    }
}