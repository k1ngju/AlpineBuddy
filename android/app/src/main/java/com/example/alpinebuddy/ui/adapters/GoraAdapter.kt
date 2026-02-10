package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.alpinebuddy.R
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.GoraRead

class GoraAdapter(
    private val onItemClicked: (GoraRead) -> Unit
) : ListAdapter<GoraRead, GoraAdapter.GoraViewHolder>(GoraDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gora, parent, false)
        return GoraViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoraViewHolder, position: Int) {
        val gora = getItem(position)
        holder.bind(gora)
        holder.itemView.setOnClickListener {
            onItemClicked(gora)
        }
    }

    class GoraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nazivTextView: TextView = itemView.findViewById(R.id.tvNazivGore)
        private val imageView: ImageView = itemView.findViewById(R.id.ivGora)

        fun bind(gora: GoraRead) {
            nazivTextView.text = gora.naziv

            if (!gora.slikaUrl.isNullOrEmpty()) {
                val fullUrl = ApiClient.BASE_URL + gora.slikaUrl
                imageView.load(fullUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_foreground)
                }
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}

class GoraDiffCallback : DiffUtil.ItemCallback<GoraRead>() {
    override fun areItemsTheSame(oldItem: GoraRead, newItem: GoraRead): Boolean {
        return oldItem.goraId == newItem.goraId
    }

    override fun areContentsTheSame(oldItem: GoraRead, newItem: GoraRead): Boolean {
        return oldItem == newItem
    }
}
