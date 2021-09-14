package com.example.test.textcolor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * @ClassName: GradualChangeTv
 */
public class GradualChangeTv extends AppCompatTextView {
    public Paint mPaint = new Paint();

    public final String text = "起来不愿做奴隶的人们";

    //用来记录当前进度 【0-1】
    float progress = 0f;

    public GradualChangeTv(Context context) {
        this(context, null);
    }

    public GradualChangeTv(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradualChangeTv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.RED);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //设置文字大小
        mPaint.setTextSize(textSize);
    }

    //文字大小
    float textSize = 80;

    //默认从左到右滑动
    int type = GradualChangeTextView.GRADUAL_CHANGE_RIGHT;

    //缩放比例
    float scale = 1.2f;

    public void setType(@GradualChangeType int type) {
        this.type = type;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //文字宽度
        float textWidth = mPaint.measureText(text);
        //文字高度
        float textHeight = mPaint.descent() + mPaint.ascent();

        //获取当前控件的宽高的一半
        int viewWidth = getWidth() / 2;
        int viewHeight = getHeight() / 2;

        //设置文字大小
        mPaint.setTextSize(textSize);

        //绘制底层
        drawBottom(canvas, viewWidth, viewHeight, textWidth, textHeight);

        //从左到右滑动
        if (type == GradualChangeTextView.GRADUAL_CHANGE_LEFT) {
            //绘制上层【颜色渐变的】
            drawUp(canvas, viewWidth, viewHeight, textWidth, textHeight);
        } else if (type == GradualChangeTextView.GRADUAL_CHANGE_RIGHT) {
            //从右到左滑动
            drawRightToLeft(canvas, viewWidth, viewHeight, textWidth, textHeight);
        }
        //else{
        //....这里还可以扩展从上到下等等。。。
        //}

        //绘制居中线
        drawCenterLine(canvas, viewWidth, viewHeight);
    }

    private void drawRightToLeft(Canvas canvas, int viewWidth, int viewHeight, float textWidth, float textHeight) {
        mPaint.setColor(Color.GREEN);
        /*
         * 这里 left和right能够在此抽取出来,不过这样写很易懂,有需求自己弄吧！！！
         */
        canvas.save();
        //绘制文字X轴的位置 【文字开始的位置】
        float textX = viewWidth - textWidth / 2;

        //绘制文字Y轴的位置
        float textY = viewHeight - textHeight / 2;

        //文字结束的位置
        float end = viewWidth + mPaint.measureText(text) / 2;

        canvas.clipRect(end, 0, textX + textWidth * (1 - progress), getHeight());

        canvas.drawText(text, textX, textY, mPaint);
        canvas.restore();
    }

    //绘制上层【渐变的】
    private void drawUp(Canvas canvas, int viewWidth, int viewHeight, float textWidth, float textHeight) {
        mPaint.setColor(Color.BLACK);
        canvas.save();
        //绘制文字X轴的位置 【文字开始的位置】
        float textX = viewWidth - textWidth / 2;

        //绘制文字Y轴的位置
        float textY = viewHeight - textHeight / 2;

        /*
         * 裁剪
         * 参数一: 从文字开始位置绘制
         * 参数二: 顶部裁剪为0
         * 参数三: 绘制进度
         * 参数四: 绘制高度
         */
        canvas.clipRect((int) textX, 0, (int) textX + textWidth * progress, getHeight());

        mPaint.setSubpixelText(true);
        int i = mPaint.breakText(text, false, textWidth * progress, new float[]{});
        if (i >= text.length()) {
            i = text.length() - 1;
        }
        Log.i("szj当前文字", text.charAt(i) + "");


        canvas.scale(1, 1.5f, textX, textY);

        /*
         * 绘制文字
         * 参数一: 绘制文字
         * 参数二: x轴开始位置
         * 参数三: y 轴开始位置
         * 参数四: 画笔
         */
        canvas.drawText(text, textX, textY, mPaint);

        canvas.restore();
    }

    //绘制下层 不动的
    private void drawBottom(Canvas canvas, int viewWidth, int viewHeight, float textWidth, float textHeight) {
        mPaint.setColor(Color.RED);
        canvas.save();
        //绘制文字X轴的位置 [文字开始的位置]
        float textX = viewWidth - textWidth / 2;

        //绘制文字Y轴的位置
        float textY = viewHeight - textHeight / 2;

        //跟随者上层裁剪
        //canvas.clipRect((int) textX + textWidth * progress, 0, textWidth + viewWidth, getHeight());
//        canvas.scale(1f, 1.5f, textX, textX + 30);

        /*
         * 绘制文字
         * 参数一: 绘制文字
         * 参数二: x轴开始位置
         * 参数三: y 轴开始位置
         * 参数四: 画笔
         */
        canvas.drawText(text, textX, textY, mPaint);

        canvas.restore();
    }

    private void drawCenterLine(Canvas canvas, int viewWidth, int viewHeight) {
        //垂直线
        canvas.drawLine(viewWidth, 0, viewWidth, getHeight(), mPaint);

        //水平线
        canvas.drawLine(0, viewHeight, getWidth(), viewHeight, mPaint);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (type == GradualChangeTextView.GRADUAL_CHANGE_RIGHT) {
                //从右到左滑动
                progress = 1 - event.getX() / getWidth();
            } else if (type == GradualChangeTextView.GRADUAL_CHANGE_LEFT) {
                //从左到右滑动
                progress = event.getX() / getWidth();
            }
            invalidate();
        }
        return true;
    }
}
