package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.alpinebuddy.R
import com.example.alpinebuddy.data.GorovjeRead

class GorovjeAdapter(
    private var gorovja: List<GorovjeRead>,
    private val onItemClicked: (GorovjeRead) -> Unit
) : RecyclerView.Adapter<GorovjeAdapter.GorovjeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GorovjeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gorovje, parent, false)
        return GorovjeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GorovjeViewHolder, position: Int) {
        val gorovje = gorovja[position]
        holder.bind(gorovje)
        holder.itemView.setOnClickListener {
            onItemClicked(gorovje)
        }
    }

    override fun getItemCount(): Int = gorovja.size

    fun updateData(newGorovja: List<GorovjeRead>) {
        gorovja = newGorovja
        notifyDataSetChanged()
    }

    class GorovjeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nazivTextView: TextView = itemView.findViewById(R.id.tvNazivGorovja)
        private val imageView: ImageView = itemView.findViewById(R.id.ivGorovje)

        fun bind(gorovje: GorovjeRead) {
            nazivTextView.text = gorovje.naziv

            if (!gorovje.slikaUrl.isNullOrEmpty()) {
                imageView.load(gorovje.slikaUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background) // Placeholder, če se slika nalaga
                    error(R.drawable.ic_launcher_foreground) // Slika v primeru napake
                }
            } else {
                // Nastavi privzeto sliko, če URL-ja ni
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}
