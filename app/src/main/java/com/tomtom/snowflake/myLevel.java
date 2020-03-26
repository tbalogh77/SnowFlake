package com.tomtom.snowflake;

import android.util.Log;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


/**
 * Created by t on 12/28/15.
 */
public class myLevel {

    private static final String TAG = "myLevel";

    private static final boolean mbCollideBalls = Globals.mPreset == Globals.Preset.pstBilliard;

    private Vector mVectorOfVBOs = null;
    private Vector mBalls = null;
    public Vec2i mvSizes = new Vec2i();
    public final int mnMaxBalls = 100;
    private myGrid mGrid;
    //private Stack mClickStack = new Stack();
    DMatrix dm = null;
    private Ball mBallSelected = null;
    public Vec3 mLastClick = new Vec3();
    public Vec3 mBallSelectedDiff = new Vec3();

    public int getVboCount() {
        return mVectorOfVBOs.size();
    }
    public boolean isVboCreated() {
        return mVectorOfVBOs != null;
    }
    private Vec3 getPosFromScreen(float x, float y) {
        float newX = Globals.mvMax.x() - (Globals.mvMax.x()-Globals.mvMin.x())  *  x/(float)mvSizes.x();
        float newY = Globals.mvMax.y() - (Globals.mvMax.y()-Globals.mvMin.y())  *  y/(float)mvSizes.y();
        return new Vec3(newX,newY,0);
    }
    public void clickBegin(float x, float y) {
        mLastClick.set(getPosFromScreen(x, y));
        //mBallSelected = getClosestBall(mLastClick);
        mBallSelected = getCoveredBall(mLastClick);
        if (null != mBallSelected) {
            mBallSelectedDiff.set(mBallSelected.mPos).minus(mLastClick);
        }
    }
    public void clickAdd(float x, float y) {
        mLastClick.set(getPosFromScreen(x, y));
        if (mBallSelected != null) mBallSelected.mPos.set(mLastClick).plus(mBallSelectedDiff);
        //mClickStack.push(getPosFromScreen(x, y));
    }
    public void clickEnd(float x, float y) {
        mBallSelected = null;
    }
    public void create() {
        if (null == mVectorOfVBOs) {
            //myTexture.smartDeleteAll();
            mGrid = new myGrid(Globals.mvMin,Globals.mvMax,new Vec3(0.16f,0.16f,0), this);
            mVectorOfVBOs = new Vector();

            final float fGap = 0.05f * 4.f;
            final float fLen = 1.f - fGap;
            final float fThick = 0.1f;

            vbo Vbo = null;
            mVectorOfVBOs.addElement(Vbo = new vbo(/*new Vec3(-1,-1,0)*/Globals.mvMin, new Vec3(Globals.mvMax).minus(Globals.mvMin)));/**/
            final float ff = 0.2f;
            Vbo.mColor.set(ff, ff, ff, 1);

            switch (Globals.mPreset) {
                case pstBilliard:
                    addBallsBilliard();
                    break;
                case pstBubbles:
                    addBallsBubble();
                    break;
                case pstFall:
                    addBallsFall();
                    break;
            };
        }
        myTexture.smartInvalidateAll();
        createEmAll("texture2", null);
    }

