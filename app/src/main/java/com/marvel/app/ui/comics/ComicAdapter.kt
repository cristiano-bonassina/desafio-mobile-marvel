package com.marvel.app.ui.comics

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
import com.marvel.app.data.Comic
import com.marvel.app.extensions.getUrl

class ComicAdapter(private val context: Context) : PagedListAdapter<Comic, ComicAdapter.ViewHolder>(Comic.CALLBACK) {

    var onItemClick: ((Comic) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.comic_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context
        private val comicTitle = itemView.findViewById<TextView>(R.id.comicTitle)
        private val comicThumbnail = itemView.findViewById<ImageView>(R.id.comicThumbnail)
        private var comic: Comic? = null

        init {
            itemView.setOnClickListener {
                comic?.let { onItemClick?.invoke(it) }
            }
        }

        fun bind(item: Comic) {
            this.comic = item
            comicTitle.text = item.title
            val thumbnailUrl = item.thumbnail.getUrl()
            Glide.with(context).load(thumbnailUrl).into(comicThumbnail)
        }

    }

}