package com.example.test.viewpager.utils.banner;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * @ClassName: StackPageTransformer
 * TODO 自己模仿写的仿照图书翻页
 */
public class StackPageTransformer implements ViewPager.PageTransformer {
    private final ViewPager viewPager;

    private final float SCALE_VALUE = 1f;

    //View 之间的偏移量
    private final float DEVIATION = 60f;

    //旋转
    private final float ROTATION = 60f;

    //图片是否叠加【默认不叠加】
    private final boolean isStack = false;


    public StackPageTransformer(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        Log.i("szjPosition2", position + "");

        /*
         * 当不滑动状态下:
         *      position = -1 左侧View
         *      position = 0 当前View
         *      position = 1 右侧View
         *
         * 当滑动状态下:
         *  向左滑动: [ position < 0 && position > -1]
         *    左侧View      position < -1
         *    当前View    0 ~ -1
         *    右侧View   1 ~ 0
         *
         * 向右滑动:[position > 0 && position < 1 ]
         *   左侧View  -1 < position < 0
         *   当前View  0 ~ 1
         *   右侧View  position > 1
         */

        int pageWidth = viewPager.getWidth();

        //隐藏左侧侧的view
        if (position == -1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        //当前View和右侧的View [让右侧View和当前View叠加起来]
        if (position >= 0) {
            float translationX;
            //这里不要晕! 改变isStack来看看效果吧!!
            if (isStack) {
                translationX = DEVIATION - (pageWidth) * position;
            } else {
                translationX = (DEVIATION - pageWidth) * position;
            }
            Log.i("szjTranslationX", translationX + "");
            view.setTranslationX(translationX);
        }

        //当前view
        if (position == 0) {
            view.setScaleX(SCALE_VALUE);
            view.setScaleY(SCALE_VALUE);
        } else {
            //左侧已经隐藏了，所以这里值的是右侧View的偏移量
            float scaleFactor = Math.min(SCALE_VALUE - position * 0.1f, SCALE_VALUE);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }

        //向左滑动
        if (position < 0 && position > -1) {
            //旋转
            view.setRotation(ROTATION * position);
            view.setAlpha(1 - Math.abs(position));
        } else {
            //透明度 其他状态不设置透明度
            view.setAlpha(1);
        }

        //向右滑动
        if (position > 0 && position < 1) {
            view.setRotation(0);
        }
    }
}
