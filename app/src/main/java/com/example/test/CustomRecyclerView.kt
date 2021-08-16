package com.example.test

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var mLastY = 0f
    private var isToBottom = false
    private var isToTop = true
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = event.y
                parent.requestDisallowInterceptTouchEvent (true)
            }
            MotionEvent.ACTION_MOVE -> {
                checkPosition(event.y)
                if (isToBottom || isToTop) {
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                mLastY = event.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )
        }
        return super.dispatchTouchEvent(event)
    }

    private fun checkPosition(nowY: Float) {
        val manager =
            layoutManager as LinearLayoutManager
            isToTop = false
            isToBottom = false
        val firstVisiblePosition =
            manager.findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition =
            manager.findLastCompletelyVisibleItemPosition()
        if (layoutManager!!.childCount > 0) {
            if (lastVisiblePosition == manager.itemCount - 1) {
                if (canScrollVertically(-1) && nowY < mLastY) {
                    isToBottom = true
                }
            } else if (firstVisiblePosition == 0) {
                if (canScrollVertically(1) && nowY > mLastY) {
                    isToTop = true
                }
            }
        }
    }
}