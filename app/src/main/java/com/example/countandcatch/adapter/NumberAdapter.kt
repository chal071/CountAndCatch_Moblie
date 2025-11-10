package com.example.countandcatch.adapter

import android.R.attr.value
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.R

class NumberAdapter : RecyclerView.Adapter<NumberAdapter.VH>() {

    private val items = mutableListOf<Int>()
    private var itemSizePx = 0
    private var onItemClick: ((Int) -> Unit)? = null


    fun submit(list: List<Int>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    fun setItemSize(px: Int) {
        itemSizePx = px; notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClick = listener
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tv: TextView = itemView.findViewById(R.id.numberItemCount)
        private val layout: ViewGroup = itemView.findViewById(R.id.numberItemLayout)

        fun bind(value: Int) {
            tv.text = value.toString()
            tv.layoutParams = tv.layoutParams.apply {
                width = itemSizePx / 2
            }
            layout.layoutParams = layout.layoutParams.apply {
                width = itemSizePx
            }
            tv.setOnClickListener { onItemClick?.invoke(value) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.number_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
