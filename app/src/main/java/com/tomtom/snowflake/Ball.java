/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

import android.util.Log;

public class  Ball extends vbo {

    private static final boolean mbDrawCollide = true;
    private static final boolean mbOneCollideInCycle = true;

    private Vec3 mvOldPos = new Vec3();
    public  Vec3 mvVel = new Vec3(0.0000f, 0.0000f, 0);
    public boolean mbHaveCollided = false;
    public static float mfMinDist = 1000.0f;

    protected int id = 0;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setVel(Vec3 vel) {
        mvVel.set(vel);
    }
    /**
     * TOMTOMTOM
     */
    public Ball() {
        super();
    }
    public Ball(Vec3 size) {
        super(size);
    }
    void climp(int coord, float fMin, float fMax) {
        if (mPos.vals[coord] < fMin) {
            Log.e("Ball", " Climped up from " + mPos.vals[coord] + " to " + fMin);
            mvVel.vals[coord] *= -1.f;
            mPos.vals[coord] = fMin;
        } else if (fMax - mSizes.getVals()[coord] < mPos.vals[coord]) {
            Log.e("Ball", " Climped down from " + mPos.vals[coord] + " to " + fMax);
            mvVel.vals[coord] *= -1.f;
            mPos.vals[coord] = fMax - mSizes.getVals()[coord];
        }
    }
    void climpRndBack(Vec3 vMin, Vec3 vMax) {

        final int nClimper = 0;
        climp(nClimper, vMin.vals[nClimper], vMax.vals[nClimper]);

        if ( /*vMax.x() < mPos.x() ||*/ vMax.y() < mPos.y() ) {
            setRndPos(vMin, vMax);
            setRndVel();
        }
    }
    public void reInitFall(Vec3 vMin, Vec3 vMax) {

        mPos.set(MyTools.rndInterval(vMin.x(),vMax.x()),

                /*MyTools.rndInterval(vMin.y()-vMax.y(),vMin.y()-mSizes.y()),*/
                MyTools.rndInterval(vMax.y(), vMax.y()*1.6f),
                //vMin.y()-mSizes.y(),
                mPos.z());


        final float vVMax = 0.008f;
        mvVel.set(MyTools.rndInterval(-vVMax,vVMax),
                //0,
                MyTools.rndInterval(-vVMax,0),
                0);

    }
    void climpFall(Vec3 vMin, Vec3 vMax) {

        /*final int nClimper = 0;
        climp(nClimper, vMin.vals[nClimper], vMax.vals[nClimper]);*/

        if (  mPos.y() + mSizes.y() < vMin.y()
             || vMax.x() < mPos.x()
             || mPos.x() + mSizes.x() < vMin.x()
           ) {

            reInitFall(vMin, vMax);
        }
    }
    void clump(int coord, float fMin, float fMax) {
        if (mPos.vals[coord] < fMin) {
            mvVel.vals[coord] = 0.f;
            //mvVel.vals[coord] *= 1.1f;
            mPos.vals[coord] = fMax+(float)Math.random()*0.5f;
        } else if (fMax < mPos.vals[coord] && 0 < mvVel.vals[coord] ) {
            mvVel.vals[coord] *= -1.f;
            //mPos.vals[coord] = fMax;
        }/**/
    }
    public void setRndPos(Vec3 vMin, Vec3 vMax) {
        mPos.set(MyTools.rndInterval(vMin.x(),vMax.x()),
            MyTools.rndInterval(vMin.y()-vMax.y(),vMin.y()-mSizes.y()),
            //vMin.y()-mSizes.y(),
        0);
    }
    public void setRndVel() {
        final float vMax = 0.008f;
        mvVel.set(MyTools.rndInterval(-vMax,vMax),
            MyTools.rndInterval(0/*-vMax*/,vMax), 0);
    }
    public boolean intersectBall(Ball ballOther) {
        boolean bIQ = isIntersectQ(ballOther);
        ///TODO: Put it back
        /*if (bIQ) {
            float fDist = Vec3.minus(mPos, ballOther.mPos).length();
            ///Opti: mindist kiszeded
            if ( fDist < mfMinDist ) mfMinDist = fDist;
            ///return (fDist <= 2.f * mSizes.x());
            return (fDist <= mSizes.x());
        }*/
        return bIQ;
    }
    public float velTo(Ball ballOther) {

        Vec3 vAxis = new Vec3(ballOther.mPos);
        vAxis.minus(mPos);
        vAxis.normalize();
        return vAxis.dot(mvVel);

    }
    public float dist(Vec3 vFrom) {
        return new Vec3(vFrom).minus(mPos).length();
    }
    public void beginStep(long dt) {
        mbHaveCollided = false;
        mvOldPos.set(mPos);
        //*//Gravity
        final float fMass = 50.f;
        final float fFriction= 0.99f;
        //Vec3 vF = new Vec3((float) Math.sin(Math.toRadians(Globals.mOri[0])), -1.0f * (float) Math.sin(Math.toRadians(Globals.mOri[1])),0);

        Vec3 vF = new Vec3(Globals.mResF);
        Vec3 vSpeed = vF.div(fMass*dt);

        if (Globals.mPreset == Globals.Preset.pstBubbles) {
            mvVel.vals[0] += vSpeed.x();
            mvVel.vals[1] += -1 * vSpeed.y();
        } else if (Globals.mPreset == Globals.Preset.pstFall) {
            mvVel.vals[0] += -1 * vSpeed.x();
            mvVel.vals[1] += 0.05f * vSpeed.y();
        }

        //Friction
        mvVel.vals[0] *= fFriction;
        mvVel.vals[1] *= fFriction;

        /**///Gravity
        for (int i = 0; i < 2; i++) {
            /////Opti: dt always 1.f, remove multiplication
            mPos.vals[i] += dt * mvVel.vals[i] / 5.f;
        }
        switch (Globals.mPreset) {
            case pstBilliard:
                climp(0, Globals.mvMin.getVals()[0], Globals.mvMax.getVals()[0]);
                climp(1, Globals.mvMin.getVals()[1], Globals.mvMax.getVals()[1]);
                break;
            case pstBubbles:
                climpRndBack(Globals.mvMin, Globals.mvMax);
                break;
            case pstFall:
                climpFall(Globals.mvMin, Globals.mvMax);
                break;
        };
    }
    public int collideBall(Ball ballOther) {
        ///u1 = ((m1-m2)*v1 + 2*m2*v2) / (m1+m2)
        ///u2 = ((m2-m1)*v2 + 2*m1*v1) / (m1+m2)


        if (intersectBall(ballOther)) {
            if ( 0 < (velTo(ballOther) + ballOther.velTo(this))) return 2;
            return 1;
        }
        return 0;

        /*if (intersectBall(ballOther)) {
            mbHaveCollided = ballOther.mbHaveCollided = true;
            for (int i = 0; i < 2; i++) {
                float fTmp = mvVel.vals[i];
                mvVel.vals[i] = ballOther.mvVel.vals[i];
                ballOther.mvVel.vals[i] = fTmp;
            }
            resetOldPos();
            //ballOther.resetOldPos();
        }*/
    }
    public void velocitySwap(Ball ballOther) {
        //if (mbOneCollideInCycle && (mbHaveCollided || ballOther.mbHaveCollided) ) return;
        mbHaveCollided = ballOther.mbHaveCollided = true;
        for (int i = 0; i < 2; i++) {
            float fTmp = mvVel.vals[i];
            mvVel.vals[i] = ballOther.mvVel.vals[i];
            ballOther.mvVel.vals[i] = fTmp;
        }
        resetOldPos();
        ballOther.resetOldPos();
    }
    public void collideBrick(Brick brick) {
        int nIntersect = brick.intersect(this);
        if (0 != nIntersect) {
            final float fSlower = -0.5f;
            switch (nIntersect) {
                case 2: case 7:
                    mvVel.vals[1] *= fSlower;
                    resetOldPos();
                    break;
                case 4: case 5:
                    mvVel.vals[0] *= fSlower;
                    resetOldPos();
                    break;
                case 1: case 3: case 6: case 8:
                    mvVel.vals[0] *= fSlower;
                    mvVel.vals[1] *= fSlower;
                    resetOldPos();
                    break;
            }
        }
    }
    protected void resetOldPos() {
        mPos.set(mvOldPos);
    }
    public void draw(float[] mvpMatrix) {
        Vec4 c = null;
        if (mbHaveCollided && mbDrawCollide) {
            c =  new Vec4(mColor);
            mColor.set(1,0,0,1);
        }
        super.draw(mvpMatrix);
        if (null != c) {
            mColor.set(c);
        }
        //mbHaveCollided = false;
    }
}