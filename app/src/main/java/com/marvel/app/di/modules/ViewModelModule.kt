package com.marvel.app.di.modules

import com.marvel.app.data.MarvelApi
import com.marvel.app.ui.characters.CharactersViewModel
import com.marvel.app.ui.comics.ComicsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideCharacterViewModel(marvelApi: MarvelApi): CharactersViewModel {
        return CharactersViewModel(marvelApi)
    }

    @Provides
    @Singleton
    fun provideComicViewModel(marvelApi: MarvelApi): ComicsViewModel {
        return ComicsViewModel(marvelApi)
    }

}
