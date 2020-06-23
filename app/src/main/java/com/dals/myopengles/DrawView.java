package com.dals.myopengles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DrawView extends View {

    private Paint paint;
    private int mhight, mwidth;
    private Rect rect;
    private float rhight, rwidth;
    private float[] lines;
    float zerox = (float)100;
    float zeroy = (float)900;

    public DrawView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        paint = new Paint();
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        canvas.drawRect(mwidth/2-100,mhight/2-100,mwidth/2+100,mhight/2+100,paint);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(R.color.colorGrayLine1));

        Path path = new Path();
        path.moveTo(rwidth*100,rhight*100);
        path.lineTo(rwidth*100,rhight*900);
        path.lineTo(rwidth*900,rhight*900);
        path.moveTo(rwidth*100,rhight*100);
        path.lineTo(rwidth*100-10,rhight*100);
        path.moveTo(rwidth*100,rhight*((800/2)+100));
        path.lineTo(rwidth*100-10,rhight*((800/2)+100));
        path.moveTo(rwidth*100,rhight*((800*(float)3.0/(float)4.0)+100));
        path.lineTo(rwidth*100-10,rhight*((800*(float)3.0/(float)4.0)+100));
        path.moveTo(rwidth*100,rhight*((800*(float)1.0/(float)4.0)+100));
        path.lineTo(rwidth*100-10,rhight*((800*(float)1.0/(float)4.0)+100));
        canvas.drawPath(path,paint);
    }

    public void setViewSize(int hight, int width){
        mhight = hight;
        mwidth = width;
        rhight = (float)mhight/(float)1000;
        rwidth = (float)mwidth/(float)1000;
    }

    public void makeGraphPath(Path path, float[] xpoint, float[] ypoint){
        int len;
        if(xpoint.length>ypoint.length){
            len = ypoint.length;
        } else { len = xpoint.length;}

        path.moveTo(rwidth * (zerox + xpoint[0]),rhight*(zeroy-ypoint[0]));
        for(int i=1;i<len;i++) {
            path.lineTo(rwidth * (zerox + xpoint[i]),rhight*(zeroy-ypoint[i]));
        }
    }

    private float scale(float x, float xmin, float xmax, float ymin, float ymax){
        return ((x*(ymax-ymin))/(xmax-xmin));
    }

    private float[] scaleArr(float[] x, float xmin, float xmax, float ymin, float ymax){
        int len = x.length;
        float[] y = new float[len];
        for(int i=0;i<len;i++){
            y[i] = scale(x[i],xmin,xmax,ymin,ymax);
        }
        return y;
    }
}
