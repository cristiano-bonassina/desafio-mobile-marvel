package com.marvel.app.ui.characters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marvel.app.R
import com.marvel.app.data.Character
import com.marvel.app.extensions.getUrl

class CharacterAdapter(private val context: Context) : PagedListAdapter<Character, CharacterAdapter.ViewHolder>(Character.CALLBACK) {

    var onItemClick: ((View, Character) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.character_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context
        private val characterName = itemView.findViewById<TextView>(R.id.characterName)
        private val characterThumbnail = itemView.findViewById<ImageView>(R.id.characterThumbnail)
        private var character: Character? = null

        init {
            itemView.setOnClickListener {
                character?.let { onItemClick?.invoke(itemView, it) }
            }
        }

        fun bind(item: Character) {
            this.character = item
            characterName.text = item.name
            val thumbnailUrl = item.thumbnail.getUrl()
            Glide.with(context).load(thumbnailUrl).into(characterThumbnail)
        }

    }

}