package com.example.test.viewpager.utils.banner;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;


/**
 * @ClassName: ScaleTransformer
 * TODO 自己模仿写的 缩放[中间大,两边小]ViewPager
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final float MAX_SCALE = 1.0f;//0缩放
    private static final float MIN_SCALE = 0.80f;//0.85缩放


    @Override
    public void transformPage(@NonNull View view, float position) {

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

        if (position != 0) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else {
            view.setScaleX(MAX_SCALE);
            view.setScaleY(MAX_SCALE);
        }
        if (position < 1) {
            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
            Log.i("szjScaleFactor", scaleFactor + "\tposition:" + position);

            view.setScaleX(scaleFactor);

            view.setScaleY(scaleFactor);

        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
    }
}
