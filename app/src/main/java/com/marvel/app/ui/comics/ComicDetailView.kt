package com.marvel.app.ui.comics

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.marvel.app.data.models.Comic
import com.marvel.app.databinding.ViewComicDetailBindingImpl

class ComicDetailView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val inflater = LayoutInflater.from(context)
    private val binding = ViewComicDetailBindingImpl.inflate(inflater, this, true)

    var comic: Comic? = null
        set(value) {
            field = value
            binding.viewModel = comic
        }

}