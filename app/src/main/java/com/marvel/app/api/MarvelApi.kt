package com.marvel.app.api

import com.marvel.app.api.data.ApiResponse
import com.marvel.app.api.data.Pagination
import com.marvel.app.data.Character
import com.marvel.app.data.Comic
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