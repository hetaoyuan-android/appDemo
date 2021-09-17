package com.example.test.textcolor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.textcolor.fragment.HomeFragment;
import com.example.test.textcolor.fragment.MyFragment;
import com.example.test.textcolor.fragment.SettingFragment;
import com.example.test.textcolor.fragment.TestFragment;

import java.util.ArrayList;

public class TextColorActivity extends AppCompatActivity {


    private Fragment homeFragment;
    private Fragment myFragment;
    private Fragment testFragment;
    private Fragment settingFragment;
    private ViewPager viewPager;
    private GradualChangeTextView textView1,textView2,textView3,textView4,changeTextView;
    private Button left,right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_color);
        homeFragment = new HomeFragment();
        myFragment = new MyFragment();
        testFragment = new TestFragment();
        settingFragment = new SettingFragment();
        viewPager = findViewById(R.id.viewPager);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        changeTextView = findViewById(R.id.changeTextView);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        left.setOnClickListener(v -> {
            changeTextView.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_LEFT);
            startAnimator(changeTextView);
        });
        right.setOnClickListener(v -> {
            changeTextView.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_RIGHT);
            startAnimator(changeTextView);
        });

        ArrayList<View> arrayList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundColor(Color.parseColor("#aabbcc"));
            arrayList.add(imageView);
        }
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Fragment> list = new ArrayList<>();
        ArrayList<GradualChangeTextView> textViews = new ArrayList<>();
        list.add(homeFragment);
        list.add(myFragment);
        list.add(testFragment);
        list.add(settingFragment);
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        textViews.add(textView4);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        textViews.get(viewPager.getCurrentItem()).setPercent(1f);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    GradualChangeTextView left = textViews.get(position);
                    GradualChangeTextView right = textViews.get(position + 1);
                    left.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_RIGHT);
                    right.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_LEFT);
                    left.setPercent(1 - positionOffset);
                    right.setPercent(positionOffset);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPageScrollStateChanged(int state) {
                textViews.forEach((e) ->{
                    if (e.getTag() == textViews.get(viewPager.getCurrentItem()).getTag()) {
                        e.setPercent(1f);
                    } else {
                        e.setPercent(0f);
                    }
                });
            }
        });
    }

    private void startAnimator(GradualChangeTextView view) {
        ObjectAnimator.ofFloat(view,"percent",0f, 1f)
                .setDuration(3000)
                .start();
    }
}
