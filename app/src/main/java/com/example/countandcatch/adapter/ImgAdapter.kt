package com.example.countandcatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.R
import com.example.countandcatch.data.ImageItem

class ImgAdapter : RecyclerView.Adapter<ImgAdapter.VH>() {

    private val items = mutableListOf<ImageItem>()
    private var itemSizePx = 0
    private var onItemClick: ((ImageItem) -> Unit)? = null


    fun submit(list: List<ImageItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemSize(px: Int) {
        itemSizePx = px
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (ImageItem) -> Unit) {
        onItemClick = listener
    }

    fun removeByPairId(pairId: Int) {
        val idx = items.indexOfFirst { it.pairId == pairId }
        if (idx != -1) {
            items.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }

    fun isEmpty(): Boolean = items.isEmpty()

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val img = v.findViewById<ImageView>(R.id.imgItemCount)
        private val layout: ViewGroup = itemView.findViewById(R.id.imgItemLayout)

        fun bind(item: ImageItem) {
            img.setImageResource(item.drawableId)
            layout.layoutParams = layout.layoutParams.apply {
                width = itemSizePx
            }
            img.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.img_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun getCount(): Int = items.size

}
