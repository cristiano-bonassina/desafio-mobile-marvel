package com.marvel.app.ui.comics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.marvel.app.data.ComicDataSource
import com.marvel.app.data.MarvelApi
import com.marvel.app.data.models.Comic

class ComicsViewModel(private val marvelApi: MarvelApi) : ViewModel() {

    val comics = MutableLiveData<PagedList<Comic>>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    private val config: PagedList.Config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(10)
        .setPageSize(20)
        .setPrefetchDistance(4)
        .build()

    fun searchComics(characterId: Int, title: String?) {
        val dataSource = ComicDataSource(marvelApi, characterId, title)
        dataSource.error.observeForever { error -> this.error.postValue(error) }
        dataSource.loading.observeForever { loading -> this.loading.postValue(loading) }
        val dataSourceFactory = object : DataSource.Factory<Int, Comic>() {
            override fun create(): DataSource<Int, Comic> {
                return dataSource
            }
        }
        val page = LivePagedListBuilder(dataSourceFactory, config).build()
        page.observeForever { comics -> this.comics.postValue(comics) }
    }

}