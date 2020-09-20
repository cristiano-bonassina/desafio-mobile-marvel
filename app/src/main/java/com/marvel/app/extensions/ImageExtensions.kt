package com.marvel.app.extensions

import android.net.Uri
import com.marvel.app.data.Image

fun Image.getUrl(): String {
    val thumbnailUrl = "${this.path}/portrait_incredible.${this.extension}"
    return Uri.parse(thumbnailUrl).buildUpon().scheme("https").toString()
}
