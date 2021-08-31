package com.example.test.viewpager.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;

import androidx.palette.graphics.Palette;

/**
 * @Package: com.example.testproject.utils
 * @ClassName: ColorUtil
 * @Author: szj
 * @CreateDate: 8/20/21 7:59 PM
 * TODO 颜色工具类封装
 */
public class ColorUtil {
    private ColorUtil() {
    }

    private static volatile ColorUtil utils;

    public static ColorUtil getInstance() {
        if (utils == null) {
            synchronized (ColorUtil.class) {
                if (utils == null) {
                    utils = new ColorUtil();
                }
            }
        }
        return utils;
    }


    /**
     * TODO 设置渐变颜色
     *
     * @param view   需要设置的View
     * @param colors 渐变颜色
     * @param type   渐变位置  例如:GradientDrawable.Orientation.LEFT_RIGHT 从左到右
     * @param radius 圆角
     */
    public void setGradualChange(View view, int[] colors, GradientDrawable.Orientation type, int radius) {
        GradientDrawable drawable = new GradientDrawable(type, colors);
        drawable.setCornerRadius(radius);
        view.setBackground(drawable);
    }

    /**
     * TODO 给一张图片,吧图片上主要的颜色给你回调!
     *
     * @param bitmap  图片
     * @param onclick 颜色回调
     */
    public void initPalette(Bitmap bitmap, onColorUtilClick onclick) {
        new Thread(() -> Palette.from(bitmap).generate(palette -> {
            //以RGB压缩整数的形式从调色板返回静音和深色。
            int darkMutedColor = palette.getDarkMutedColor(Color.TRANSPARENT);

            //暗 柔和 [以RGB压缩整数的形式从调色板返回静音和浅色。]
            int lightMutedColor = palette.getLightMutedColor(Color.TRANSPARENT);

            //暗 鲜艳 [以RGB压缩整数的形式从调色板返回深色和鲜艳的颜色。]
            int darkVibrantColor = palette.getDarkVibrantColor(Color.TRANSPARENT);

            //量 鲜艳 [以RGB压缩整数的形式从调色板返回明亮的颜色。]
            int lightVibrantColor = palette.getLightVibrantColor(Color.TRANSPARENT);

            //柔和 [将调色板中的静音颜色作为RGB压缩整数返回。]
            int mutedColor = palette.getMutedColor(Color.TRANSPARENT);

            //以RGB压缩整数形式返回调色板中最鲜艳的颜色。
            int vibrantColor = palette.getVibrantColor(Color.TRANSPARENT);


            //从调色板中返回一个明亮且充满活力的样例。可能为空。
            Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();

            int hotColor = Color.TRANSPARENT;
            if (lightVibrantSwatch != null) {
                //谷歌推荐的:图片的整体的颜色rgb的混合痔---主色调
                int rgb = lightVibrantSwatch.getRgb();
                hotColor = getTranslucentColor(0.7f, rgb);
            }

            onclick.bitmapColors(hotColor, darkMutedColor,
                    lightMutedColor,
                    darkVibrantColor,
                    lightVibrantColor,
                    mutedColor,
                    vibrantColor);
        })).start();
    }


    public void setAnimatorColor(View view, int duration, int... colors) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor",
                colors);
        colorAnim.end();
        colorAnim.setDuration(duration);
        colorAnim.setEvaluator(new android.animation.ArgbEvaluator());
//        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
//        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    /**
     * rgb & 0xff运算如下:
     * a           r       g          b
     * 11011010  01111010  10001010  10111010
     * 11111111 做运算
     * 10111010这样就取出了blue了
     * int green = rgb>> 8 & 0xff运算如下:
     * rgb>>8运算:
     * 11011010  01111010  10001010  这里砍掉10111010
     * 和0xff运算
     * 11111111 做运算
     * 100001010  这样就取出了g了
     *
     * @param percent 透明度
     * @param rgb     主题颜色
     * @return 颜色
     */
    private int getTranslucentColor(float percent, int rgb) {
        //10101011110001111
        int blue = rgb & 0xff;//源码就是这样玩的
        int green = rgb >> 8 & 0xff;
        int red = rgb >> 16 & 0xff;
        int alpha = rgb >>> 24;
        alpha = Math.round(alpha * percent);
        //int blue=Color.blue(rgb); //会自动给你分析出颜色
        return Color.argb(alpha, red, green, blue);
    }

    public interface onColorUtilClick {
        void bitmapColors(int hotColor,
                          int darkMutedColor,
                          int lightMutedColor,
                          int darkVibrantColor,
                          int lightVibrantColor,
                          int mutedColor,
                          int vibrantColor);
    }
}
