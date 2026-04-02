package com.example.test.widget

import android.content.Context
import android.util.TypedValue


/**
 * @author yanteng
 * @date 2026-03-19 11:33
 * @description float扩展函数
 */

/**
 * dp转换px
 */
fun Float.dp2px(context : Context): Float {
    return dpToPx(context,this)
}

/**
 * sp转换px
 */
fun Float.sp2px(context : Context): Int {
    return sp2px(context,this)
}

fun dpToPx(context: Context?, dp: Float): Float {
    if (context == null) {
        return -1f
    }
    return dp * context.getResources().getDisplayMetrics().density
}

fun sp2px(context: Context, spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal,
        context.getResources().getDisplayMetrics()
    ).toInt()
}