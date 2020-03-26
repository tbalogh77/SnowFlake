/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

public class Brick extends vbo {

    public Brick(float Width, float Height) {
        super(new Vec3(Width, Height,0));
    }
    public Brick( Vec3 vPos, Vec3 vSiz) {
        super(vPos,vSiz);
    }
    public int intersect(vbo otherVBO) {
        float fMinMine = mPos.x();
        float fMaxMine = mPos.x() + mSizes.x();
        float fMinOther = otherVBO.mPos.x();
        float fMaxOther = otherVBO.mPos.x() + otherVBO.mSizes.x();
        boolean x0in = fMinMine < fMinOther && fMinOther < fMaxMine;
        boolean x1in = fMinMine < fMaxOther && fMaxOther < fMaxMine;
        fMinMine = mPos.y();
        fMaxMine = mPos.y() + mSizes.y();
        fMinOther = otherVBO.mPos.y();
        fMaxOther = otherVBO.mPos.y() + otherVBO.mSizes.y();
        boolean y0in = fMinMine < fMinOther && fMinOther < fMaxMine;
        boolean y1in = fMinMine < fMaxOther && fMaxOther < fMaxMine;
        if ( !x0in  &&  x1in &&  y0in && !y1in ) return 1;
        if (  x0in  &&  x1in &&  y0in && !y1in ) return 2;
        if (  x0in  && !x1in &&  y0in && !y1in ) return 3;
        if ( !x0in  &&  x1in &&  y0in &&  y1in ) return 4;
        if (  x0in  && !x1in &&  y0in &&  y1in ) return 5;
        if ( !x0in  &&  x1in && !y0in &&  y1in ) return 6;
        if (  x0in  &&  x1in && !y0in &&  y1in ) return 7;
        if (  x0in  && !x1in && !y0in &&  y1in ) return 8;
        if (  !x0in  && !x1in && !y0in &&  !y1in ) return 9;
        if (   x0in  &&  x1in &&  y0in &&   y1in ) return 10;
        return 0;
    }
}
