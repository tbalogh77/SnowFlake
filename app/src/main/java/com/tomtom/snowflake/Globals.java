package com.tomtom.snowflake;

import android.util.Log;
import android.view.Surface;

/**
 * Created by t on 2017.06.12..
 */

public class Globals {

    public enum Preset { pstBilliard, pstBubbles, pstFall };
    public final static Preset mPreset = Preset.pstFall;


    ///public static int mnScreenOri = -1;
    public static int mnScreenRotation = -1;
    public static float[] mOri = new float[3];
    public static float[] mAccl = new float[3];
    public static Vec3 mResF = new Vec3();
    public static Vec3 mvMax = new Vec3();
    public static Vec3 mvMin = new Vec3();


    public static void countResF() {
        //mResF = new Vec3((float) Math.sin(Math.toRadians(mOri[0])), -1.0f * (float) Math.sin(Math.toRadians(mOri[1])),0);
        mResF = new Vec3(/*-1.0f */ (float) Math.sin(Math.toRadians(mOri[1])),(float) Math.sin(Math.toRadians(mOri[0])), 0);


        /*Object aaa = new Object()
        {
            public void setUpsideDown() {
                mResF.swapXY();
                mResF.setX( mResF.x() * - 1.f );
                Log.e("YYYY: ", " UPSIDEDOWN !!!" + mnScreenOri + mnScreenRotation);
            }
        };*/


        switch (mnScreenRotation) {
            case Surface.ROTATION_0:
                mResF.swapXY();
                mResF.setY( mResF.y() * - 1.f );
                //Log.e("YYYY: ", " NORMAL !!!" + " rot: " + mnScreenRotation);
                break;
            case Surface.ROTATION_90:
                mResF.setX( mResF.x() * - 1.f );
                mResF.setY( mResF.y() * - 1.f );
                //Log.e("YYYY: ", " Left !!!" + " rot: "  + mnScreenRotation);
                break;
            case Surface.ROTATION_180:
                mResF.swapXY();
                mResF.setX( mResF.x() * - 1.f );
                //Log.e("YYYY: ", " UPSIDEDOWN !!!" + " rot: "  + mnScreenRotation);
                break;
            case Surface.ROTATION_270:
                //Log.e("YYYY: ", " Right !!!" + " rot: "  + mnScreenRotation);
                break;
        }

        /*if ( -1 == mnScreenOri ) {

            //mResF.setX( mResF.x() * - 1.f );
            //mResF.setY( mResF.y() * - 1.f );
            Log.e("YYYY: ", " TABLE !!!" + mnScreenRotation);

        } else if ( 315 < mnScreenOri || mnScreenOri <= 45 ) { //0

            mResF.swapXY();
            mResF.setY( mResF.y() * - 1.f );

            Log.e("YYYY: ", " NORMAL !!!" + mnScreenOri + mnScreenRotation);

        } else if ( 225 < mnScreenOri ) { //1

            mResF.setX( mResF.x() * - 1.f );
            mResF.setY( mResF.y() * - 1.f );
            Log.e("YYYY: ", " Left !!!" + mnScreenOri + mnScreenRotation);


        } else if ( 135 < mnScreenOri ) { //2

            mResF.swapXY();
            mResF.setX( mResF.x() * - 1.f );
            Log.e("YYYY: ", " UPSIDEDOWN !!!" + mnScreenOri + mnScreenRotation);


        } else //if ( 45 < mnScreenOri ) //3
        {

            Log.e("YYYY: ", " Right !!!" + mnScreenOri + mnScreenRotation);

        }*/
    }

}
