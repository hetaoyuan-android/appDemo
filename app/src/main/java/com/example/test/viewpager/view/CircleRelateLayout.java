package com.example.test.viewpager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.test.R;


/**
 * @ClassName: CircleRelateLayout
 * TODO 可以设置圆角RelativeLayout
 */
public class CircleRelateLayout extends RelativeLayout {

    private final float leftBottomRadius;
    private final float rightBottomRadius;
    private final float leftTopRadius;
    private final float rightTopRadius;
    Path path = new Path();

    public CircleRelateLayout(Context context) {
        this(context, null);
    }

    public CircleRelateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRelateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //让ViewGroup执行onDraw方法
        setWillNotDraw(false);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRelateLayout);
        leftBottomRadius = typedArray.getDimension(R.styleable.CircleRelateLayout_left_bottom_radius, 0);
        rightBottomRadius = typedArray.getDimension(R.styleable.CircleRelateLayout_right_bottom_radius, 0);
        leftTopRadius = typedArray.getDimension(R.styleable.CircleRelateLayout_left_top_radius, 0);
        rightTopRadius = typedArray.getDimension(R.styleable.CircleRelateLayout_right_top_radius, 0);

        //回收资源
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = getRectF();
        float[] readius = {leftTopRadius, leftTopRadius,
                rightTopRadius, rightTopRadius,
                rightBottomRadius, rightBottomRadius,
                leftBottomRadius, leftBottomRadius};

        path.addRoundRect(rectF, readius,
                Path.Direction.CCW); // ---------

        canvas.clipPath(path, Region.Op.INTERSECT);

    }

    private RectF getRectF() {
        Rect rect = new Rect(0, 0,
                getWidth(), getHeight());
//        getDrawingRect(rect); // -------
        RectF rectF = new RectF(rect);
        return rectF;
    }
}
