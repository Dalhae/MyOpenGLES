package com.dals.myopengles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
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
    private LineCoordinates lineXY;

    private short[] drawOrder; // order to draw vertices

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 1.0f, 0.76953125f, 0.22265625f, 1.0f };

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
        lineXY = new LineCoordinates();
        lineXY.addLine((float)0.2,(float)0.2,(float)0.2,(float)1.8);
        lineXY.addLine((float)0.2,(float)0.2,(float)1.8,(float)0.2);
   //     lineXY.addLine((float)0.3,(float)0.3,(float)1,(float)1);
        lineCoords = lineXY.getCoordinates();
        vertexCount = lineCoords.length / COORDS_PER_VERTEX;
        int len = lineCoords.length;
        drawOrder = new short[]{0,1,2,3};
       // drawOrder = new short[len];
        //for(short i=0; i<len;i++) {
        //    drawOrder[i]=i;
       // }
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
        GLES20.glDrawElements(GLES20.GL_POINTS, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

}

class LineCoordinates{
    private ArrayList<Float> mCoordinates;
    private float[] arrCoordinates;

    LineCoordinates(){
        mCoordinates = new ArrayList<Float>();
     }

    LineCoordinates(float startx, float starty, float endx, float endy){
        mCoordinates = new ArrayList<Float>();
       addLine(startx, starty, endx, endy);
    }

    public float[] getCoordinates(){
        int len = length();
        if (len > 0){
            arrCoordinates = new float[len];
            for(int i=0;i<len;i++){
                arrCoordinates[i] = mCoordinates.get(i);
            }
        }
        return arrCoordinates;
    }

    public int length(){
        return  mCoordinates.size();
    }

    public void addLine(float startx, float starty, float endx, float endy){
        Point start, end;
        start = GetCoordinates(startx,starty);
        end = GetCoordinates(endx,endy);
        mCoordinates.add(start.point[0]);
        mCoordinates.add(start.point[1]);
        mCoordinates.add(start.point[2]);
        mCoordinates.add(end.point[0]);
        mCoordinates.add(end.point[1]);
        mCoordinates.add(end.point[2]);
    }

    public float[] GetCoordinatesLine(Point start, Point end){
        float buf[]={start.point[0],start.point[1],end.point[0],end.point[1]};
        return buf;
    }

    public Point GetCoordinates(float xRatio, float yRatio){
        float x,y,z;
        x=xRatio-(float)1;
        y=yRatio-(float)1;
        z=(float)0.0;
        return new Point(x,y,z);
    }

    class Point{
        public float[] point;
        Point(float x, float y, float z){
            point = new float[3];
            point[0] = x;
            point[1] = y;
            point[2] = z;
        }
    }
}

