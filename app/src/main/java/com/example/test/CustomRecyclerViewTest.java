package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerViewTest extends RecyclerView {

    private float mLastY = 0f;
    private boolean isToBottom = false;
    private boolean isToTop = true;


    public CustomRecyclerViewTest(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerViewTest(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerViewTest(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下位置
                mLastY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                checkPosition(event.getY());
                if (isToBottom || isToTop) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(event);
    }

    private void checkPosition(float nowY) {
        LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
        isToTop = false;
        isToBottom = false;
        int firstVisiblePosition = manager.findFirstCompletelyVisibleItemPosition();
        int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
        if (manager.getChildCount() > 0) {
            if (lastVisiblePosition == manager.getItemCount() - 1) {
                if (canScrollVertically(-1) && nowY < mLastY) {
                    isToBottom = true;
                }
            } else if (firstVisiblePosition == 0) {
                if (canScrollVertically(1) && nowY > mLastY) {
                    isToTop = true;
                }
            }

        }
    }
}
