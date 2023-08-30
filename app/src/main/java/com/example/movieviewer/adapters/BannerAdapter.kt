package com.example.movieviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieviewer.databinding.BannerLayoutBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.models.Banner

/**
 * @author Marko Nikolic on 30.8.23.
 */
class BannerAdapter(
        private val itemList: List<Banner>,
        private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BannerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(private val binding: BannerLayoutBinding) : RecyclerView.ViewHolder(
            binding.root) {
        fun bind(banner: Banner) {
            binding.apply {
                Glide.with(root)
                    .load(banner.imageUrl)
                    .centerCrop()
                    .into(image)
                root.setOnClickListener { itemClickListener.onItemClick(banner.id) }
            }
        }
    }
}