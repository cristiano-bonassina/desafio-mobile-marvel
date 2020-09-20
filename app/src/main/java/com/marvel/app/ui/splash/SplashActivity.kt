package com.marvel.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.marvel.app.R
import com.marvel.app.ui.characters.CharactersActivity
import com.marvel.app.ui.characters.CharactersViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var charactersViewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        charactersViewModel.error.observe(this, {
            Toast.makeText(this@SplashActivity, R.string.startup_error, Toast.LENGTH_LONG).show()
            finish()
        })
        charactersViewModel.loading.observe(this, { loading ->
            if (!loading) {
                startMainActivity()
            }
        })
        charactersViewModel.searchCharacters(null)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, CharactersActivity::class.java))
        finish()
    }

}

