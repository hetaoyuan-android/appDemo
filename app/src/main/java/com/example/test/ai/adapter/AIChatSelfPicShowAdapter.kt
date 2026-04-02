package com.example.test.ai.adapter

import android.content.Context
import android.widget.ImageView
import com.example.test.R
import com.example.test.widget.BaseRecycleViewHolder
import com.example.test.widget.BaseRecyclerViewAdapter
import com.example.test.widget.DrawableHelper

import com.example.test.widget.dp2px
import com.genew.mpublic.helper.initRoundRect

class AIChatSelfPicShowAdapter(
    private val mContext: Context
) :
    BaseRecyclerViewAdapter<String, BaseRecycleViewHolder>(
        mContext,
        R.layout.item_aichat_self_pic_show
    ) {

    override fun onBindData(holder: BaseRecycleViewHolder, bean: String, position: Int) {
        if (mList.isNotEmpty()) {
            val bg = holder.getView<ImageView>(R.id.iv_pic_show)
            bg.initRoundRect(10F.dp2px(mContext))
            DrawableHelper.showImgWithGlideNoCache(bg, mList[position], 0);
        }
    }
}