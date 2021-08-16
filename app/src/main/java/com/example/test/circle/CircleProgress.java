package com.example.test.circle;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.example.test.R;



public class CircleProgress extends View {
    public static final int DEFAULT_PROGRESS_WIDTH = 2;
    public static final int DEFAULT_PROGRESS_BG_COLOR = Color.GRAY;
    public static final int DEFAULT_PROGRESS_COLOR = Color.RED;
    public static final int DEFAULT_PROGRESS = 0;
    public static final int DEFAULT_TEXT_COLOR = Color.RED;
    public static final int DEFAULT_TEXT_SIZE = 30;
    public static final int DEFUALT_DURATION = 2000;

    private float startAngle = -90;
    private int locationStart;
    private int progress = 0;
    private int progressColor;
    private int progressBgColor;
    private int circleTextColor;
    private int progressWidth;
    private String circleText;
    private boolean isVisibleText;
    private int circleTextSize;
    private Paint mBgPaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private RectF rectF;
    private ObjectAnimator animator;
    private Paint mCirclePaint;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化操作
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
        progressWidth = a.getDimensionPixelOffset(com.example.test.R.styleable.CircleProgress_progress_width, DEFAULT_PROGRESS_WIDTH);
        progressBgColor = a.getColor(R.styleable.CircleProgress_progress_bg_color, DEFAULT_PROGRESS_BG_COLOR);
        progressColor = a.getColor(R.styleable.CircleProgress_progress_color, DEFAULT_PROGRESS_COLOR);
        progress = a.getInteger(R.styleable.CircleProgress_progress, DEFAULT_PROGRESS);
        circleTextColor = a.getColor(R.styleable.CircleProgress_circle_text_color, DEFAULT_TEXT_COLOR);
        circleTextSize = a.getDimensionPixelOffset(R.styleable.CircleProgress_circle_text_size, DEFAULT_TEXT_SIZE);
        isVisibleText = a.getBoolean(R.styleable.CircleProgress_isVisibleText, false);
        locationStart = a.getInt(R.styleable.CircleProgress_location_start, 2);
        a.recycle();

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(progressWidth);
        mBgPaint.setColor(progressBgColor);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(progressWidth);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        switch (locationStart) {
            case 1:
                startAngle = -180;
                break;
            case 2:
                startAngle = -90;
                break;
            case 3:
                startAngle = 0;
                break;
            case 4:
                startAngle = 90;
                break;
        }

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(circleTextSize);
        mTextPaint.setColor(circleTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = widthSize < heightSize ? widthSize : heightSize;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        float radius = getWidth() / 2 - progressWidth / 2 ;
        canvas.drawCircle(cx, cy, radius-10, mBgPaint);
        canvas.drawCircle(cx, cy, radius-20, mCirclePaint);

        float sweepAngle = 360 / 100F * progress;
        rectF = new RectF(progressWidth / 2, progressWidth / 2, getWidth() - progressWidth / 2, getHeight() - progressWidth / 2);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mProgressPaint);

        if (isVisibleText && circleText != null) {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            canvas.drawText(circleText, getWidth() / 2, getHeight() / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, mTextPaint);
        }
    }

    /**
     * 动画执行进度
     *
     * @param endProgress 进度
     */
    public void startAnimProgress(@IntRange(from = 0, to = 100) int endProgress) {
        startAnimProgress(endProgress, DEFUALT_DURATION);
    }

    /**
     * 动画执行进度
     *
     * @param endProgress 进度
     * @param duration    动画持续时间
     */
    public void startAnimProgress(int endProgress, int duration) {
        animator = ObjectAnimator.ofInt(this, "progress", 0, endProgress);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                setProgress(progress);
            }
        });
        animator.start();
    }

    /**
     * 获取当前进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置进度
     */
    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.progress = progress;
        invalidate();
        if (mOnCircleProgressListener != null) {
            mOnCircleProgressListener.onProgressChange(progress);
        }
    }

    /**
     * 设置文本
     */
    public void setCircleText(String text) {
        circleText = text;
        invalidate();
    }

    public interface OnCircleProgressListener {
        void onProgressChange(int progress);
    }

    private OnCircleProgressListener mOnCircleProgressListener;

    /**
     * 监听进度
     */
    public void setOnCircleProgressListener(OnCircleProgressListener listener) {
        mOnCircleProgressListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOnCircleProgressListener = null;
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }
}
