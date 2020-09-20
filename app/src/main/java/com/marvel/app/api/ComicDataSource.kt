package com.marvel.app.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.marvel.app.api.data.ApiResponse
import com.marvel.app.api.data.Pagination
import com.marvel.app.data.Comic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComicDataSource(private val marvelApi: MarvelApi, private val characterId: Int, private val comicTitle: String?) : PageKeyedDataSource<Int, Comic>() {

    var error = MutableLiveData<Throwable>()
    var loading = MutableLiveData<Boolean>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Comic>) {
        loading.postValue(true)
        val data = marvelApi.getComics(characterId, comicTitle?.ifEmpty { null }, params.requestedLoadSize, 0)
        data.enqueue(object : Callback<ApiResponse<Pagination<Comic>>> {
            override fun onResponse(call: Call<ApiResponse<Pagination<Comic>>>, response: Response<ApiResponse<Pagination<Comic>>>) {
                loading.postValue(false)
                val responseData = response.body()?.data ?: return
                val characters = responseData.results.toMutableList()
                callback.onResult(characters, 0, responseData.count)
            }

            override fun onFailure(call: Call<ApiResponse<Pagination<Comic>>>, t: Throwable) {
                loading.postValue(false)
                error.postValue(t)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Comic>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Comic>) {
        loading.postValue(true)
        val data = marvelApi.getComics(characterId, comicTitle?.ifEmpty { null }, params.requestedLoadSize, params.key)
        data.enqueue(object : Callback<ApiResponse<Pagination<Comic>>> {
            override fun onResponse(call: Call<ApiResponse<Pagination<Comic>>>, response: Response<ApiResponse<Pagination<Comic>>>) {
                loading.postValue(false)
                val responseData = response.body()?.data ?: return
                val characters = responseData.results.toMutableList()
                callback.onResult(characters, responseData.offset + responseData.count)
            }

            override fun onFailure(call: Call<ApiResponse<Pagination<Comic>>>, t: Throwable) {
                loading.postValue(false)
                error.postValue(t)
            }
        })
    }

}