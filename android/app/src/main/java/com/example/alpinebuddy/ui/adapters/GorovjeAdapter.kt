package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.alpinebuddy.R
import com.example.alpinebuddy.data.GorovjeRead

class GorovjeAdapter(
    private val onItemClicked: (GorovjeRead) -> Unit
) : ListAdapter<GorovjeRead, GorovjeAdapter.GorovjeViewHolder>(GorovjeDiffCallback()) {

    // Seznam barv za ozadja
    private val colors = listOf(
        "#FF6D6D".toColorInt(), // Rdeča
        "#6DA5FF".toColorInt(), // Modra
        "#74D68A".toColorInt(), // Zelena
        "#FFD966".toColorInt(), // Rumena
        "#A56DFF".toColorInt(), // Vijolična
        "#FF9E6D".toColorInt()  // Oranžna
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GorovjeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gorovje, parent, false)
        return GorovjeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GorovjeViewHolder, position: Int) {
        val gorovje = getItem(position)
        val color = colors[position % colors.size]
        holder.bind(gorovje, color)
        holder.itemView.setOnClickListener {
            onItemClicked(gorovje)
        }
    }

    class GorovjeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nazivTextView: TextView = itemView.findViewById(R.id.tvNazivGorovja)
        private val imageView: ImageView = itemView.findViewById(R.id.ivGorovje)

        fun bind(gorovje: GorovjeRead, color: Int) {
            nazivTextView.text = gorovje.naziv

            if (!gorovje.slikaUrl.isNullOrEmpty()) {
                imageView.load(gorovje.slikaUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(color)
                }
            } else {
                imageView.setBackgroundColor(color)
                imageView.setImageDrawable(null)
            }
        }
    }
}

class GorovjeDiffCallback : DiffUtil.ItemCallback<GorovjeRead>() {
    override fun areItemsTheSame(oldItem: GorovjeRead, newItem: GorovjeRead): Boolean {
        return oldItem.gorovjeId == newItem.gorovjeId
    }

    override fun areContentsTheSame(oldItem: GorovjeRead, newItem: GorovjeRead): Boolean {
        return oldItem == newItem
    }
}
