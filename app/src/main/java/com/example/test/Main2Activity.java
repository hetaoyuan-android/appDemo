package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.annotation.AnnotationActivity;
import com.example.test.bezier.BezierActivity;
import com.example.test.chart.ChartActivity;
import com.example.test.circle.CircleActivity;
import com.example.test.imagetext.ImageAddTextActivity;
import com.example.test.sortlist.SortListActivity;
import com.example.test.tree.TreeActivity;

public class Main2Activity extends AppCompatActivity {

    private Button btnChat, btnImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnChat = findViewById(R.id.btn_chat);
        btnImg = findViewById(R.id.btn_img);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ImageAddTextActivity.class);
                startActivity(intent);
            }
        });
    }

    public void bezier(View view) {
        Intent intent = new Intent(Main2Activity.this, BezierActivity.class);
        startActivity(intent);
    }

    public void circle(View view) {
        Intent intent = new Intent(Main2Activity.this, CircleActivity.class);
        startActivity(intent);
    }

    public void tree(View view) {
        Intent intent = new Intent(Main2Activity.this, TreeActivity.class);
        startActivity(intent);
    }


    public void annotation(View view) {
        Intent intent = new Intent(Main2Activity.this, AnnotationActivity.class);
        startActivity(intent);
    }
    public void sortList(View view) {
        Intent intent = new Intent(Main2Activity.this, SortListActivity.class);
        startActivity(intent);
    }
}
