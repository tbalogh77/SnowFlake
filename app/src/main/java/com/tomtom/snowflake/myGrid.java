package com.tomtom.snowflake;

/**
 * Created by t on 12/19/15.
 */
public class myGrid {

    class gridElement {

        public gridElement mNext = null;
        public vbo mVbo = null;

        public gridElement(vbo aVbo, gridElement aNext ) {
            mVbo = aVbo;
            mNext = aNext;
        }
        public vbo getVbo() {
            return mVbo;
        }
        public gridElement next() {
            return mNext;
        }
        void addLast(vbo Vbo) {
            if (null != mNext) {
                mNext.addLast(Vbo);
            } else {
                mVbo = Vbo;
            }
        }
    };

    Vec3 mvFrom;
    Vec3 mvTo;
    Vec3 mvStep;
    Vec2i mvCount;
    gridElement[] mElements;
    myLevel mLevel = null;

    public myGrid(Vec3 vFrom, Vec3 vTo, Vec3 vStep, myLevel level) {
        mLevel = level;
        mvFrom = new Vec3(vFrom);
        mvTo = new Vec3(vTo);
        mvStep = new Vec3(vStep);
        Vec3 vCount = new Vec3(vTo).minus(vFrom).divideByElements(vStep);
        mvCount = new Vec2i((int) vCount.x() + 1, (int) vCount.y() + 1);
        mElements = new gridElement[mvCount.x()*mvCount.y()];
    }
    public Vec2i countIndices(Vec3 vPos) {
        return new Vec2i(new Vec3(vPos).minus(mvFrom).divideByElements(mvStep));
    }
    public int countIndex(Vec2i vIndices) {
        return mvCount.x() * vIndices.y() + vIndices.x();
    }
    public Vec3 countPosFromIndices(Vec2i vIndex) {
        return new Vec3(mvFrom.x()+vIndex.x()*mvStep.x(),mvFrom.y()+vIndex.y()*mvStep.y(), 0);
    }
    public Brick touch(Vec3 vPos) {
        Brick brick = null;
        Vec2i vIndex = countIndices(vPos);
        int nIndex = countIndex(vIndex);
        if (null == mElements[nIndex]) {
            brick = new Brick(countPosFromIndices(vIndex),mvStep);
            mElements[nIndex] = new gridElement(brick,null);
        } else {
            mLevel.removeVbo(mElements[nIndex].getVbo());
            mElements[nIndex] = null;
        }
        return brick;
    }
}