package com.example.test.widget;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {

    // 整个列表项的根视图
    private View mConvertView;

    // 存储视图SparseArray集合，这是轻量级键值对
    private SparseArray<View> mView;

    /**
     * 构造函数，初始化 ViewHolder。
     * @param itemView 列表项的根视图
     */
    public BaseRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = new SparseArray<>();
        mConvertView = itemView;
    }

    /**
     * 获取整个列表项的根视图。
     * @return 整个列表项的根视图
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的资源ID获取该控件。
     *
     * @param viewId 控件的资源ID
     * @param <T>    控件的类型
     * @return 通过资源ID获取到的控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 通过ImageView的Id获取ImageView
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 通过TextView的Id获取TextView
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseRecycleViewHolder setText(@IdRes int viewId, CharSequence text) {
        if (text == null) {
            text = "";
        }
        getTextView(viewId).setText(text);
        return this;
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param stringResId 字符串资源id
     * @return
     */
    public BaseRecycleViewHolder setText(@IdRes int viewId, @StringRes int stringResId) {
        getTextView(viewId).setText(stringResId);
        return this;
    }



    /**
     * @param viewId
     * @param textColorResId 颜色资源id
     * @return
     */
    public BaseRecycleViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        getTextView(viewId).setTextColor(getConvertView().getContext().getResources().getColor(textColorResId));
        return this;
    }

    /**
     * @param viewId
     * @param textColor 颜色值
     * @return
     */
    public BaseRecycleViewHolder setTextColor(@IdRes int viewId, int textColor) {
        getTextView(viewId).setTextColor(textColor);
        return this;
    }

    /**
     * @param viewId
     * @param backgroundResId 背景资源id
     * @return
     */
    public BaseRecycleViewHolder setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    /**
     * @param viewId
     * @param color  颜色值
     * @return
     */
    public BaseRecycleViewHolder setBackgroundColor(@IdRes int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param colorResId 颜色值资源id
     * @return
     */
    public BaseRecycleViewHolder setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = getView(viewId);
        view.setBackgroundColor(getConvertView().getContext().getResources().getColor(colorResId));
        return this;
    }

    /**
     * @param viewId
     * @param imageResId 图像资源id
     * @return
     */
    public BaseRecycleViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

}
