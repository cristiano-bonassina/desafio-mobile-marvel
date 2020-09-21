package com.marvel.app.data

import com.marvel.app.extensions.toMD5
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MarvelApiInterceptor(private val apiPublicKey: String, private val apiPrivateKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val now = System.currentTimeMillis()
        val hash = "$now$apiPrivateKey$apiPublicKey".toMD5()
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url()
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("ts", now.toString())
            .addQueryParameter("apikey", apiPublicKey)
            .addQueryParameter("hash", hash)
            .build()
        val requestBuilder: Request.Builder = original.newBuilder().url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

}
