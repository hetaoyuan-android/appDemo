package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.annotation.AnnotationActivity;
import com.example.test.bezier.BezierActivity;
import com.example.test.bigimage.LargeImageActivity;
import com.example.test.chart.ChartActivity;
import com.example.test.circle.CircleActivity;
import com.example.test.contact.ContactActivity;
import com.example.test.coroutines.CoroutineActivity;
import com.example.test.flowlayout.FlowActivity;
import com.example.test.genged.ThreeGangedActivity;
import com.example.test.imagetext.ImageAddTextActivity;
import com.example.test.jd_home.NavActivity;
import com.example.test.minling.MinLingActivity;
import com.example.test.photopicker.selector.SelectorActivity;
import com.example.test.shapeview.ShapeViewActivity;
import com.example.test.slide_recyclerview.SlideRecyclerViewActivity;
import com.example.test.sortlist.SortListActivity;
import com.example.test.tabscroll.TabScrollActivity;
import com.example.test.textcolor.TextColorActivity;
import com.example.test.tree.TreeActivity;
import com.example.test.twoganged.TwoGangedActivity;
import com.example.test.videoplayer.GSYActivity;
import com.example.test.viewpager.ViewPagerActivity;
import com.example.test.viewpager2.ViewPager2Activity;

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

    public void contactClick(View view) {
        Intent intent = new Intent(Main2Activity.this, ContactActivity.class);
        startActivity(intent);
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

    public void tabList(View view) {
        Intent intent = new Intent(Main2Activity.this, TabScrollActivity.class);
        startActivity(intent);

    }
    public void videoPlayer(View view) {
        Intent intent = new Intent(Main2Activity.this, GSYActivity.class);
        startActivity(intent);
    }
    public void imgScale(View view) {
        Intent intent = new Intent(Main2Activity.this, FlowActivity.class);
        startActivity(intent);
    }

    public void threeGanged(View view) {
        Intent intent = new Intent(Main2Activity.this, ThreeGangedActivity.class);
        startActivity(intent);
    }

    public void twoGanged(View view) {
        Intent intent = new Intent(Main2Activity.this, TwoGangedActivity.class);
        startActivity(intent);
    }

    public void selectorImg(View view) {
        Intent intent = new Intent(Main2Activity.this, SelectorActivity.class);
        startActivity(intent);
    }

    public void shapeView(View view) {
        Intent intent = new Intent(Main2Activity.this, ShapeViewActivity.class);
        startActivity(intent);
    }

    public void viewPager(View view) {
        Intent intent = new Intent(Main2Activity.this, ViewPagerActivity.class);
        startActivity(intent);
    }

    public void textColor(View view) {
        Intent intent = new Intent(Main2Activity.this, TextColorActivity.class);
        startActivity(intent);
    }

    public void viewPager2(View view) {
        Intent intent = new Intent(Main2Activity.this, ViewPager2Activity.class);
        startActivity(intent);

    }
    public void slideRecyclerView(View view) {
        Intent intent = new Intent(Main2Activity.this, SlideRecyclerViewActivity.class);
        startActivity(intent);
    }
    public void jdHome(View view) {
        Intent intent = new Intent(Main2Activity.this, NavActivity.class);
        startActivity(intent);
    }

    /**
     * 加载大图
     */
    public void largeImage(View view) {
        Intent intent = new Intent(Main2Activity.this, LargeImageActivity.class);
        startActivity(intent);
    }

    public void coroutine(View view) {
        Intent intent = new Intent(Main2Activity.this, CoroutineActivity.class);
        startActivity(intent);
    }
    public void minLingDemo(View view) {
        Intent intent = new Intent(Main2Activity.this, MinLingActivity.class);
        startActivity(intent);
    }
}
