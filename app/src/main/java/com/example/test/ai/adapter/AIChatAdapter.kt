package com.example.test.ai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.ai.bean.DataText
import com.example.test.skin.SkinManager
import com.example.test.skin.ThemeConstant

import com.genew.mpublic.helper.copyToClipboard
import com.genew.mpublic.helper.gone
import com.genew.mpublic.helper.visible


class AIChatAdapter(private val data: List<DataText>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemChildViewClick : ((position : Int) -> Unit)? = null

    fun setItemChildViewClick(click : ((position : Int) -> Unit)){
        this.itemChildViewClick = click
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DataText.TYPE_SELF -> {
                AIChatSelfViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_aichat_self_enter, parent, false
                    )
                )
            }

            DataText.TYPE_SERVICE -> {
                AIChatServiceViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_aichat_service_retur, parent, false
                    )
                )
            }


            DataText.TYPE_SELF_PIC -> {
                AIChatSelfPicViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_aichat_self_enter_pic, parent, false
                    )
                )
            }


            else -> {
                AIChatSelfViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_aichat_self_enter, parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].msgTypeLocal
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]
        when (holder) {
            is AIChatSelfViewHolder -> {
                holder.llTextChat.setBackgroundResource(R.drawable.chatto_bg)
                holder.tvText.setTextColor(holder.tvText.context.getColor(R.color.white))
                holder.tvText.text = model.text
            }

            is AIChatSelfPicViewHolder -> {
                val picAdapter = AIChatSelfPicShowAdapter(holder.rvPic.context)
                holder.rvPic.adapter = picAdapter
                picAdapter.addData(model.picListLocal)
            }

            is AIChatServiceViewHolder -> {

                val mSkinSuffix = SkinManager.getInstance().mSkinSuffix
                if (mSkinSuffix == ThemeConstant.DARK) {
                    holder.llTextChat.setBackgroundResource(R.drawable.skin_chatfrom_bg_dark)
                    holder.tvText.setTextColor(holder.tvText.context.getColor(R.color.skin_text_item_title_color_dark))
                } else {
                    holder.llTextChat.setBackgroundResource(R.drawable.skin_chatfrom_bg)
                    holder.tvText.setTextColor(holder.tvText.context.getColor(R.color.skin_text_item_title_color))
                }
                holder.tvText.text = model.text

                if (DataText.START_WORK_FLOW_TYPE == model.workflowType) {
                    val text = holder.tvText.context.getString(R.string.ai_chat_waiting_text)
                    holder.tvText.append("$text")
                }

                holder.ivRefresh.setOnClickListener { itemChildViewClick?.invoke(position) }
                holder.ivCopy.setOnClickListener {
                    val str = model.text ?: ""
                    if (str.isNotEmpty()) str.copyToClipboard()
                }

                /// 最后一条数据可以从新请求服务器
                if (position == data.size -1 && DataText.END_WORK_FLOW_TYPE == model.workflowType) {
                    holder.ivRefresh.visible()
                } else {
                    holder.ivRefresh.gone()
                }
            }
        }
    }

    class AIChatSelfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llTextChat: View = itemView.findViewById(R.id.ll_text_chat)
        val tvText: TextView = itemView.findViewById(R.id.tv_self_enter)
    }

    class AIChatServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llTextChat: View = itemView.findViewById(R.id.ll_text_chat)
        val tvText: TextView = itemView.findViewById(R.id.tv_service_retur)
        val ivRefresh: ImageView = itemView.findViewById(R.id.iv_service_refresh)
        val ivCopy: ImageView = itemView.findViewById(R.id.iv_service_copy)
    }
    class AIChatSelfPicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPic: RecyclerView = itemView.findViewById(R.id.rv_pic_show)
    }
}