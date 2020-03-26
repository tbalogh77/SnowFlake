/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomtom.snowflake;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private scoreBoard sb = new scoreBoard();
    private float[] mProjMatrix = new float[16];
    //private int[] mTextures = new int[2];
    //private static final String TAG = "MyGLRenderer";
    private myContent mContent = new myContent();
    private long mLastTime = 0;
    boolean mbInited = false;

    private Context context;                           // Context (from Activity)

    public MyGLRenderer(Context context)  {
        super();
        this.context = context;                         // Save Specified Context
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        ///GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        ///mContent.create();
        mLastTime = SystemClock.uptimeMillis();
        mbInited = false;
        sb.create(context);
    }
    private long getdt() {

        long time = SystemClock.uptimeMillis();
        long dt = time - mLastTime;
        mLastTime = time;
        return dt;
    }
    @Override
    public void onDrawFrame(GL10 unused) {
        this.drawContent();
        if (mContent.mLevel.dm != null ) sb.drawScoreBoard(mContent.mLevel.dm);
    }
    private void drawContent() {
        if (!mbInited) {
            mbInited = true;
            mContent.create();
            if (!myContent.inst.mLevel.isVboCreated()) mLastTime = SystemClock.uptimeMillis();
        }

        final long lMaxTime = 1000 / 31;
        long dt = getdt();
        mContent.simu(dt);
        long physTime = SystemClock.uptimeMillis() - mLastTime;
        mContent.draw();
        long allTime = SystemClock.uptimeMillis() - mLastTime;
        float fps = 1000.f / (float)dt;
        myContent.inst.mActFps = fps;

        long sleepTime = 0;
        boolean bFrameDrop = false;
        if ( allTime < lMaxTime) {
            try {
                sleepTime = lMaxTime-allTime;
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            bFrameDrop = true;
       }
        /*Log.e("FPSEK: ", (bFrameDrop ?"__SZOPOL__ " : "__________ " ) + fps + " vbos: " + mContent.mLevel.getVboCount() +
                " fps  dt: " + dt + " st " + sleepTime + " at: " +
                allTime + " pt " + physTime + " mdst: " + (0.1f - Ball.mfMinDist));*/
        //Log.e("FPSEK: ", " " + mAccl[0] + "   " + mAccl[1] + "  " + mAccl[2]);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        mContent.onResized(width,height);
        setMatrices(width, height);
    }
    private void setMatrices( int width, int height) { //		gl.glViewport( 0, 0, width, height );
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // Take into account device orientation
        if (width > height) {
            Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        }
        else {
            Matrix.frustumM(mProjMatrix, 0, -1, 1, -1/ratio, 1/ratio, 1, 10);
        }

        sb.setWH(width, height, mProjMatrix);
    }

}