package com.octagon_technologies.sky_weather.lazy

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.octagon_technologies.sky_weather.ads.AdHelper


// TODO - move to Utils, and rename to maybe AdUtil
fun Fragment.adHelpers() = lazy {
    AdHelper(activity as AppCompatActivity)
}