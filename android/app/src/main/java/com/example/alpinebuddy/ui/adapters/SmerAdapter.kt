package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alpinebuddy.databinding.ItemSmerBinding

// Podatkovni razred, ki združuje podatke za prikaz
data class SmerInfo(
    val smerId: Int,
    val ime: String?,
    val dolzina: Int?,
    val tezavnost: String?,
    val stil: String?
)

class SmerAdapter(
    private var smeri: List<SmerInfo>,
    private val onItemClicked: (SmerInfo) -> Unit
) : RecyclerView.Adapter<SmerAdapter.SmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmerViewHolder {
        val binding = ItemSmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmerViewHolder, position: Int) {
        val smer = smeri[position]
        holder.bind(smer)
        holder.itemView.setOnClickListener { onItemClicked(smer) }
    }

    override fun getItemCount(): Int = smeri.size

    fun updateData(newSmeri: List<SmerInfo>) {
        smeri = newSmeri
        notifyDataSetChanged()
    }

    class SmerViewHolder(private val binding: ItemSmerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(smer: SmerInfo) {
            binding.tvSmerIme.text = smer.ime ?: "Neznano ime"
            binding.tvSmerDolzina.text = "Dolžina: ${smer.dolzina?.toString() ?: "N/A"} m"
            binding.tvSmerTezavnost.text = "Težavnost: ${smer.tezavnost ?: "N/A"}"
            binding.tvSmerStil.text = "Stil: ${smer.stil ?: "N/A"}"
        }
    }
}
