package com.example.movieviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieviewer.databinding.ItemWithHeaderBinding
import com.example.movieviewer.interfaces.ItemClickListener
import com.example.movieviewer.models.ItemWithHeader

/**
 * @author Marko Nikolic on 10.1.24.
 */
class ItemWithHeaderAdapter(private val itemList: List<ItemWithHeader>,
        private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ItemWithHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWithHeaderBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(private val binding: ItemWithHeaderBinding) : RecyclerView.ViewHolder(
            binding.root) {
        fun bind(item: ItemWithHeader) {
            binding.apply {
                header.text = item.genreName
                recyclerView.adapter = MovieCardAdapter(item.movies,
                        object : ItemClickListener {
                            override fun onItemClick(id: String) {
                                itemClickListener.onItemClick(id)
                            }
                        })
            }
        }
    }
}