package com.marvel.app.data

import com.marvel.app.data.models.ApiResponse
import com.marvel.app.data.models.Pagination
import com.marvel.app.data.models.Character
import com.marvel.app.data.models.Comic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    fun getCharacters(@Query("nameStartsWith") nameStartsWith: String?, @Query("limit") limit: Int, @Query("offset") offset: Int): Call<ApiResponse<Pagination<Character>>>

    @GET("v1/public/characters/{characterId}/comics")
    fun getComics(
        @Path("characterId") characterId: Int,
        @Query("titleStartsWith") titleStartsWith: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ApiResponse<Pagination<Comic>>>

}