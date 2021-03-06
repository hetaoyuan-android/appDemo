package com.example.test.viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.viewpager.utils.ColorUtil;
import com.example.test.viewpager.utils.banner.BannerAdapter;
import com.example.test.viewpager.utils.banner.ScaleTransformer;
import com.example.test.viewpager.view.BannerView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerActivity extends AppCompatActivity {

    private TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private ImageView imageView;

    private final int[] mDrawables = new int[]{R.drawable.guidao_d, R.drawable.guidao_c, R.drawable.guidao_b, R.drawable.guidao_a,};

    private final ArrayList<String> mNekWorkUrls = new ArrayList<String>() {{
        add("https://doge.zzzmh.cn/wallpaper/origin/b0fced9bf8864e88bb35b437b72f0c14.jpg");
        add("https://doge.zzzmh.cn/wallpaper/origin/22fd0510e4b24f1fabf19f8b601fce47.jpg");
        add("https://doge.zzzmh.cn/wallpaper/origin/c5398839880411ebb6edd017c2d2eca2.jpg");
        add("https://doge.zzzmh.cn/wallpaper/origin/6fbdf15fa797443e9be572740c4f14a9.png");
        add("https://doge.zzzmh.cn/wallpaper/origin/03723d65880711ebb6edd017c2d2eca2.jpg");
    }};
    private final Random mRandom = new Random();
    int mHotColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        imageView = findViewById(R.id.iv_id);
        tv = findViewById(R.id.title_tv);
        tv1 = findViewById(R.id.title_tv1);
        tv2 = findViewById(R.id.title_tv2);
        tv3 = findViewById(R.id.title_tv3);
        tv4 = findViewById(R.id.title_tv4);
        tv5 = findViewById(R.id.title_tv5);
        tv6 = findViewById(R.id.title_tv6);
        tv7 = findViewById(R.id.title_tv7);
        ViewPager viewPager = findViewById(R.id.viewPager);
        RelativeLayout viewPagerRootView = findViewById(R.id.viewPagerRootView);
        BannerView bannerView = findViewById(R.id.bannerView);

        //????????????Banner
        initHeadBanner(bannerView);

        //?????????Banner
        initBannerViewPager(viewPager, viewPagerRootView);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        initBitMap(bitmap);

    }

    private void initHeadBanner(BannerView bannerView) {
        //??????????????????
        // bannerView.setUrls(mDrawables);
        bannerView.setUrls(mNekWorkUrls);

        //????????? ???????????????????????????????????????????????????????????????1???
        bannerView.setOffscreenPageLimit(5);

        //page????????????
        //bannerView.setPageMargin(20);

        //??????????????????????????????
        bannerView.setCurrentItem(1);


        //??????????????????
        //bannerView.start(2000);

        //bannerView.setPageType(BannerView.PAGE_TRANSFORMER_SCALE);
        bannerView.setPageType(BannerView.PAGE_TRANSFORMER_STACK_PAGE);


        bannerView.setOnBannerItemClick(new BannerView.OnBannerItemClick() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(ViewPagerActivity.this, "?????????" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //????????????????????????,????????????,????????????
    boolean isDown ;

    private void initBannerViewPager(ViewPager viewPager, RelativeLayout viewPagerRootView) {
        Timer timer = new Timer();

        //???????????????
        viewPager.setAdapter(new BannerAdapter(this, mDrawables));

        //Pager???????????????
        viewPager.setPageMargin(20);

        //?????????
        viewPager.setOffscreenPageLimit(5);

        //??????????????? ????????? ??????????????? xml: android:clipChildren="false" ???????????????
        //??????
        viewPager.setPageTransformer(true, new ScaleTransformer());
        //?????????
//        viewPager.setPageTransformer(true, new StackPageTransformer(viewPager));

        //?????????????????? ???????????????
        viewPager.setCurrentItem(1);

        //???????????????ViewPager
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isDown) {
                    //????????????????????????
                    int page = viewPager.getCurrentItem() + 1;
                    runOnUiThread(() -> viewPager.setCurrentItem(page));
                }
            }
        };
        timer.schedule(timerTask, 0, 2500);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isDown = true;
            }

            @Override
            public void onPageSelected(int position) {
                int drawable = mDrawables[position % mDrawables.length];

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);

                ColorUtil.getInstance().initPalette(bitmap, new ColorUtil.onColorUtilClick() {
                    @Override
                    public void bitmapColors(int hotColor, int darkMutedColor, int lightMutedColor, int darkVibrantColor, int lightVibrantColor, int mutedColor, int vibrantColor) {
                        Log.i("szjHotColor2", hotColor + "");
                        //??????????????????
                        //circleRelateLayout.setBackgroundColor(hotColor);

                        //??????????????????
//                        int[] color = new int[]{mutedColor, lightVibrantColor, vibrantColor};
//                        ColorUtil.getInstance().setGradualChange(circleRelateLayout, color, GradientDrawable.Orientation.TL_BR, 0);

                        //????????????????????????
//                        ColorUtil.getInstance().setAnimatorColor(viewPagerRootView, 1000, mHotColor, hotColor);
                        mHotColor = hotColor;
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isDown = false;
            }
        });

        //viewPager?????????????????????????????????
//        viewPagerRootView.setOnTouchListener((v, event) -> viewPager.onTouchEvent(event));
    }


    //??????????????????
    public void onRandomClick(View view) {
        int drawable = mDrawables[mRandom.nextInt(mDrawables.length)];

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);

        imageView.setImageBitmap(bitmap);

        initBitMap(bitmap);
    }

    public void onAnimatorClick(View view) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(tv7, "backgroundColor",
                /*Red*//*0xFFFF8080, *//*Blue*//*0xFF8080FF);
                /*Red*/Color.RED, Color.BLUE, Color.YELLOW);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new android.animation.ArgbEvaluator());
//        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
//        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    private void initBitMap(Bitmap bitmap) {
        ColorUtil.getInstance().initPalette(bitmap,
                (hotColor, darkMutedColor, lightMutedColor, darkVibrantColor, lightVibrantColor, mutedColor, vibrantColor) -> {

                    runOnUiThread(() -> {
                        tv.setBackgroundColor(hotColor);

                        tv1.setBackgroundColor(darkMutedColor);

                        tv2.setBackgroundColor(lightMutedColor);

                        tv3.setBackgroundColor(darkVibrantColor);

                        tv4.setBackgroundColor(lightVibrantColor);

                        tv5.setBackgroundColor(mutedColor);

                        tv6.setBackgroundColor(vibrantColor);

                        int[] color = new int[]{mutedColor, lightVibrantColor, vibrantColor};

                        ColorUtil.getInstance().setGradualChange(tv7, color, GradientDrawable.Orientation.TL_BR, 55);

//                    Log.i("szjColor","????????????????");
//                    ColorUtil.getInstance().setAnimatorColor(tv7, 1000, this.hotColor, hotColor);
//                    this.hotColor = hotColor;

                    });
                });
    }
}
