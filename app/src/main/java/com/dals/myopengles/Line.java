package com.dals.myopengles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class Line {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final int mProgram;

    private int positionHandle;
    private int colorHandle;

    private int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Use to access and set the view transformation
    private int vPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float[] lineCoords;
    private CreateXY lineXY;

    private short[] drawOrder = new short[300]; // order to draw vertices

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    /*private final String vertexShaderCode =
          "attribute vec4 vPosition;" +
                  "void main() {" +
                  "  gl_Position = vPosition;" +
                  "}";*/
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Line() {
        lineXY = new CreateXY(300);
        lineCoords = lineXY.arrXYZ;
        vertexCount = lineCoords.length / COORDS_PER_VERTEX;
        for(short i=0; i<300;i++) {
            drawOrder[i]=i;
        }
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                lineCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(lineCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        //////////////////////

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDrawElements(GLES20.GL_LINES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

}

class CreateXY{
    Random rnd;
    float[] arrXYZ;
    float buf;
    CreateXY(){
        rnd = new Random(1);
        arrXYZ = new float[1000];
        for(int i=0 ; i<1000; i+=3){
            buf = rnd.nextFloat();
            arrXYZ[i] = (float) (buf/0.1);
            arrXYZ[i+1] = (float)(buf%0.1);
            arrXYZ[i+2] =(float) 0.0;
        }
    }

    CreateXY(int count){
        rnd = new Random(1);
        int len = count * 3;
        arrXYZ = new float[len];
        for(int i=0 ; i<len; i+=3){
            buf = rnd.nextFloat();
            arrXYZ[i] = (float) (buf*2-1);
            buf = rnd.nextFloat();
            arrXYZ[i+1] = (float)(buf*2-1);
            arrXYZ[i+2] =(float) 0.0;
        }
    }

    public float[] getData(){
        return arrXYZ;
    }

    public int length(){
        return arrXYZ.length;
    }
}