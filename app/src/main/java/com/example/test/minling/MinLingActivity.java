package com.example.test.minling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.test.R;
import com.example.test.annotation.OnClick;

public class MinLingActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawCanvas mCanvas;
    private DrawPath mPath;
    private Paint mPaint;
    private IBrush mBrush;

    private Button red, green, blue, normal, circle, redo, undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_ling);

        mPaint = new Paint();
        mPaint.setColor(0XFFFFFFFF);
        mPaint.setStrokeWidth(3);
        mBrush = new NormalBrush();


        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        normal = findViewById(R.id.normal);
        circle = findViewById(R.id.circle);
        redo = findViewById(R.id.redo);
        undo = findViewById(R.id.undo);
        mCanvas = findViewById(R.id.canvas);
        red.setOnClickListener(this);
        green.setOnClickListener(this);
        blue.setOnClickListener(this);
        normal.setOnClickListener(this);
        circle.setOnClickListener(this);
        redo.setOnClickListener(this);
        undo.setOnClickListener(this);
        mCanvas.setOnTouchListener(new DrawTouchListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.red:
                mPaint = new Paint();
                mPaint.setStrokeWidth(3);
                mPaint.setColor(0xFFFF0000);
                break;
            case R.id.green:
                mPaint = new Paint();
                mPaint.setStrokeWidth(3);
                mPaint.setColor(0xFF00FF00);
                break;
            case R.id.blue:
                mPaint = new Paint();
                mPaint.setStrokeWidth(3);
                mPaint.setColor(0xFF0000FF);
                break;
            case R.id.undo:
                mCanvas.undo();
                if (!mCanvas.canUndo()) {
                    undo.setEnabled(false);
                }
                redo.setEnabled(true);
                break;
            case R.id.redo:
                mCanvas.redo();
                if (mCanvas.canRedo()) {
                    redo.setEnabled(false);
                }
                undo.setEnabled(true);
                break;
            case R.id.normal:
                mBrush = new NormalBrush();
                break;
            case R.id.circle:
                mBrush = new CircleBrush();
                break;
        }
    }

    private class DrawTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPath = new DrawPath();
                mPath.paint = mPaint;
                mPath.path = new Path();
                mBrush.down(mPath.path, event.getX(), event.getY());
            } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                mBrush.move(mPath.path, event.getX(), event.getY());
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                mBrush.up(mPath.path, event.getX(), event.getY());
                mCanvas.add(mPath);
                mCanvas.isDrawing = true;
                undo.setEnabled(true);
                redo.setEnabled(false);
            }
            return true;
        }
    }
}