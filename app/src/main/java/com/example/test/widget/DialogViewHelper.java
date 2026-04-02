package com.example.test.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;


import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

class DialogViewHelper {

    private Context mContext;


    private SparseArray<WeakReference<View>> mViews = new SparseArray<>();
    private View mContentView;

    public DialogViewHelper(Context context, @LayoutRes int viewLayoutId) {
        this.mContext = context;
        mContentView = LayoutInflater.from(context).inflate(viewLayoutId, null);
    }

    public DialogViewHelper(Context context) {
        this.mContext = context;
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    public void setText(int i, CharSequence charSequence) {
        TextView textView = findView(i);
        if (textView != null) {
            textView.setText(charSequence);
        }

    }

    public void setImage(int i, String path) {
        ImageView imageView = findView(i);
        if (imageView != null) {
            Glide.with(mContext).load(path).into(imageView);
        }

    }

    public void setImage(int i, @DrawableRes int path) {
        ImageView imageView = findView(i);
        if (imageView != null) {
            Glide.with(mContext).load(path).into(imageView);
        }

    }

    public void setClickListener(int i, View.OnClickListener onClickListener) {
        View view = findView(i);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }

    }

    public void setVisiable(int i, boolean isvisiable) {
        View view = findView(i);
        if (view != null) {
            view.setVisibility(isvisiable ? View.VISIBLE : View.GONE);
        }

    }

    public <T extends View> T findView(@IdRes int id) {

        WeakReference<View> viewWeakReference = mViews.get(id);
        if (viewWeakReference != null) {
            View view = viewWeakReference.get();
            return (T) view;
        }

        View viewById = getContentView().findViewById(id);
        if (viewById != null) {
            mViews.put(id, new WeakReference<View>(viewById));
        }

        return (T) viewById;
    }


    public View getContentView() {

        return mContentView;
    }


}
