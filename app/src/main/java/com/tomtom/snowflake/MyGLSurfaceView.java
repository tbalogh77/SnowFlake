package com.tomtom.snowflake;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);
        ///setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    //private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                myContent.inst.onTacsiEnded(x,y);
                break;
            case MotionEvent.ACTION_DOWN:
                myContent.inst.onTacsiBegan(x,y);
                break;
            case MotionEvent.ACTION_MOVE:

                myContent.inst.onTacsi(x,y);
                //return true;
                break;
        }
        return true;
    }
}
