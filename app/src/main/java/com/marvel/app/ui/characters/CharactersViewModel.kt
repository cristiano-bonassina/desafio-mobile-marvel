package com.marvel.app.ui.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.marvel.app.data.CharacterDataSource
import com.marvel.app.data.MarvelApi
import com.marvel.app.data.models.Character

class CharactersViewModel(private val marvelApi: MarvelApi) : ViewModel() {

    val characters = MutableLiveData<PagedList<Character>>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    private val config: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(20)
        .setPrefetchDistance(4)
        .build()

    fun searchCharacters(name: String?) {
        val dataSource = CharacterDataSource(marvelApi, name)
        dataSource.error.observeForever { error -> this.error.postValue(error) }
        dataSource.loading.observeForever { loading -> this.loading.postValue(loading) }
        val dataSourceFactory = object : DataSource.Factory<Int, Character>() {
            override fun create(): DataSource<Int, Character> {
                return dataSource
            }
        }
        val page = LivePagedListBuilder(dataSourceFactory, config).build()
        page.observeForever { characters -> this.characters.postValue(characters) }
    }

}