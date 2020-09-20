package com.marvel.app.ui.characters

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.marvel.app.R
import com.marvel.app.data.Character
import com.marvel.app.ui.comics.ComicsActivity
import com.marvel.app.ui.widget.GridSpacingItemDecoration
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_characters.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class CharactersActivity : DaggerAppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var charactersViewModel: CharactersViewModel

    private var backPressedAt = 0L

    private var searchCharactersJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)
        setSupportActionBar(findViewById(R.id.toolbar))

        val columns = resources.getInteger(R.integer.grid_columns)
        val layoutManager = GridLayoutManager(this, columns)
        charactersRecyclerView.layoutManager = layoutManager

        val itemSpace = resources.getInteger(R.integer.grid_item_space)
        val itemDecoration = GridSpacingItemDecoration(this, columns, itemSpace)
        charactersRecyclerView.addItemDecoration(itemDecoration)

        charactersViewModel.error.observe(this, {
            Toast.makeText(this, R.string.error_fetching_characters, Toast.LENGTH_LONG).show()
        })

        charactersViewModel.loading.observe(this, { loading ->
            progressBar.visibility = if (loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        charactersViewModel.characters.observe(this, { characters ->
            val characterAdapter = CharacterAdapter(this@CharactersActivity)
            characterAdapter.onItemClick = { view, character -> showCharacterComics(view, character) }
            characterAdapter.submitList(characters)
            charactersRecyclerView.adapter = characterAdapter
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.characters, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        val elapsed = now - backPressedAt
        val interval = 3000L
        if (elapsed > interval) {
            backPressedAt = now
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchCharactersJob?.cancel()
        searchCharactersJob = lifecycleScope.launch {
            delay(500)
            searchCharacters(newText)
        }
        return false
    }

    private fun searchCharacters(name: String?) {
        charactersViewModel.searchCharacters(name)
    }

    private fun showCharacterComics(view: View, character: Character) {
        val intent = Intent(this, ComicsActivity::class.java)
        intent.putExtra(ComicsActivity.CHARACTER, character)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "characterThumbnail")
        startActivity(intent, options.toBundle())
    }

}