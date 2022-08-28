package com.example.test.minling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.annotation.OnClick;

public class MinLingActivity extends Activity implements View.OnClickListener {
    private DrawCanvas mCanvas;
    private DrawPath mPath;
    private Paint mPaint;
    private IBrush mBrush;

    private Button red, green, blue, normal, circle, redu, undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_ling);
        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        normal = findViewById(R.id.normal);
        circle = findViewById(R.id.circle);
        redu = findViewById(R.id.redu);
        undo = findViewById(R.id.undo);
        red.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}