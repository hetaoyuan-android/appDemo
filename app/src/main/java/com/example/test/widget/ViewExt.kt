package com.genew.mpublic.helper

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * view 扩展函数
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 切圆角。4个角切相同的大小，不支持切单个角
 * @param radius 单位像素
 */
fun View.initRoundRect(radius: Float = 10F) {
    // 图片设置圆角
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRoundRect(
                0,
                0,
                view?.width!!,
                view?.height!!,
                radius
            )
        }
    }
    this.clipToOutline = true
}

/**
 * 圆形角
 */
fun View.initRoundRound() {
    // 图片设置圆角
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            var radius = 0F
            try {
                radius = if (view?.width!! > view?.height!!) {
                    view.height / 2F
                } else {
                    view.width / 2F
                }
            } catch (e: Exception) {
            }
            outline?.setRoundRect(
                0,
                0,
                view?.width!!,
                view?.height!!,
                radius
            )
        }
    }
    this.clipToOutline = true
}