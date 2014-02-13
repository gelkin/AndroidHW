package com.gmail.mazinva.ColourfulTable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public static final int WIDTH = 240;
    public static final int HEIGHT = 320;

    int[] cells;
    int[] cells_buf;
    int[] tmp;

    Matrix matrix;
    Paint p;

    int seed; // number of colours. Set in method onCreate.
    int step; // difference between two neighbour colours.

    int startTime;
    int endTime;
    double curTime;
    String fps;
    int k;

    class WhirlView extends View {

        public WhirlView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {

            canvas.setMatrix(matrix);


            for (int j = 1; j < (HEIGHT - 1); j++) {
                // TODO: 1. Вынести из внутреннего цикла int k = WIDTH * j;
                // TODO: 2. Запилить константы типа MY_CONST
                // TODO: 3. Сделать FPS (каждые n кадров)
                // TODO: 4. Сделать на весь экран (через нативные методы)

                for (int i = 1; i < (WIDTH - 1); i++) {

                    if (((cells[WIDTH * (j - 1) + i - 1] - cells[WIDTH * j + i]) == step) ||
                            ((cells[WIDTH * j + i - 1] - cells[WIDTH * j + i]) == step) ||
                            ((cells[WIDTH * (j + 1) + i - 1] - cells[WIDTH * j + i]) == step) || // left part

                            ((cells[WIDTH * (j - 1) + i] - cells[WIDTH * j + i]) == step) ||
                            ((cells[WIDTH * (j + 1) + i] - cells[WIDTH * j + i]) == step) || // up&down part

                            ((cells[WIDTH * (j - 1) + i + 1] - cells[WIDTH * j + i]) == step) ||
                            ((cells[WIDTH * j + i + 1] - cells[WIDTH * j + i]) == step) ||
                            ((cells[WIDTH * (j + 1) + i + 1] - cells[WIDTH * j + i]) == step)) { // right part


                        cells_buf[WIDTH * j + i] = cells[WIDTH * j + i] + step;

                    } else if ((cells[WIDTH * j + i] == (seed - 1) * step) &&

                            ((cells[WIDTH * (j - 1) + i - 1] == 0) ||
                                    (cells[WIDTH * j + i - 1] == 0) ||
                                    (cells[WIDTH * (j + 1) + i - 1] == 0) ||  // left part

                                    (cells[WIDTH * (j - 1) + i] == 0) ||
                                    (cells[WIDTH * (j + 1) + i] == 0) ||// up&down part

                                    (cells[WIDTH * (j - 1) + i + 1] == 0) ||
                                    (cells[WIDTH * j + i + 1] == 0) ||
                                    (cells[WIDTH * (j + 1) + i + 1] == 0))) {

                        cells_buf[WIDTH * j + i] = 0;
                    } else {
                        cells_buf[WIDTH * j + i] = cells[WIDTH * j + i];
                    }
                }
            }


            // upper row
            for (int i = 1; i < (WIDTH - 1); i++) {

                if (((cells[WIDTH * (HEIGHT - 1) + i - 1] - cells[i]) == step) ||
                        ((cells[i - 1] - cells[i]) == step) ||
                        ((cells[WIDTH + i - 1] - cells[i]) == step) ||
                        // left part

                        ((cells[WIDTH * (HEIGHT - 1) + i] - cells[i]) == step) ||
                        ((cells[WIDTH + i] - cells[i]) == step) ||
                        // up&down part

                        ((cells[WIDTH * (HEIGHT - 1) + i + 1] - cells[i]) == step) ||
                        ((cells[i + 1] - cells[i]) == step) ||
                        ((cells[WIDTH + 1] - cells[i]) == step)
                        ) { // right part

                    cells_buf[i] = cells[i] + step;

                } else if ((cells[i] == (seed - 1) * step) &&

                        ((cells[WIDTH * (HEIGHT - 1) + i - 1] == 0) ||
                                (cells[i - 1] == 0) ||
                                (cells[WIDTH + i - 1] == 0) ||  // left part

                                (cells[WIDTH * (HEIGHT - 1) + i] == 0) ||
                                (cells[WIDTH + i] == 0) || // up&down part

                                (cells[WIDTH * (HEIGHT - 1) + i + 1] == 0) ||
                                (cells[i + 1] == 0) ||
                                (cells[WIDTH + i + 1] == 0))) { // right part

                    cells_buf[i] = 0;
                } else {
                    cells_buf[i] = cells[i];
                }
            }

            // downer row
            for (int i = 1; i < (WIDTH - 1); i++) {

                if (((cells[WIDTH * (HEIGHT - 2) + i - 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step) ||
                        ((cells[WIDTH * (HEIGHT - 1) + i - 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step) ||
                        ((cells[i - 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step) || // left part

                        ((cells[WIDTH * (HEIGHT - 2) + i] - cells[WIDTH * (HEIGHT - 1) + i]) == step) ||
                        ((cells[i] - cells[WIDTH * (HEIGHT - 1) + i]) == step) || // up&down part

                        ((cells[WIDTH * (HEIGHT - 2) + i + 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step) ||
                        ((cells[WIDTH * (HEIGHT - 1) + i + 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step) ||
                        ((cells[i + 1] - cells[WIDTH * (HEIGHT - 1) + i]) == step)) { // right part

                    cells_buf[WIDTH * (HEIGHT - 1) + i] = cells[WIDTH * (HEIGHT - 1) + i] + step;

                } else if ((cells[WIDTH * (HEIGHT - 1) + i] >= (seed - 1) * step) &&

                        ((cells[WIDTH * (HEIGHT - 2) + i - 1] == 0) ||
                                (cells[WIDTH * (HEIGHT - 1) + i - 1] == 0) ||
                                (cells[i - 1] == 0) ||  // left part

                                (cells[WIDTH * (HEIGHT - 2) + i] == 0) ||
                                (cells[i] == 0) || // up&down part

                                (cells[WIDTH * (HEIGHT - 2) + i + 1] == 0) ||
                                (cells[WIDTH * (HEIGHT - 1) + i + 1] == 0) ||
                                (cells[i + 1]) == 0)) { // right part

                    cells_buf[WIDTH * (HEIGHT - 1) + i] = 0;
                } else {
                    cells_buf[WIDTH * (HEIGHT - 1) + i] = cells[WIDTH * (HEIGHT - 1) + i];
                }
            }

            // left column
            for (int j = 1; j < (HEIGHT - 1); j++) {

                if (((cells[WIDTH * (j - 1) + (WIDTH - 1)] - cells[WIDTH * j]) == step) ||
                        ((cells[WIDTH * j + (WIDTH - 1)] - cells[WIDTH * j]) == step) ||
                        ((cells[WIDTH * (j + 1) + (WIDTH - 1)] - cells[WIDTH * j]) == step) || // left part

                        ((cells[WIDTH * (j - 1)] - cells[WIDTH * j]) == step) ||
                        ((cells[WIDTH * (j + 1)] - cells[WIDTH * j]) == step) || // up&down part

                        ((cells[WIDTH * (j - 1) + 1] - cells[WIDTH * j]) == step) ||
                        ((cells[WIDTH * j + 1] - cells[WIDTH * j]) == step) ||
                        ((cells[WIDTH * (j + 1) + 1] - cells[WIDTH * j]) == step)) { // right part


                    cells_buf[WIDTH * j] = cells[WIDTH * j] + step;

                } else if ((cells[WIDTH * j] >= (seed - 1) * step) &&

                        ((cells[WIDTH * (j - 1) + (WIDTH - 1)] == 0) ||
                                (cells[WIDTH * j + (WIDTH - 1)] == 0) ||
                                (cells[WIDTH * (j + 1) + (WIDTH - 1)] == 0) ||  // left part

                                (cells[WIDTH * (j - 1)] == 0) ||
                                (cells[WIDTH * (j + 1)] == 0) || // up&down part

                                (cells[WIDTH * (j - 1) + 1] == 0) ||
                                (cells[WIDTH * j + 1] == 0) ||
                                (cells[WIDTH * (j + 1) + 1] == 0))) { // right part

                    cells_buf[WIDTH * j] = 0;
                } else {
                    cells_buf[WIDTH * j] = cells[WIDTH * j];
                }
            }

            // right column
            for (int j = 1; j < (HEIGHT - 1); j++) {

                if (((cells[WIDTH * (j - 1) + (WIDTH - 2)] - cells[WIDTH * j + (WIDTH - 1)]) == step) ||
                        ((cells[WIDTH * j + (WIDTH - 2)] - cells[WIDTH * j + (WIDTH - 1)]) == step) ||
                        ((cells[WIDTH * (j + 1) + (WIDTH - 2)] - cells[WIDTH * j + (WIDTH - 1)]) == step) || // left part

                        ((cells[WIDTH * (j - 1) + (WIDTH - 1)] - cells[WIDTH * j + (WIDTH - 1)]) == step) ||
                        ((cells[WIDTH * (j + 1) + (WIDTH - 1)] - cells[WIDTH * j + (WIDTH - 1)]) == step) || // up&down part

                        ((cells[WIDTH * (j - 1)] - cells[WIDTH * j + (WIDTH - 1)]) == step) ||
                        ((cells[WIDTH * j] - cells[WIDTH * j + (WIDTH - 1)]) == step) ||
                        ((cells[WIDTH * (j + 1)] - cells[WIDTH * j + (WIDTH - 1)]) == step)) { // right part

                    cells_buf[WIDTH * j + (WIDTH - 1)] = cells[WIDTH * j + (WIDTH - 1)] + step;

                } else if ((cells[WIDTH * j + (WIDTH - 1)] >= (seed - 1) * step) &&

                        ((cells[WIDTH * (j - 1) + (WIDTH - 2)] == 0) ||
                                (cells[WIDTH * j + (WIDTH - 2)] == 0) ||
                                (cells[WIDTH * (j + 1) + (WIDTH - 2)] == 0) ||  // left part

                                (cells[WIDTH * (j - 1) + (WIDTH - 1)] == 0) ||
                                (cells[WIDTH * (j + 1) + (WIDTH - 1)] == 0) || // up&down part

                                (cells[WIDTH * (j - 1)] == 0) ||
                                (cells[WIDTH * j] == 0) ||
                                (cells[WIDTH * (j + 1)] == 0))) { // right part

                    cells_buf[WIDTH * j + (WIDTH - 1)] = 0;
                } else {
                    cells_buf[WIDTH * j + (WIDTH - 1)] = cells[WIDTH * j + (WIDTH - 1)];
                }
            }


            // corners:
            // left&up
            if (((cells[WIDTH * HEIGHT - 1] - cells[0]) == step) ||
                    ((cells[(WIDTH - 1)] - cells[0]) == step) ||
                    ((cells[WIDTH + (WIDTH - 1)] - cells[0]) == step) || // left part

                    ((cells[WIDTH * (HEIGHT - 1)] - cells[0]) == step) ||
                    ((cells[WIDTH] - cells[0]) == step) || // up&down part

                    ((cells[WIDTH * (HEIGHT - 1) + 1] - cells[0]) == step) ||
                    ((cells[1] - cells[0]) == step) ||
                    ((cells[WIDTH + 1] - cells[0]) == step)) { // right part

                cells_buf[0] = cells[0] + step;
            } else if ((cells[0] >= (seed - 1) * step) &&
                    ((cells[WIDTH * HEIGHT - 1] == 0) || (cells[(WIDTH - 1)] == 0) || (cells[WIDTH + (WIDTH - 1)] == 0) ||
                            (cells[WIDTH * (HEIGHT - 1)] == 0) || (cells[WIDTH] == 0) || (cells[WIDTH * (HEIGHT - 1) + 1] == 0) ||
                            (cells[1] == 0) || (cells[WIDTH + 1] == 0))) {

                cells_buf[0] = 0;
            } else {
                cells_buf[0] = cells[0];
            }

            // right&up
            if (((cells[WIDTH * (HEIGHT - 1) + (WIDTH - 2)] - cells[(WIDTH - 1)]) == step) ||
                    ((cells[(WIDTH - 2)] - cells[(WIDTH - 1)]) == step) ||
                    ((cells[WIDTH + (WIDTH - 2)] - cells[(WIDTH - 1)]) == step) || // left part

                    ((cells[WIDTH * (HEIGHT - 1) + (WIDTH - 1)] - cells[(WIDTH - 1)]) == step) ||
                    ((cells[WIDTH + (WIDTH - 1)] - cells[(WIDTH - 1)]) == step) || // up&down part

                    ((cells[WIDTH * (HEIGHT - 1)] - cells[(WIDTH - 1)]) == step) ||
                    ((cells[0] - cells[(WIDTH - 1)]) == step) ||
                    ((cells[WIDTH] - cells[(WIDTH - 1)]) == step)) { // right part

                cells_buf[(WIDTH - 1)] = cells[(WIDTH - 1)] + step;
            } else if ((cells[(WIDTH - 1)] >= (seed - 1) * step) &&
                    ((cells[WIDTH * (HEIGHT - 1) + (WIDTH - 2)] == 0) || (cells[(WIDTH - 2)] == 0) || (cells[WIDTH + (WIDTH - 2)] == 0) ||
                            (cells[WIDTH * (HEIGHT - 1) + (WIDTH - 1)] == 0) || (cells[WIDTH + (WIDTH - 1)] == 0) || (cells[WIDTH * (HEIGHT - 1)] == 0) ||
                            (cells[0] == 0) || (cells[WIDTH] == 0))) {

                cells_buf[(WIDTH - 1)] = 0;
            } else {
                cells_buf[(WIDTH - 1)] = cells[(WIDTH - 1)];
            }

            // left&down
            if (((cells[WIDTH * (HEIGHT - 1) - 1] - cells[WIDTH * (HEIGHT - 1)]) == step) ||
                    ((cells[WIDTH * HEIGHT - 1] - cells[WIDTH * (HEIGHT - 1)]) == step) ||
                    ((cells[(WIDTH - 1)] - cells[WIDTH * (HEIGHT - 1)]) == step) || // left part

                    ((cells[WIDTH * (HEIGHT - 2)] - cells[WIDTH * (HEIGHT - 1)]) == step) ||
                    ((cells[0] - cells[WIDTH * (HEIGHT - 1)]) == step) || // up&down part

                    ((cells[WIDTH * (HEIGHT - 2) + 1] - cells[WIDTH * (HEIGHT - 1)]) == step) ||
                    ((cells[WIDTH * (HEIGHT - 1) + 1] - cells[WIDTH * (HEIGHT - 1)]) == step) ||
                    ((cells[1] - cells[WIDTH * (HEIGHT - 1)]) == step)) { // right part

                cells_buf[WIDTH * (WIDTH - 1)] = cells[WIDTH * (WIDTH - 1)] + step;
            } else if ((cells[WIDTH * (WIDTH - 1)] >= (seed - 1) * step) &&
                    ((cells[WIDTH * (HEIGHT - 1) - 1] == 0) || (cells[WIDTH * HEIGHT - 1] == 0) || (cells[(WIDTH - 1)] == 0) ||
                            (cells[WIDTH * (HEIGHT - 2)] == 0) || (cells[0] == 0) || (cells[WIDTH * (HEIGHT - 2) + 1] == 0) ||
                            (cells[WIDTH * (HEIGHT - 1) + 1] == 0) || (cells[1] == 0))) {

                cells_buf[WIDTH * (WIDTH - 1)] = 0;
            } else {
                cells_buf[WIDTH * (WIDTH - 1)] = cells[WIDTH * (WIDTH - 1)];
            }

            // right&down
            if (((cells[WIDTH * (HEIGHT - 1) - 2] - cells[WIDTH * HEIGHT - 1]) == step) ||
                    ((cells[WIDTH * HEIGHT - 2] - cells[WIDTH * HEIGHT - 1]) == step) ||
                    ((cells[(WIDTH - 2)] - cells[WIDTH * HEIGHT - 1]) == step) || // left part

                    ((cells[WIDTH * (HEIGHT - 1) - 1] - cells[WIDTH * HEIGHT - 1]) == step) ||
                    ((cells[(WIDTH - 1)] - cells[WIDTH * HEIGHT - 1]) == step) || // up&down part

                    ((cells[WIDTH * (HEIGHT - 2)] - cells[WIDTH * HEIGHT - 1]) == step) ||
                    ((cells[WIDTH * (HEIGHT - 1)] - cells[WIDTH * HEIGHT - 1]) == step) ||
                    ((cells[0] - cells[WIDTH * HEIGHT - 1]) == step)) { // right part

                cells_buf[WIDTH * HEIGHT - 1] = cells[WIDTH * HEIGHT - 1] + step;
            } else if ((cells[WIDTH * HEIGHT - 1] >= (seed - 1) * step) &&
                    ((cells[WIDTH * (HEIGHT - 1) - 2] == 0) || (cells[WIDTH * HEIGHT - 2] == 0) || (cells[(WIDTH - 2)] == 0) ||
                            (cells[WIDTH * (HEIGHT - 1) - 1] == 0) || (cells[(WIDTH - 1)] == 0) || (cells[WIDTH * (HEIGHT - 2)] == 0) ||
                            (cells[WIDTH * (HEIGHT - 1)] == 0) || (cells[0]) == 0)) {

                cells_buf[WIDTH * HEIGHT - 1] = 0;
            } else {
                cells_buf[WIDTH * HEIGHT - 1] = cells[WIDTH * HEIGHT - 1];
            }


            canvas.drawBitmap(cells_buf, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);

            tmp = cells;
            cells = cells_buf;
            cells_buf = tmp;

            endTime = (int) System.currentTimeMillis();
            curTime += 1000.0 / (endTime - startTime);
            startTime = (int) System.currentTimeMillis();

            if (k % 10 == 1) {
                fps = "FPS: " + ((int) (curTime / k));
            }
            canvas.drawText(fps, 100, 300, p);
            k++;

            invalidate();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cells = new int[WIDTH * HEIGHT];
        cells_buf = new int[WIDTH * HEIGHT];

        matrix = new Matrix();
        matrix.setScale(3, 3, 0, 0);

        p = new Paint();
        p.setARGB(255, 255, 255, 255);

        seed = 20;
        step = 17112241;
        startTime = (int) System.currentTimeMillis();


        k = 1;

        Random random = new Random();

        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                cells[WIDTH * j + i] = random.nextInt(seed) * step;
            }
        }

        setContentView(new WhirlView(this));
    }
}