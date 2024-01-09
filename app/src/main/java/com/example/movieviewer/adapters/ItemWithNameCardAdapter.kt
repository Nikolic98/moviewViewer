package com.example.movieviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieviewer.databinding.ItemWithNameCardLayoutBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.models.Movie

/**
 * @author Marko Nikolic on 8.1.24.
 */
class ItemWithNameCardAdapter(private val itemList: List<Movie>,
        private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ItemWithNameCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWithNameCardLayoutBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(
            private val binding: ItemWithNameCardLayoutBinding) : RecyclerView.ViewHolder(
            binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                Glide.with(root)
                    .load(movie.imageUrl)
                    .centerCrop()
                    .into(image)
                name.text = movie.name
                root.setOnClickListener { itemClickListener.onItemClick(movie.id) }
            }
        }
    }
}