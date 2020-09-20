package com.marvel.app.data

import androidx.recyclerview.widget.DiffUtil

data class Comic(
    val description: String,
    val id: Int,
    val images: List<Image>,
    val pageCount: Int,
    val prices: Collection<Price>,
    val thumbnail: Image,
    val title: String
) {

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<Comic> = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}