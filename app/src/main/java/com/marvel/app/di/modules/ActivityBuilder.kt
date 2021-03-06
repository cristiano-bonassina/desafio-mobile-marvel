package com.marvel.app.di.modules

import com.marvel.app.ui.characters.CharactersActivity
import com.marvel.app.ui.comics.ComicsActivity
import com.marvel.app.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindComicActivity(): ComicsActivity

    @ContributesAndroidInjector
    abstract fun bindLauncherActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): CharactersActivity

}