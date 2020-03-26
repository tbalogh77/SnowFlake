/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.tomtom.snowflake.GLToolbox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

public class myTexture {

    private Bitmap mBitmap = null;
    private int[] mTextures = new int[2];
    private final String mStrName;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;

    private FloatBuffer mTexVertices = null;

    private int mImageWidth;
    private int mImageHeight;
/*
    private int mViewWidth;
    private int mViewHeight;

    private int mTexWidth;
    private int mTexHeight;

    private static final int FLOAT_SIZE_BYTES = 4;

*/
    private static Vector mTexturesVector = new Vector();


    private myTexture(String strName) {
        mStrName = new String(strName);
    }
    public static myTexture smartCreate(String mStrName) {
        for (int i = 0; i < mTexturesVector.size(); i++) {
            myTexture tex =  (myTexture) mTexturesVector.elementAt(i);
            if (tex.mStrName.equals(mStrName)) return tex;
        }
        myTexture newTex = new myTexture(mStrName);
        mTexturesVector.addElement(newTex);
        return newTex ;
    }
    public static void smartInvalidateAll() {
        for (int i = 0; i < mTexturesVector.size(); i++) {
            myTexture tex =  (myTexture) mTexturesVector.elementAt(i);
            tex.mTexVertices = null;
        }
    }
    public static void smartDeleteAll() {
        mTexturesVector.clear();
    }
    public void create(float [] TEX_VERTICES) {
        if (mTexVertices == null) {
            ByteBuffer bbtc = ByteBuffer.allocateDirect(TEX_VERTICES.length * 4);
            bbtc.order(ByteOrder.nativeOrder());
            mTexVertices = bbtc.asFloatBuffer();
            mTexVertices.put(TEX_VERTICES);
            mTexVertices.position(0);
            loadTextures(mStrName);
        }
    }

    public void tearDown() {
        //GLES20.glDeleteProgram(mProgram);
    }


    public void renderTexture(myShader shader) {
        int texId = mTextures[0];

        // Disable blending
        //GLES20.glDisable(GLES20.GL_BLEND);
        //GLToolbox.checkGlError("glDisable(GLES20.GL_BLEND)");

        // Set the vertex attributes
        ///Log.e("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX","mTexSamplerHandle" + mTexSamplerHandle);
        //GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        //GLToolbox.checkGlError("glVertexAttribPointer(mTexCoordHandle)");

        //GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        //GLToolbox.checkGlError("glEnableVertexAttribArray(mTexCoordHandle)");

        mTexCoordHandle = shader.GetAttribLocation("a_texcoord");
        mTexSamplerHandle = shader.GetUniformLocation("tex_sampler");

        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLToolbox.checkGlError("glEnableVertexAttribArray(mTexCoordHandle)");

        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        GLToolbox.checkGlError("glVertexAttribPointer(mTexCoordHandle)");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);
    }

    private void loadTextures(String resName) {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);
        GLToolbox.checkGlError("glGenTextures(2, mTextures, 0)");


        // Load input bitmap
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.puppy);

        mBitmap = OpenGLES20Activity.inst.readBitmapAsset(resName);
        //mBitmap = OpenGLES20Activity.inst.readBitmapID(0);


        mImageWidth = mBitmap.getWidth();
        mImageHeight = mBitmap.getHeight();
        //mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLToolbox.checkGlError("glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0])");
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        GLToolbox.checkGlError("texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0)");

        // Set texture parameters
        GLToolbox.initTexParams();
    }
    public void DisableVertexAttribArray()
    {
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
    }
}
