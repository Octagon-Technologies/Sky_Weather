package com.octagon_technologies.sky_weather.lazy

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.octagon_technologies.sky_weather.ads.AdHelper

fun Fragment.adHelpers() = lazy {
    AdHelper(activity as AppCompatActivity)
}