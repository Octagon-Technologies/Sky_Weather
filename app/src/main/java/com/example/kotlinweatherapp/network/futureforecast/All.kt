package com.example.kotlinweatherapp.network.futureforecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class All(
    val clouds: Clouds,
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val rain: Rain?,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
): Parcelable