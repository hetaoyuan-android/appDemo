package com.example.test.jd_home.v4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.test.R;
import com.example.test.jd_home.adapter.HomeFragmentPageAdapter;
import com.example.test.jd_home.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_jd);

        viewPager = findViewById(R.id.customViewPager);

        StatusBarUtil.immersive(this);

        initData();
    }

    private void initData() {
        List<String> listTitle = new ArrayList<>();
        List<Fragment> listFragments = new ArrayList<>();

        listTitle.add("首页");
        listFragments.add(HomePageFragment4.getInstance());

        HomeFragmentPageAdapter homeFragmentPageAdapter = new HomeFragmentPageAdapter(getSupportFragmentManager(), listFragments);
        viewPager.setAdapter(homeFragmentPageAdapter);

    }

}