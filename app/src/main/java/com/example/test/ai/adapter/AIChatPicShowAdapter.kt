package com.genew.groupcontrol.ui.nuchat.adapter

import android.content.Context
import android.widget.ImageView
import com.example.test.R
import com.example.test.ai.bean.AiChatPicShowBean
import com.example.test.widget.BaseRecycleViewHolder
import com.example.test.widget.BaseRecyclerViewAdapter
import com.example.test.widget.DrawableHelper
import com.example.test.widget.dp2px
import com.genew.mpublic.helper.gone
import com.genew.mpublic.helper.initRoundRect
import com.genew.mpublic.helper.visible


class AIChatPicShowAdapter(
    private val mContext: Context
) :
    BaseRecyclerViewAdapter<AiChatPicShowBean, BaseRecycleViewHolder>(
        mContext,
        R.layout.item_aichat_pic_show
    ) {

    private var clickCloseCallback: ((Int) -> Unit)? = null
    private var clickAddCallback: (() -> Unit)? = null

    fun setClickCloseCallback(callback: (Int) -> Unit) {
        this.clickCloseCallback = callback
    }
    fun setClickAddCallback(callback: () -> Unit) {
        this.clickAddCallback = callback
    }

    override fun onBindData(holder: BaseRecycleViewHolder, bean: AiChatPicShowBean, position: Int) {
        if (mList.isNotEmpty()) {
            val close = holder.getView<ImageView>(R.id.iv_pic_close)
            val add = holder.getView<ImageView>(R.id.iv_pic_add)
            val bg = holder.getView<ImageView>(R.id.iv_pic_show)
            bg.initRoundRect(10F.dp2px(mContext))
            DrawableHelper.showImgWithGlideNoCache(bg,mList[position].picUrl,0);
            close.setOnClickListener { clickCloseCallback?.invoke(position) }
            add.setOnClickListener { clickAddCallback?.invoke() }

            if (mList[position].viewType == AiChatPicShowBean.TYPE_ADD) {
                close.gone()
                add.visible()
            } else {
                close.visible()
                add.gone()
            }
        }
    }
}