package com.example.test.jd_home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.databinding.ActivityNavBinding;
import com.example.test.jd_home.v1.MainActivity;
import com.example.test.jd_home.v2.MainActivity2;
import com.example.test.jd_home.v3.MainActivity3;
import com.example.test.jd_home.v4.MainActivity4;


public class NavActivity extends AppCompatActivity {

    ActivityNavBinding vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        vb.btn1.setOnClickListener(v -> startActivity(new Intent(NavActivity.this, MainActivity.class)));
        vb.btn2.setOnClickListener(v -> startActivity(new Intent(NavActivity.this, MainActivity2.class)));
        vb.btn3.setOnClickListener(v -> startActivity(new Intent(NavActivity.this, MainActivity3.class)));
        vb.btn4.setOnClickListener(v -> startActivity(new Intent(NavActivity.this, MainActivity4.class)));
        vb.btn5.setOnClickListener(v -> startActivity(new Intent(NavActivity.this, MainActivity4.class)));
    }

}