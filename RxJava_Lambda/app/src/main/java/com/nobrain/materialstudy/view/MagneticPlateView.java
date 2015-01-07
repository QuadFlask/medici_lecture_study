package com.nobrain.materialstudy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.EView;

/**
 * Created by Steve SeongUg Jung on 15. 1. 4..
 */
@EView
public class MagneticPlateView extends View {


    private Point ballCenterPoint;

    private int ballRadious;
    private int plateWidth;
    private int plateHeight;
    private Paint ballPaint;

    private Paint pipePaint;
    private int pipeLength;
    private int pipeRowCount;
    private int pipeColumnCount;
    private int pipeMargin;

    public MagneticPlateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ballCenterPoint = new Point();
        ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(Color.RED & 0x66FFFFFF);

        pipePaint = new Paint();
        pipePaint.setColor(Color.BLACK);

        setWillNotDraw(false);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.save();
        drawIronPipe(canvas);
        drawMagneticBall(canvas);
        canvas.restore();

    }

    private void drawMagneticBall(Canvas canvas) {
        canvas.drawCircle(ballCenterPoint.x, ballCenterPoint.y, ballRadious, ballPaint);
    }


    private void drawIronPipe(Canvas canvas) {

        final int ballCenterX = ballCenterPoint.x;
        final int ballCenterY = ballCenterPoint.y;

        for (int rowIdx = 0; rowIdx < pipeRowCount; ++rowIdx) {
            for (int colIdx = 0; colIdx < pipeColumnCount; ++colIdx) {

                int startX = colIdx * (pipeLength + pipeMargin);
                int startY = rowIdx * pipeLength;

                double radian = Math.atan2(ballCenterX - startX, ballCenterY - startY);

                int stopX = (int) (startX + Math.sin(radian) * pipeLength);
                int stopY = (int) (startY + Math.cos(radian) * pipeLength);

                canvas.drawLine(startX, startY, stopX, stopY, pipePaint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        plateWidth = MeasureSpec.getSize(widthMeasureSpec);
        plateHeight = MeasureSpec.getSize(heightMeasureSpec);

        ballRadious = plateWidth / 10;
        pipeLength = ballRadious / 5;

        setBallCenter(plateWidth / 2, plateHeight / 2);

        pipeMargin = pipeLength / 2;

        pipeColumnCount = plateWidth / (pipeLength);
        pipeRowCount = plateHeight / (pipeLength);


    }

    public void setBallCenter(int cx, int cy) {
        ballCenterPoint.set(cx, cy);
    }

    public void setTouchDiff(int diffX, int diffY) {
        ballCenterPoint.x += diffX;
        ballCenterPoint.y += diffY;

    }
}
