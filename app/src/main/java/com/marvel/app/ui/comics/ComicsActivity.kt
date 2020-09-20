package com.marvel.app.ui.comics

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.marvel.app.R
import com.marvel.app.data.Character
import com.marvel.app.data.Comic
import com.marvel.app.extensions.getUrl
import com.marvel.app.ui.widget.GridSpacingItemDecoration
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_comics.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class ComicsActivity : DaggerAppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var comicsViewModel: ComicsViewModel

    lateinit var character: Character

    private var searchComicsJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val character = this.intent.getParcelableExtra<Character>(CHARACTER)
        if (character == null) {
            finish()
            return
        }

        this.character = character

        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = character.name.toUpperCase()

        val thumbnailUrl = character.thumbnail.getUrl()
        Glide.with(this).load(thumbnailUrl).into(characterThumbnail)

        val columns = resources.getInteger(R.integer.grid_columns)
        val layoutManager = GridLayoutManager(this, columns)
        comicsRecyclerView.layoutManager = layoutManager

        val itemSpace = resources.getInteger(R.integer.grid_item_space)
        val itemDecoration = GridSpacingItemDecoration(this, columns, itemSpace)
        comicsRecyclerView.addItemDecoration(itemDecoration)

        comicsViewModel.error.observe(this, {
            Toast.makeText(this, R.string.error_fetching_comics, Toast.LENGTH_LONG).show()
        })

        comicsViewModel.loading.observe(this, { loading ->
            progressBar.visibility = if (loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        comicsViewModel.comics.observe(this, { comics ->
            val comicAdapter = ComicAdapter(this@ComicsActivity)
            comicAdapter.onItemClick = { comic -> showComicDetail(comic) }
            comicAdapter.submitList(comics)
            comicsRecyclerView.adapter = comicAdapter
        })

        searchComics(null)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.characters, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchComicsJob?.cancel()
        searchComicsJob = lifecycleScope.launch {
            delay(500)
            searchComics(newText)
        }
        return false
    }

    private fun searchComics(title: String?) {
        comicsViewModel.searchComics(this.character.id, title)
    }

    private fun showComicDetail(comic: Comic) {
        val dialogContentView = View.inflate(this, R.layout.bottom_dialog_comics_detail, null)
        val comicDetailView = dialogContentView.findViewById<ComicDetailView>(R.id.comicDetailView)
        comicDetailView.comic = comic
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogContentView)
        dialog.show()
    }

    companion object {
        const val CHARACTER = "character"
    }

}