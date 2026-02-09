package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.alpinebuddy.R
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.GoraRead

class GoraAdapter(
    private var gore: List<GoraRead>,
    private val onItemClicked: (GoraRead) -> Unit
) : RecyclerView.Adapter<GoraAdapter.GoraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gora, parent, false)
        return GoraViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoraViewHolder, position: Int) {
        val gora = gore[position]
        holder.bind(gora)
        holder.itemView.setOnClickListener {
            onItemClicked(gora)
        }
    }

    override fun getItemCount(): Int = gore.size

    fun updateData(newGore: List<GoraRead>) {
        gore = newGore
        notifyDataSetChanged()
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
