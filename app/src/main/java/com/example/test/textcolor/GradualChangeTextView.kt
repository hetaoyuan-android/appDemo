package com.example.test.textcolor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.test.R

/**
 *
 * @ClassName: GradualChangeTextView
 * TODO 渐变文字
 */
class GradualChangeTextView : AppCompatTextView {

    //文字
    private var text = ""

    //文字大小
    private var textSize = 48

    //上层颜色
    private var upColor = Color.BLACK

    //底层颜色
    private var belowColor = Color.RED

    //滑动位置[默认从左到右滑动]
    private var mSlidingPosition = GRADUAL_CHANGE_LEFT

    companion object {
        //左侧开始滑动 [左侧->右侧滑动]
        const val GRADUAL_CHANGE_LEFT = 0

        //右侧开始滑动 [右侧->左侧滑动]
        const val GRADUAL_CHANGE_RIGHT = 1
    }

    //渐变进度 [0-1]
    var percent: Float = 0.0f
        set(value) {
            field = value
            invalidate()
        }

    //画笔
    private val mPaint = Paint().apply {
        //抗锯齿
        isAntiAlias = true
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.GradualChangeTextView)

        //设置文字
        text = obtainStyledAttributes.getString(R.styleable.GradualChangeTextView_gradual_text)
            .toString()

        //设置文字大小
        textSize =
            obtainStyledAttributes.getDimension(
                R.styleable.GradualChangeTextView_gradual_text_size,
                48f
            )
                .toInt()

        //设置上层颜色
        upColor = obtainStyledAttributes.getColor(
            R.styleable.GradualChangeTextView_gradual_up_color,
            Color.BLACK
        )

        //设置底层颜色
        belowColor = obtainStyledAttributes.getColor(
            R.styleable.GradualChangeTextView_gradual_below_color,
            Color.RED
        )

        //回收
        obtainStyledAttributes.recycle()

        //设置文字大小
        mPaint.textSize = textSize.toFloat()
    }

    //设置滑动位置
    fun setSlidingPosition(@GradualChangeType position: Int) {
        mSlidingPosition = position
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //绘制垂直居中线
//        drawCenterLineLineX(canvas)
//
//        //绘制水平居中线
//        drawCenterLineLineY(canvas)

        //文字宽度
        val textWidth = mPaint.measureText(text)

        //控件宽度的一半
        val viewWidth = width / 2


        //文字测量工具
        val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics

        //文字开始位置
        val start = viewWidth - textWidth / 2

        //文字结束的位置
        var end: Float = (height / 2) - (fontMetrics.ascent + fontMetrics.descent) / 2

        //绘制中心文字 [底层]
        drawBelowCenterText(canvas, start, end)

        when (mSlidingPosition) {
            GRADUAL_CHANGE_LEFT -> {
                //TODO 从左到右滑动
                end = start + textWidth * percent

                //绘制下层
                drawUpCenterText(canvas, upColor, start.toInt(), end.toInt())
            }
            GRADUAL_CHANGE_RIGHT -> {
                //TODO 从右到左滑动

                /**
                 *  结束位置
                 *  (1-percent) 表示从右往左滑动
                 */
                end = start + textWidth * (1 - percent)

                //上层滑动
                drawUpCenterText(canvas, upColor, end.toInt(), (start + textWidth).toInt())
            }
        }
    }


    /**
     * canvas 画布
     * percent 进度
     */
    private fun drawUpCenterText(canvas: Canvas, color: Int, start: Int, end: Int) {
        /**
         * 画布分层
         *  canvas.save()
         *
         *  这里的代码就被分层了
         *
         *  canvas.restore()
         *
         */
        mPaint.color = color

        canvas.save()

        //裁剪
        canvas.clipRect(start, 0, end, height)

        val textY = (height / 2) - (mPaint.descent() + mPaint.ascent()) / 2

        val start2 = width / 2 - mPaint.measureText(text) / 2

        canvas.drawText(text, start2, textY, mPaint)

        canvas.restore()
    }

    private fun drawBelowCenterText(canvas: Canvas, start: Float, end: Float) {

        /**
         * 画布分层
         *  canvas.save()
         *
         *  这里的代码就被分层了
         *
         *  canvas.restore()
         */

        mPaint.color = belowColor

        canvas.save()

        canvas.drawText(text, start, end, mPaint)

        canvas.restore()
    }

    //绘制垂直居中线
    private fun drawCenterLineLineX(canvas: Canvas) {

        //实线
        mPaint.style = Paint.Style.FILL

        //设置颜色
        mPaint.color = Color.RED

        //设置宽度
        mPaint.strokeWidth = 3f

        canvas.drawLine((width / 2).toFloat(), 0f, (width / 2).toFloat(), height.toFloat(), mPaint)
    }

    //绘制水平居中线
    private fun drawCenterLineLineY(canvas: Canvas) {
        //实线
        mPaint.style = Paint.Style.FILL

        //设置颜色
        mPaint.color = Color.RED

        //设置宽度
        mPaint.strokeWidth = 3f

        canvas.drawLine(
            0f,
            height / 2.toFloat(), width.toFloat(), height / 2.toFloat(), mPaint
        )
    }


//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_MOVE -> {
//                when (mSlidingPosition) {
//                    GRADUAL_CHANGE_LEFT -> {
//                        percent = event.x / width
//                    }
//                    GRADUAL_CHANGE_RIGHT -> {
//                        percent = 1 - event.x / width
//                    }
//                }
//                Log.i("szjTouchEvent", "x:${event.x}\ty:${event.y}\t${width}")
//                invalidate()
//            }
//        }
//        return true
//    }
}
