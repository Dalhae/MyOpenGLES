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
        paint.setStrokeWidth(5);

        Path path = new Path();
        path.moveTo(rwidth*100,rhight*100);
        path.lineTo(rwidth*100,rhight*900);
        path.lineTo(rwidth*900,rhight*900);
        canvas.drawPath(path,paint);
    }

    public void setViewSize(int hight, int width){
        mhight = hight;
        mwidth = width;
        rhight = (float)mhight/(float)1000;
        rwidth = (float)mwidth/(float)1000;
    }
}
