package com.example.countandcatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.R
import com.example.countandcatch.data.ImageItem
import com.example.countandcatch.data.PairData

class ImgAdapter : RecyclerView.Adapter<ImgAdapter.VH>() {

    private val items = mutableListOf<ImageItem>()
    private var itemSizePx = 0

    fun submit(list: List<ImageItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    fun setItemSize(px: Int) {
        itemSizePx = px; notifyDataSetChanged()
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val img = v.findViewById<ImageView>(R.id.imgItemCount)
        private val layout: ViewGroup = itemView.findViewById(R.id.imgItemLayout)

        fun bind(it: ImageItem) {
            img.setImageResource(it.drawableId)
            layout.layoutParams = layout.layoutParams.apply {
                width = itemSizePx
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.img_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
