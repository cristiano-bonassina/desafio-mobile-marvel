package com.marvel.app.extensions

import retrofit2.Retrofit
import kotlin.reflect.KClass

fun <T : Any> Retrofit.create(service: KClass<T>): T {
    return this.create(service.java)
}