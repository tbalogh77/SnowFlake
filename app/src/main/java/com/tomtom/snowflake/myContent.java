/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class myContent {
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    ///private final float[] mRotationMatrix = new float[16];
    private float mRatio;


    ///Gány!!!
    public myLevel mLevel = new myLevel();
    public float mActFps = 0;
    public static myContent inst = null;

    public myContent() {
        inst = this;
    }
    public void create() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        mLevel.create();
    }
    public void simu(long dt){
        mLevel.step(/*33*/dt);
    }
    public void draw(){
        float fYellow = 0.f;
        GLES20.glClearColor(fYellow, fYellow, 0.f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mLevel.drawEmAll(mMVPMatrix);
    }
    public void onResized(int width, int height) {
        mLevel.mvSizes.set(width, height);
        ///TODO: Adjust the viewport based on geometry changes,
        ///such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        //mRatio = 1.f;//(float) width / height;
        mRatio = (float) width / height;

        Globals.mvMin.set(-mRatio, -1.f, 3.f);
        Globals.mvMax.set(mRatio, 1.f, 7.f);

        //Matrix.frustumM(mProjectionMatrix, 0, mLevel.mvMin.x(), mLevel.mvMax.x(), mLevel.mvMin.y(), mLevel.mvMax.y(), mLevel.mvMin.z(), mLevel.mvMax.z());
        Matrix.orthoM(mProjectionMatrix, 0, Globals.mvMin.x(), Globals.mvMax.x(), Globals.mvMin.y(), Globals.mvMax.y(), Globals.mvMin.z(), Globals.mvMax.z());

        float fX = 0;
        float fY = 0;
        Matrix.setLookAtM(mViewMatrix, 0, fX, fY, -3, fX, fY, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);/**/
    }
    public void onTacsi(float x, float y) {
        mLevel.clickAdd(x,y);

    }
    public void onTacsiBegan(float x, float y) {
        mLevel.clickBegin(x,y);
        //mLevel.addClick(x,y);
    }
    public void onTacsiEnded(float x, float y) {
        mLevel.clickEnd(x,y);
        //mLevel.addClick(x,y);
    }
}
