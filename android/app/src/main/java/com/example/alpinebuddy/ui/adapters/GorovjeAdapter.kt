package com.example.alpinebuddy.ui.adapters

import android.graphics.Color
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

    // Seznam barv za ozadja
    private val colors = listOf(
        Color.parseColor("#FF6D6D"), // Rdeča
        Color.parseColor("#6DA5FF"), // Modra
        Color.parseColor("#74D68A"), // Zelena
        Color.parseColor("#FFD966"), // Rumena
        Color.parseColor("#A56DFF"), // Vijolična
        Color.parseColor("#FF9E6D")  // Oranžna
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GorovjeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gorovje, parent, false)
        return GorovjeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GorovjeViewHolder, position: Int) {
        val gorovje = gorovja[position]
        // Določi barvo glede na pozicijo v seznamu
        val color = colors[position % colors.size]
        holder.bind(gorovje, color)
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

        fun bind(gorovje: GorovjeRead, color: Int) {
            nazivTextView.text = gorovje.naziv

            // Preverimo, če obstaja URL slike
            if (!gorovje.slikaUrl.isNullOrEmpty()) {
                imageView.load(gorovje.slikaUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(color) // Če nalaganje slike ne uspe, prikaži barvo
                }
            } else {
                // Če URL-ja ni, direktno nastavimo barvo
                imageView.setBackgroundColor(color)
                imageView.setImageDrawable(null) // Odstranimo morebitno prejšnjo sliko
            }
        }
    }
}
