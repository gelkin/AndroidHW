package com.gmail.mazinva.Colloq1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends Activity {
    int width;
    int height;

    int[] pointX;
    int[] pointY;

    int[] fixedPointsX;
    int[] fixedPointsY;

    int numberOfPoints;

    ArrayList<MyPoint> points;
    ArrayList<MyPoint> fixedPoints;
    Random random;
    Paint pWhite;
    Paint pRed;
    Paint pGreen;

    int numberOfFixedPoints;
    int tmpPos;
    MyPoint tmpPoint;

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
            mainLoop:
            for (int i = 0; i < numberOfPoints; i++) {
                if (fixedPointsX[i] == 1) {
                    continue;
                }

                numberOfFixedPoints = fixedPoints.size();
                tmpPoint = points.get(i);
                tmpX = tmpPoint.x;
                tmpY = tmpPoint.y;

                // is this point in the area of red fixed points?
                for (int j = 0; j < numberOfFixedPoints; j++) {
                    tmpFixedX = fixedPoints.get(j).x;
                    tmpFixedY = fixedPoints.get(j).y;

                    if ((((((tmpX - 1) + width) % width) == tmpFixedX) || ((tmpX) == tmpFixedX) || ((((tmpX - 1) + width) % width) == tmpFixedX))
                            && (((((tmpY - 1) + height) % height) == tmpFixedY) || ((tmpY) == tmpFixedY) || ((tmpY + 1) == tmpFixedY))) {
                        // text = "I came here";
                        tmpPoint = points.remove(i);
                        numberOfPoints--;
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

            for (int i = 0; i < numberOfPoints; i++) {
                canvas.drawPoint(points.get(i).x, points.get(i).y, pWhite);
            }
            for (int i = 0; i < numberOfFixedPoints; i++) {
                canvas.drawPoint(fixedPoints.get(i).x, fixedPoints.get(i).y, pRed);
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

        numberOfPoints = 5000;
        numberOfFixedPoints = 1;
        pointX = new int[5001];
        pointY = new int[5001];

        fixedPointsX = new int[5001];
        fixedPointsY = new int[5001];

        random = new Random();
        for (int i = 0; i < numberOfPoints; i++) {
            pointX[i] = random.nextInt(width);
            pointY[i] = random.nextInt(height);
        }

        fixedPoints = new ArrayList<MyPoint>(1);
        fixedPoints.add(0, new MyPoint(width / 2, height / 2));

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