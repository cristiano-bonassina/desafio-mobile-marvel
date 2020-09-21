package com.marvel.app.di.modules

import android.content.Context
import com.marvel.app.R
import com.marvel.app.data.MarvelApi
import com.marvel.app.data.MarvelApiInterceptor
import com.marvel.app.extensions.create
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @Named("apiUrl")
    fun provideApiUrl(context: Context): String {
        return context.getString(R.string.marvel_api_url)
    }

    @Provides
    @Singleton
    @Named("apiPrivateKey")
    fun provideApiPrivateKey(context: Context): String {
        return context.getString(R.string.marvel_api_private_key)
    }

    @Provides
    @Singleton
    @Named("apiPublicKey")
    fun provideApiPublicKey(context: Context): String {
        return context.getString(R.string.marvel_api_public_key)
    }

    @Provides
    @Singleton
    fun provideMarvelApiInterceptor(@Named("apiPublicKey") apiPublicKey: String, @Named("apiPrivateKey") apiPrivateKey: String): MarvelApiInterceptor {
        return MarvelApiInterceptor(apiPublicKey, apiPrivateKey)
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: MarvelApiInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60 * 2, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("apiUrl") apiUrl: String, httpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideMarvelApi(retrofit: Retrofit): MarvelApi {
        return retrofit.create(MarvelApi::class)
    }

}