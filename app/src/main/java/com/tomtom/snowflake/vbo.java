/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.opengl.Matrix;
import android.opengl.GLES20;
import android.util.Log;

public class  vbo {

    private static final String TAG = "vbo";

    static final int COORDS_PER_VERTEX = 3;
    static final int BYTES_PER_VERTEX = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public Vec3 mPos = new Vec3(0,0,0);
    public Vec3 mSizes = new Vec3(0.06f,0.06f,0);
    public Vec4 mColor = new Vec4(1,1,1,1);
    private FloatBuffer mVertexBuffer = null;
    private ShortBuffer mIndexBuffer = null;
    private myShader mShader = new myShader();
    public  myTexture mTexture = null;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    protected float mVertices[] = null;
    protected float mTexCoords[] = null;
    protected short mIndices[] = null; // order to draw vertices
    /**TOMTOMTOM*/
    public void constructor(Vec3 size) {
        if (size != null) mSizes = new Vec3(size);
        final float mVerticesDef[] = {
                0,          0,          0,      // top left     00
                                                //              0x

                0,          mSizes.y(), 0,      // top right    0x
                                                //              00

                mSizes.x(), mSizes.y(), 0,      // bottom       x0
                                                // bottom       00

                mSizes.x(), 0,          0,      // bottom       00
                                                // bottom       x0
                };
        mVertices = mVerticesDef.clone();
        final short mIndicesDef[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
        mIndices = mIndicesDef.clone();

        final float[] mTexCoordsDef = {
                1, 1,
                1, 0,
                0, 0,
                0, 1,
        };
        mTexCoords = mTexCoordsDef.clone();
    }
    public vbo() {
        constructor(null);
    }
    public vbo(Vec3 size) {
        constructor(size);
    }
    public vbo( Vec3 vPos, Vec3 vSiz) {
        constructor(vSiz);
        mPos.set(vPos);
    }
    public void create(String shaderName, String texResName) {

        ByteBuffer bb = ByteBuffer.allocateDirect(mVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mIndexBuffer = dlb.asShortBuffer();
        mIndexBuffer.put(mIndices);
        mIndexBuffer.position(0);

        mShader.create(shaderName);

        mPositionHandle = mShader.GetAttribLocation("vPosition");
        mColorHandle = mShader.GetUniformLocation("vColor");
        mMVPMatrixHandle = mShader.GetUniformLocation("uMVPMatrix");

        //int idSoccer = R.drawable.soccer;

        if (texResName != null ) {
            //mShader.UseProgram();

            /*mTexture =  new myTexture();
            mTexture.create(texResName,mTexCoords);*/

            mTexture = myTexture.smartCreate(texResName);
            mTexture.create(mTexCoords);
        }
    }
    public boolean isIntersectQ(vbo otherVbo) {

        if (mPos.x() + mSizes.x() < otherVbo.mPos.x()) return false;
        if (otherVbo.mPos.x() + otherVbo.mSizes.x() < mPos.x()) return false;

        if (mPos.y() + mSizes.y() < otherVbo.mPos.y()) return false;
        if (otherVbo.mPos.y() + otherVbo.mSizes.y() < mPos.y()) return false;
        return true;
    }
    public boolean isIntersectPoint(Vec3 vPoint) {

        if (mPos.x() + mSizes.x() < vPoint.x()) return false;
        if (vPoint.x() < mPos.x()) return false;

        if (mPos.y() + mSizes.y() < vPoint.y()) return false;
        if (vPoint.y() < mPos.y()) return false;
        return true;
    }
    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        //Log.d(TAG, "pos: " + mPos.x() + "; " + mPos.y());
        mShader.UseProgram();


/*
        mPositionHandle = mShader.GetAttribLocation("vPosition");
        mColorHandle = mShader.GetUniformLocation("vColor");
        mMVPMatrixHandle = mShader.GetUniformLocation("uMVPMatrix");
*/

        /*NEW*/
        int nMinMaxHandle = mShader.GetUniformLocation("vMinMax");
        final float dx = Globals.mvMax.x()-Globals.mvMin.x();
        final float dy = Globals.mvMax.y()-Globals.mvMin.y();

        /*Vec4 vMinMax = new Vec4(
                mSizes.y() / dy,
                mSizes.x() / dx,

                (mPos.y() - Globals.mvMin.y())/dy
                ,
                1-(mPos.x() + mSizes.x()  - Globals.mvMin.x())/dx
        );*/
        Vec4 vMinMax = new Vec4(
                mSizes.x() / dx,
                mSizes.y() / dy,
                (Globals.mvMax.x()-mPos.x()-mSizes.x())/dx,
                //1-(mPos.y() + mSizes.y()- Globals.mvMin.y())/dy
                (Globals.mvMax.y() -  mPos.y() - mSizes.y() )/dy
        );
        /*NEW*/


        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                BYTES_PER_VERTEX, mVertexBuffer);
        GLES20.glUniform4fv(mColorHandle, 1, mColor.vals, 0);
        /*NEW*/
        GLES20.glUniform4fv(nMinMaxHandle, 1, vMinMax.vals, 0);
        /*NEW*/
        Matrix.translateM(mvpMatrix, 0, mPos.x(), mPos.y(), mPos.z());
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        //MyGLRenderer.checkGlError("glUniformMatrix4fv");
        if (mTexture != null)
            mTexture.renderTexture(mShader);
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, mIndices.length,
                GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        if (mTexture != null) mTexture.DisableVertexAttribArray();
    }
}