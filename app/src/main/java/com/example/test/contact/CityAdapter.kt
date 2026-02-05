package com.example.test.contact

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R

class CityAdapter(private val list: List<IBaseItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) = list[position].itemType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) { // Letter Header
            val view = inflater.inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else { // City Item
            val view = inflater.inflate(R.layout.item_data, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is HeaderViewHolder && item is LetterHeader) {
            holder.tvLetter.text = item.letter
        } else if (holder is ItemViewHolder && item is CityItem) {
            holder.tvName.text = item.name
        }
    }

    override fun getItemCount() = list.size

    // 对应的 ViewHolder
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLetter: TextView = view.findViewById(R.id.tvHeaderLetter)
    }
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvDataName)
    }
}