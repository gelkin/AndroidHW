package com.gmail.mazinva.Colloq1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.*;

public class MyActivity extends Activity {
    int width;
    int height;

    int numberOfPoints;

    LinkedList<MyPoint> points;
    LinkedList<MyPoint> fixedPoints;
    ListIterator pointsItr;
    ListIterator fixedPointsItr;
    Random random;
    Paint pWhite;
    Paint pRed;
    Paint pGreen;

    int numberOfFixedPoints;
    int tmpPos;
    MyPoint tmpPoint;
    MyPoint tmpFixedPoint;


    int tmpX;
    int tmpY;

    int tmpFixedX;
    int tmpFixedY;

    int startTime;
    int endTime;
    double curTime;
    String fps;
    int k;

    /**
     * Called when the activity is first created.
     */

    class MyPoint {
        int x;
        int y;

        MyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            pointsItr = points.listIterator();
            fixedPointsItr = fixedPoints.listIterator();
            mainLoop:
            while (pointsItr.hasNext()) {
                tmpPoint = (MyPoint) pointsItr.next();
                tmpX = tmpPoint.x;
                tmpY = tmpPoint.y;

                // is this point in the area of red fixed points?
                while (fixedPointsItr.hasNext()) {
                    tmpFixedPoint = (MyPoint) fixedPointsItr.next();
                    tmpFixedX = tmpFixedPoint.x;
                    tmpFixedY = tmpFixedPoint.y;

                    if ((((((tmpX - 1) + width) % width) == tmpFixedX) || ((tmpX) == tmpFixedX) || ((((tmpX - 1) + width) % width) == tmpFixedX))
                            && (((((tmpY - 1) + height) % height) == tmpFixedY) || ((tmpY) == tmpFixedY) || ((tmpY + 1) == tmpFixedY))) {

                        pointsItr.remove();
                        fixedPoints.add(tmpPoint);
                        continue mainLoop;
                    }
                }

                tmpPos = random.nextInt(9);
                switch (tmpPos) {
                    case 0:
                        tmpPoint.x = ((tmpPoint.x - 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y - 1) + height) % height;
                        break;
                    case 1:
                        tmpPoint.x = ((tmpPoint.x) + width) % width;
                        tmpPoint.y = ((tmpPoint.y - 1) + height) % height;
                        break;
                    case 2:
                        tmpPoint.x = ((tmpPoint.x + 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y - 1) + height) % height;
                        break;
                    case 3:
                        tmpPoint.x = ((tmpPoint.x - 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y) + height) % height;
                        break;
                    // case 4: nothing to do
                    case 5:
                        tmpPoint.x = ((tmpPoint.x + 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y) + height) % height;
                        break;
                    case 6:
                        tmpPoint.x = ((tmpPoint.x - 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y + 1) + height) % height;
                        break;
                    case 7:
                        tmpPoint.x = ((tmpPoint.x) + width) % width;
                        tmpPoint.y = ((tmpPoint.y + 1) + height) % height;
                        break;
                    case 8:
                        tmpPoint.x = ((tmpPoint.x + 1) + width) % width;
                        tmpPoint.y = ((tmpPoint.y + 1) + height) % height;
                        break;
                    default:
                        break;
                }
            }

            pointsItr = points.listIterator();
            while (pointsItr.hasNext()) {
                tmpPoint = (MyPoint) pointsItr.next();
                canvas.drawPoint(tmpPoint.x, tmpPoint.y, pWhite);
            }

            fixedPointsItr = fixedPoints.listIterator();
            while (fixedPointsItr.hasNext()) {
                tmpFixedPoint = (MyPoint) fixedPointsItr.next();
                canvas.drawPoint(tmpFixedPoint.x, tmpFixedPoint.y, pRed);
            }


            endTime = (int) System.currentTimeMillis();
            curTime += 1000.0 / (endTime - startTime);
            startTime = (int) System.currentTimeMillis();

            if (k % 4 == 1) {
                fps = "FPS: " + ((int) (curTime / k));
            }
            canvas.drawText(fps, 100, 300, pGreen);
            k++;

            invalidate();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;

        numberOfPoints = 1000;
        points = new LinkedList<MyPoint>();
        // init points
        random = new Random();
        for (int i = 0; i < numberOfPoints; i++) {
            points.add(new MyPoint(random.nextInt(width), random.nextInt(height)));
        }

        numberOfFixedPoints = 1;
        fixedPoints = new LinkedList<MyPoint>();
        //init fixedPoints
        fixedPoints.add(new MyPoint(width / 2, height / 2));

        pWhite = new Paint();
        pWhite.setARGB(255, 255, 255, 255);
        pRed = new Paint();
        pRed.setARGB(255, 255, 0, 0);

        pGreen = new Paint();
        pGreen.setARGB(255, 0, 255, 0);

        startTime = (int) System.currentTimeMillis();
        fps = new String();
        k = 1;

        setContentView(new MyView(this));
    }
}