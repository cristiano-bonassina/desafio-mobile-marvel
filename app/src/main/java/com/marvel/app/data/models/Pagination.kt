package com.marvel.app.data.models

data class Pagination<T>(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<T>,
    val total: Int
)
