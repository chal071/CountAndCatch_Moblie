package com.example.countandcatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.R
import com.example.countandcatch.data.Partida

class RankingAdapter(
    private val items: List<Partida>
) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tvRankNumber)
        val tvNombre: TextView = itemView.findViewById(R.id.tvPlayerName)
        val tvInfo: TextView = itemView.findViewById(R.id.tvPlayerInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val p = items[position]
        holder.tvNumber.text = (position + 1).toString()
        holder.tvNombre.text = p.nombre
        holder.tvInfo.text = "Tiempo: ${p.tiempo_partida}s  ·  Errores: ${p.errores}"
    }

    override fun getItemCount(): Int = items.size
}