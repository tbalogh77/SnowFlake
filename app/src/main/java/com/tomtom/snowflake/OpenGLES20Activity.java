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

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OpenGLES20Activity extends Activity  implements SensorEventListener {

    private static final String TAG = "OpenGLES20Activity";

    OrientationEventListener mOrientationListener;

    private static final int mSensorType = Sensor.TYPE_ACCELEROMETER;

    float [] mRotMat = new float [9];

    private MyGLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mSensorAccel;

    public static  OpenGLES20Activity inst;

    public String readTextAsset( String textFile ) {

        InputStream iS = null;
        String outString = "";
        try {
            iS = getResources().getAssets().open(textFile);
            if (iS != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
                StringBuilder stringBuilder = new StringBuilder();
                while ( (outString = reader.readLine()) != null ) {
                    stringBuilder.append(outString);
                }
                outString = stringBuilder.toString();
                Log.d("Hi","ss: " + outString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outString;
    }

    public Bitmap readBitmapAsset( String textFile ) {

        InputStream iS = null;
        Bitmap bitmap = null;
        try {
            iS = getResources().getAssets().open(textFile);
            if (iS != null) {
                bitmap = BitmapFactory.decodeStream(iS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public Bitmap readBitmapID( int id ) {

        Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.soccer );
        /*InputStream iS = null;
        Bitmap bitmap = null;
        try {
            iS = getResources().getAssets().open(textFile);
            if (iS != null) {
                bitmap = BitmapFactory.decodeStream(iS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        * */

        inst = this;



        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        ///Remove  Window Title !!!
        ///this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mGLView);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mLight = mSensorManager.getDefaultSensor(mSensorType);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL)
        {
            @Override
            public void onOrientationChanged(int orientation) {
                //Globals.mnScreenOri = orientation;
                Globals.mnScreenRotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
                Globals.countResF();
                //Log.e(TAG, "Orientation changed to " + Globals.mnScreenRotation);
            }
        };
        if (mOrientationListener.canDetectOrientation() == true) {
            Log.v(TAG, "Can detect orientation");
            mOrientationListener.enable();
            mOrientationListener.onOrientationChanged(-1);
        } else {
            Log.v(TAG, "Cannot detect orientation");
            mOrientationListener.disable();
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.


        if (event.sensor.getType() == mSensorType) {
            //mMagnetic = event.values.clone();
            Globals.mOri = event.values.clone();
            Globals.countResF();

            //Log.e("vx: ", MyGLRenderer.mOri[2] + " vy: " + MyGLRenderer.mOri[1]);
            //mGLView.requestRender();
        }

        if (event.sensor.getType() ==  Sensor.TYPE_ACCELEROMETER) {
            Globals.mAccl = event.values.clone();
        }
        //lux = event.values[0];
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationListener.disable();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("YYYY: ", " onConfigurationChanged !!!");

        // Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            Log.e("YYYY: ", " landscape !!!");

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            Log.e("YYYY: ", " portrait !!!");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void newGame() {
        DMatrix dm = new DMatrix(4);
        dm.test();

    }
}