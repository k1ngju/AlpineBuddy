package com.example.alpinebuddy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alpinebuddy.R
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
    private val onItemClicked: (SmerInfo) -> Unit
) : ListAdapter<SmerInfo, SmerAdapter.SmerViewHolder>(SmerInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmerViewHolder {
        val binding = ItemSmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmerViewHolder, position: Int) {
        val smer = getItem(position)
        holder.bind(smer)
        holder.itemView.setOnClickListener { onItemClicked(smer) }
    }

    class SmerViewHolder(private val binding: ItemSmerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(smer: SmerInfo) {
            binding.tvSmerIme.text = smer.ime ?: context.getString(R.string.unknown_name)

            binding.tvSmerDolzina.text = smer.dolzina?.let {
                context.getString(R.string.length_label_format, it)
            } ?: context.getString(R.string.length_label_unavailable)

            binding.tvSmerTezavnost.text = smer.tezavnost?.let {
                context.getString(R.string.difficulty_label_format, it)
            } ?: context.getString(R.string.difficulty_label_unavailable)

            binding.tvSmerStil.text = smer.stil?.let {
                context.getString(R.string.style_label_format, it)
            } ?: context.getString(R.string.style_label_unavailable)
        }
    }
}

class SmerInfoDiffCallback : DiffUtil.ItemCallback<SmerInfo>() {
    override fun areItemsTheSame(oldItem: SmerInfo, newItem: SmerInfo): Boolean {
        return oldItem.smerId == newItem.smerId
    }

    override fun areContentsTheSame(oldItem: SmerInfo, newItem: SmerInfo): Boolean {
        return oldItem == newItem
    }
}
