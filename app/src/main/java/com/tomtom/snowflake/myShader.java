/**
 * Created by t on 8/27/15.
 */
package com.tomtom.snowflake;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.Matrix;
import android.os.Debug;
import android.util.Log;



import android.opengl.GLES20;

public class  myShader {

    public String mShaderName = "";
    public String mVertexShaderCode = "";
    public String mFragmentShaderCode = "";
    private int mProgram = -1;
    public Vec4 mColor = new Vec4(1, 0, 1, 1);

    /*** TOMTOMTOM */
    public myShader() {
    }
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        GLToolbox.checkGlError("glCreateShader(" + type + ")");


        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLToolbox.checkGlError("glShaderSource(" + shaderCode + ")");

        GLES20.glCompileShader(shader);
        GLToolbox.checkGlError("glCompileShader(" + shader + ")");


        return shader;
    }
    public void create(String shaderName) {
        mShaderName = shaderName;
        // prepare shaders and OpenGL program
        mFragmentShaderCode = OpenGLES20Activity.inst.readTextAsset("shaders/" + mShaderName + ".fsh");
        mVertexShaderCode = OpenGLES20Activity.inst.readTextAsset("shaders/" + mShaderName + ".vsh");

        int vertexShader = loadShader(
                GLES20.GL_VERTEX_SHADER,
                mVertexShaderCode);
        int fragmentShader = loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                mFragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLToolbox.checkGlError("glCreateProgram()");
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLToolbox.checkGlError("glAttachShader(mProgram, vertexShader)");
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLToolbox.checkGlError("glAttachShader(mProgram, fragmentShader)");
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        GLToolbox.checkGlError("Link Program " + shaderName);

        //GLES20.glUseProgram(mProgram);
        //GLToolbox.checkGlError("glUseProgram " + shaderName);
    }

    public void UseProgram() {
        //if (GLES20.glIsProgram(mProgram)) ...
        GLToolbox.checkGlError("glUseProgram__BEFORE__(" + mProgram + ")");
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram(" + mProgram + ")");
    }

    public int GetAttribLocation(String attrName) {
        int nRetVal = GLES20.glGetAttribLocation(mProgram,attrName);
        GLToolbox.checkGlError("glGetAttribLocation(" + attrName + ")");
        return nRetVal;
    }
    public int GetUniformLocation(String attrName) {
        int nRetVal = GLES20.glGetUniformLocation(mProgram,attrName);
        GLToolbox.checkGlError("glGetUniformLocation(" + attrName + ")");
        return nRetVal;
    }
}