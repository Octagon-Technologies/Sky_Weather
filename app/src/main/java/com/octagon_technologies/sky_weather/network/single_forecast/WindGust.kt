package com.octagon_technologies.sky_weather.network.single_forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WindGust(
    val units: String?,
    val value: Double?
) : Parcelable