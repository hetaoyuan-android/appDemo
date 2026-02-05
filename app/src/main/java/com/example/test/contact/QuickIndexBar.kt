package com.example.test.contact

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.test.R

const val INDEX_TOP = "↑"

class QuickIndexBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val letters = arrayOf(INDEX_TOP, "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 32f
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.blue_line_color_dark)
    }

    private var cellHeight = 0f
    var onLetterChangeListener: ((String) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cellHeight = h.toFloat() / letters.size
    }

    override fun onDraw(canvas: Canvas) {
        for (i in letters.indices) {
            if (letters[i] == INDEX_TOP) {
                paint.isFakeBoldText = true // 加粗箭头
            } else {
                paint.isFakeBoldText = false
            }
            val x = width / 2f
            val y = cellHeight / 2 + paint.textSize / 2 + i * cellHeight
            canvas.drawText(letters[i], x, y, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val index = (event.y / cellHeight).toInt()
                if (index in letters.indices) {
                    onLetterChangeListener?.invoke(letters[index])
                }
            }
        }
        return true
    }
}