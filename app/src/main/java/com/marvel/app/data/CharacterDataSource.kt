package com.marvel.app.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.marvel.app.data.models.ApiResponse
import com.marvel.app.data.models.Pagination
import com.marvel.app.data.models.Character
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDataSource(private val marvelApi: MarvelApi, private val characterName: String?) : PageKeyedDataSource<Int, Character>() {

    var error = MutableLiveData<Throwable>()
    var loading = MutableLiveData<Boolean>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>) {
        loading.postValue(true)
        val data = marvelApi.getCharacters(characterName?.ifEmpty { null }, params.requestedLoadSize, 0)
        data.enqueue(object : Callback<ApiResponse<Pagination<Character>>> {
            override fun onResponse(call: Call<ApiResponse<Pagination<Character>>>, response: Response<ApiResponse<Pagination<Character>>>) {
                loading.postValue(false)
                val responseData = response.body()?.data ?: return
                val characters = responseData.results.toMutableList()
                callback.onResult(characters, 0, responseData.count)
            }

            override fun onFailure(call: Call<ApiResponse<Pagination<Character>>>, t: Throwable) {
                loading.postValue(false)
                error.postValue(t)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        loading.postValue(true)
        val data = marvelApi.getCharacters(characterName?.ifEmpty { null }, params.requestedLoadSize, params.key)
        data.enqueue(object : Callback<ApiResponse<Pagination<Character>>> {
            override fun onResponse(call: Call<ApiResponse<Pagination<Character>>>, response: Response<ApiResponse<Pagination<Character>>>) {
                loading.postValue(false)
                val responseData = response.body()?.data ?: return
                val characters = responseData.results.toMutableList()
                callback.onResult(characters, responseData.offset + responseData.count)
            }

            override fun onFailure(call: Call<ApiResponse<Pagination<Character>>>, t: Throwable) {
                loading.postValue(false)
                error.postValue(t)
            }
        })
    }

}