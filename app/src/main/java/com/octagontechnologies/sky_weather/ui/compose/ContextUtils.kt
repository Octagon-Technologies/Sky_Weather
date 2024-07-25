package com.octagontechnologies.sky_weather.ui.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is  ContextWrapper -> baseContext.getActivity()
    else -> null
}