    private String randomLeaf() {
        switch ((int)(10000.0 * Math.random())%4) {
            case 0: return "textures/leaf.png";
            case 1: return "textures/leaf003.png";
            case 2: return "textures/leaf002.png";
            default: return "textures/leaf004.png";
        }
    }
    private void createEmAll(String shaderName, String texResName) {
        int nBallTexIndex = 0;
        //final String strDefTex = 0.5 < Math.random() ? "textures/xmsstar.png" : "textures/xmas-song.jpg" ;
        final String strDefTex = "textures/chess.jpg" ;
        Iterator it = mVectorOfVBOs.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                //ball.create(shaderName, "textures/bub010.png");
                switch (Globals.mPreset) {
                    case pstBilliard:
                        ball.create(shaderName, "textures/ball.png");
                        break;
                    case pstBubbles:
                        ball.create("texture3", strDefTex);
                        break;
                    case pstFall: {
                        //boolean bGreen = 0.5 < Math.random();
                        //ball.create(shaderName, !bGreen ? "textures/leaf.png" : "textures/leaf002.png");
                        ball.create(shaderName, randomLeaf());
                        //if (bGreen) ball.mPos.setZ(2);
                    }
                        break;
                };
                /*switch (nBallTexIndex++ % 4) {
                    case 0: ball.create(shaderName, "textures/sf1.png"); break;
                    case 1: ball.create(shaderName, "textures/ball.png"); break;
                    case 2: ball.create(shaderName, "textures/hexagon.png"); break;
                    case 3: ball.create(shaderName, "textures/soccer.png"); break;
                }*/
            } else if (obj instanceof Brick) {
                Brick brick = (Brick)obj;
                brick.create(shaderName, "textures/brick.png");
            } else if (obj instanceof vbo) {
                vbo Vbo = (vbo)obj;
                Vbo.create(shaderName, strDefTex);
            }
        }
    }
    /*private void processClickStack() {
        if (mBallSelected != null) mBallSelected.mPos.set(mLastClick).plus(mBallSelectedDiff);
    }*/
    public void drawEmAll(float[] pMatrix) {
        //processClickStack();
        Iterator it = mVectorOfVBOs.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof vbo) {
                vbo Vbo = (vbo)obj;
                Vbo.draw(pMatrix.clone());
            }
        }
        //OSD();
    }
    private int setIds() {
        int id = 0;
        for (int i = 0; i < mVectorOfVBOs.size(); i++) {
            Object obj = mVectorOfVBOs.elementAt(i);
            if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                ball.setId(id++);
            }
        }
        return id;
    }
    private void setBallCollideds( DMatrix dm ) {
        for (int i = 0; i < mVectorOfVBOs.size(); i++) {
            Object obj = mVectorOfVBOs.elementAt(i);
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                ball.mbHaveCollided = dm.haveElement(ball.getId());
            }
        }
    }
    private void checkBall(Ball ball1,  DMatrix dm ) {
        Iterator it = mVectorOfVBOs.iterator();
        boolean bReached = false;
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Brick) {
                Brick brick = (Brick)obj;
                ball1.collideBrick(brick);
            }
            //*///Collide Ball
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                if (!bReached) {
                    if (ball == ball1) bReached = true;
                } else {
                    //ball1.collideBall(ball);
                    dm.setElement(ball.getId(),ball1.getId(),ball.collideBall(ball1));
                }
            }/**/
        }
    }
    /*private Ball addBall(){
        Ball ball = new Ball();
        mVectorOfVBOs.addElement(ball);
        ball.mPos.set(0, Globals.mvMin.y()+dxy, 0);
        ball.mvVel.setY( 0.004f );
    }*/
    private void addBallsBilliard(){
        int nBalls = mnMaxBalls;
        float fxdelta = 0.8f;
        float fydelta = 0.1f;
        int nRows = 1;
        float fx0 = Globals.mvMin.x()+(Globals.mvMax.x()-Globals.mvMin.x())/2.f;
        float fy0 = Globals.mvMin.y()+(Globals.mvMax.y()-Globals.mvMin.y())/2.f;

        float fx = fx0 + (nRows-1) * 0.06f*nRows;

        float dxy = 0.07f;

        {
            Ball ball = new Ball();
            mVectorOfVBOs.addElement(ball);
            ball.mPos.set(0, Globals.mvMin.y() + dxy, 0);
            ball.mvVel.setY(0.004f);
        }

        while (nRows < 5) {

            float fy = fy0 - ((nRows-1) * dxy) / 2.f;

            for ( int j = 0; j < nRows; j++ ) {
                Ball ball = new Ball();
                mVectorOfVBOs.addElement(ball);
                ball.mPos.set(fy,fx,0);
                ball.mvVel.set(0,0,0);
                final float fColMin = 0.25f;
                ball.mColor.set(MyTools.rndInterval(fColMin,1.0f),
                                MyTools.rndInterval(fColMin,0.5f),
                                MyTools.rndInterval(fColMin,1.0f),
                1.f);
                fy += dxy;
            }
            nRows++;
            fx += dxy;
        }
    }
    private void addBallsTwo(){
        int nBalls = mnMaxBalls;
        float fxdelta = 0.8f;
        float fydelta = 0.1f;

        int nRows = 1;
        float fx0 = Globals.mvMin.x()+(Globals.mvMax.x()-Globals.mvMin.x())/2.f;
        float fy0 = Globals.mvMin.y()+(Globals.mvMax.y()-Globals.mvMin.y())/2.f;

        float fx = fx0 + (nRows-1) * 0.06f*nRows;

        float dxy = 0.07f;

        while (nRows < 2) {

            float fy = fy0 - ((nRows-1) * dxy) / 2.f;

            for ( int j = 0; j < nRows; j++ ) {
                Ball ball = new Ball();
                ball.mColor.set(1, 0, 1, 1);
                mVectorOfVBOs.addElement(ball);
                ball.mPos.set(fy,fx,0);
                ball.mvVel.set(0,0,0);
                fy += dxy;
            }
            nRows++;
            fx += dxy;
        }

        Ball ball = new Ball();
        ball.mColor.set(0, 0, 1, 1);
        mVectorOfVBOs.addElement(ball);
        ball.mPos.set(0, Globals.mvMin.y()+dxy, 0);
        //AddVel
        //ball.mvVel = Vec3.minus(new Vec3(0,0,0), ball.mPos);
        //ball.mvVel.normalize();
        //ball.mvVel.multi(0.0004f);
        ball.mvVel.setY( 0.004f );

    }
    public void removeVbo(vbo Vbo) {
        mVectorOfVBOs.remove(Vbo);
    }
    private void beginStepBalls(long dt) {
        //Log.e("vx: ", MyGLRenderer.mOri[2] + " vy: " + MyGLRenderer.mOri[1]);
        mBalls = new Vector();
        Iterator it = mVectorOfVBOs.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                mBalls.addElement(ball);
                ball.beginStep(dt);
            }
        }
    }
    private void swapBalls( DMatrix dm ) {
        for (int j = 0; j < dm.getLength(); j++ ) {
            for (int i = j; i < dm.getLength(); i++ ) {
                switch (dm.getElement(i,j)) {
                    /*case 1:
                        ((Ball) mBalls.elementAt(i)).resetOldPos();
                        //((Ball) mBalls.elementAt(j)).resetOldPos();
                        break;*/
                    case 2: ((Ball) mBalls.elementAt(i)).velocitySwap((Ball) mBalls.elementAt(j)); break;
                }
            }
        }
    }
    private void checkEmAll() {
        int maxIds = setIds();
        dm = new DMatrix(maxIds);
        for (int i = 0; i < mVectorOfVBOs.size(); i++) {
            Object obj = mVectorOfVBOs.elementAt(i);
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                checkBall(ball, dm);
            }
        }
        setBallCollideds(dm);
        swapBalls(dm);
        //dm.logElements();
    }
    private Ball getClosestBall(Vec3 vPos) {
        Ball ballClosest = null;
        float minDist = 0.f;
        for (int i = 0; i < mVectorOfVBOs.size(); i++) {
            Object obj = mVectorOfVBOs.elementAt(i);
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                float dist = ball.dist(vPos);
                if ( dist < minDist || null == ballClosest ) {
                    ballClosest = ball;
                    minDist = dist;
                }
            }
        }
        return ballClosest;
    }
    private Ball getCoveredBall(Vec3 vPos) {
        for (int i = mVectorOfVBOs.size()-1; -1 < i; i--) {
            Object obj = mVectorOfVBOs.elementAt(i);
            if (obj instanceof Ball) {
                Ball ball = (Ball)obj;
                if(ball.isIntersectPoint(vPos)) return ball;
            }
        }
        return null;
    }
    public void step(long dt){
        for (int i = 0; i<1; i++) {
            beginStepBalls(dt);
            if (mbCollideBalls) checkEmAll();
        }
    }
    private void addBallsBubble(){
        for ( int i = 0; i < 16; i++ ) {
            float fSize = MyTools.rndInterval(0.04f,0.74f);
            Ball ball = new Ball(new Vec3(fSize,fSize,0));
            ball.mColor.set(1, 1, 1, /*0.35f*/1);

            ball.setRndPos(Globals.mvMin, Globals.mvMax);
            ball.setRndVel();
            //Log.e(TAG, "BallPos: " + ball.mPos.x() + "; " + ball.mPos.y());
            mVectorOfVBOs.addElement(ball);
        }
    }
    private Brick addBrick(){
        Brick br = new Brick(0.2f,0.2f);
        mVectorOfVBOs.addElement(br);
        return br;
    }
    private void addBallsFall(){
        for ( int i = 0; i < 2 *  16   ; i++ ) {
            //float fSize = MyTools.rndInterval(0.04f,0.74f);
            float fSize = 1;
            Ball ball = new Ball(new Vec3(fSize,fSize,0));
            ball.mColor.set(1, 1, 1, /*0.35f*/1);

            ball.reInitFall(Globals.mvMin, Globals.mvMax);
            //Log.e(TAG, "BallPos: " + ball.mPos.x() + "; " + ball.mPos.y());
            mVectorOfVBOs.addElement(ball);
        }
        addBrick();
    }
}
