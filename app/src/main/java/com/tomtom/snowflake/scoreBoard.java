package com.tomtom.snowflake;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.android.texample2.GLText;

/**
 * Created by t on 2017.07.25..
 */

public class scoreBoard {

    private int width = 100;                           // Updated to the Current Width + Height in onSurfaceChanged()
    private int height = 100;
    private float[] mVMatrix = new float[16];
    private float[] mVPMatrix = new float[16];
    private float[] mProjMatrix = new float[16];

    private GLText glText;                             // A GLText Instance


    public void create(Context context){
        glText = new GLText(context.getAssets());
        // Load the font from file (set size + padding), creates the texture
        // NOTE: after a successful call to this the font is ready for rendering!
        glText.load( "fonts/Roboto-Regular.ttf", 28, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setWH( int width, int height, float [] ProjMatrix) {
        // Save width and height
        this.width = width;                             // Save Current Width
        this.height = height;                           // Save Current Height
        this.mProjMatrix = ProjMatrix;

        int useForOrtho = Math.min(width, height);

        //TODO: Is this wrong?
        Matrix.orthoM(mVMatrix, 0,
                -useForOrtho/2,
                useForOrtho/2,
                -useForOrtho/2,
                useForOrtho/2, 0.1f, 100f);

    }


    public void drawScoreBoard(DMatrix dm) {
        if (dm == null) return;
        /*GLES20.glClearColor( 0.5f, 0.5f, 0.5f, 1.0f );
        int clearMask = GLES20.GL_COLOR_BUFFER_BIT;
        GLES20.glClear(clearMask);*/
        //glText.drawTexture( width/2, height/2, mVPMatrix);            // Draw the Entire Texture
        //GLToolbox.checkGlError("XXXXXXXXXXXXXXXXXXYYYXXXXXXXXXXXXXX");

/*        // TEST: render some strings with the font
        glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color WHITE)
        glText.drawC("Test String 3D!", 0f, 0f, 0f, 0, -30, 0);
//		glText.drawC( "Test String :)", 0, 0, 0 );          // Draw Test String
        glText.draw( "Diagonal 1", 40, 40, 40);                // Draw Test String
        glText.draw( "Column 1", 100, 100, 90);              // Draw Test String
        glText.end();                                   // End Text Rendering

        glText.begin( 0.0f, 0.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color BLUE)
        glText.draw( "More Lines...", 50, 200 );        // Draw Test String
        glText.draw( "The End.", 50, 200 + glText.getCharHeight(), 180);  // Draw Test String
        glText.end();                                   // End Text Rendering
*/

        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        drawHalf(dm, true);
        drawHalf(dm, false);
    }
    private void drawHalf(DMatrix dm, boolean bZero) {
        if (bZero)
            glText.begin( 0.0f, 0.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color WHITE)
        else
            glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color WHITE)

        for (int j = 0; j < dm.getLength(); j++) {
            for (int i = j; i < dm.getLength(); i++) {
                int nElement = dm.getElement(i,j);
                if ( bZero == ( 0 < nElement) )
                    glText.draw( "" + nElement , 30*i, 30*j );        // Draw Test String
            }
        }
        glText.end();                                   // End Text Rendering
    }
}
