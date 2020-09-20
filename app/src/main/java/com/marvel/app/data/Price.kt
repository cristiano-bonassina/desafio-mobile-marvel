package com.marvel.app.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Price(
    val price: Float,
    val type: String
) : Parcelable