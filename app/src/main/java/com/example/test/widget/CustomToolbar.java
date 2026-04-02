package com.example.test.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.test.R;


public class CustomToolbar extends Toolbar {

    private TextView tvTitle;
    private TextView tvRightText;
    private ImageView imgvRight;
    private ImageView imgLeft;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_toolbar, this, true);

        // 初始化子控件
        tvTitle = findViewById(R.id.tv_toolbar_title);
        imgLeft = findViewById(R.id.img_left);
        tvRightText = findViewById(R.id.tv_toolbar_title_right);
        imgvRight = findViewById(R.id.imgv_toolbar);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title, int size) {
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tvTitle.setText(title);
    }

    /**
     * 获取标题 TextView（用于扩展）
     */
    public TextView getTitleView() {
        return tvTitle;
    }

    /**
     * 设置右侧文字按钮
     *
     * @param text     显示的文字
     * @param listener 点击事件监听器
     */
    public void setRightText(String text, final OnClickListener listener) {
        tvRightText.setText(text);
        tvRightText.setVisibility(VISIBLE);
        imgvRight.setVisibility(GONE);
        if (listener != null) {
            tvRightText.setOnClickListener(listener);
        }
    }

    /**
     * 设置右侧图标按钮
     *
     * @param resId    图标资源 ID
     * @param listener 点击事件监听器
     */
    public void setRightIcon(int resId, final OnClickListener listener) {
        imgvRight.setImageResource(resId);
        imgvRight.setVisibility(VISIBLE);
        tvRightText.setVisibility(GONE);
        if (listener != null) {
            imgvRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置左侧图片点击事件（默认关闭当前页面）
     *
     * @param activity 当前 Activity（需要是 AppCompatActivity 子类）
     */
    public void setLeftImageViewClickListener(AppCompatActivity activity) {
        imgLeft.setOnClickListener(v -> {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        });
    }

    /**
     * 设置左侧导航按钮点击事件（返回键）
     */
    public void setNavigationOnClickListener(final OnClickListener listener) {
        super.setNavigationOnClickListener(listener);
    }

    /**
     * 隐藏或显示左侧的返回按钮（navigation icon）
     */
    public void setLeftNavigationIconVisible(boolean visible) {
        imgLeft.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * 右侧文字显示隐藏
     *
     * @param visible
     */
    public void setRightTextVisible(boolean visible) {
        tvRightText.setVisibility(visible ? VISIBLE : GONE);
    }


    /**
     * 隐藏或显示右侧的 ImageView（图片按钮）
     */
    public void setRightImageViewVisible(boolean visible) {
        imgvRight.setVisibility(visible ? VISIBLE : GONE);
    }

    public TextView getTvRightText() {
        return tvRightText;
    }
}