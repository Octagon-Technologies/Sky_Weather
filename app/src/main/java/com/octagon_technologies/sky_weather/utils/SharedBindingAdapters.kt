package com.octagon_technologies.sky_weather.utils

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.WeatherCode
import com.octagon_technologies.sky_weather.domain.getWeatherIcon
import com.octagon_technologies.sky_weather.domain.getWeatherTitle
import org.joda.time.Instant
import java.util.*


fun ImageView.loadWeatherIcon(isDay: Boolean, weatherCode: WeatherCode?) {
    contentDescription = weatherCode.getWeatherTitle()

    Glide.with(context)
        .load(weatherCode.getWeatherIcon(isDay))
        .centerCrop()
        .into(this)
}

fun ImageView.loadWeatherIcon(timeInMillis: Long?, weatherCode: WeatherCode?) {
    val hourOfDay = timeInMillis?.let { Instant.ofEpochMilli(it).toDateTime().hourOfDay } ?: 12
    loadWeatherIcon(hourOfDay in 6..19, weatherCode)
}

@BindingAdapter("changeLunarTime", "addTimeFormatToLunarTime")
fun TextView.changeLunarTime(lunarTime: String?, timeFormat: TimeFormat?) {
    text = lunarTime?.changeLunarTime(timeFormat) ?: "--"
}

fun String.changeLunarTime(timeFormat: TimeFormat?): String {
    val hourIn24System = split(":")[0].toInt()
    val minutes = split(":")[1]

    /*
     I realized that moonSet is always 8pm and moonRise is 6am... which kinda doesn't make sense so I've interchanged
     the am/pm logic coz typically, the moon should rise at night and set in the morning

     BUT, we stick to factual data... Plus I've done a bit of research; It works well with Reno, Nevada and
     kina Europe
     */
    return if (timeFormat == TimeFormat.HALF_DAY) {
        if (hourIn24System <= 11)
            "$hourIn24System:$minutes am"
        else
            "${hourIn24System - 12}:$minutes pm"
    } else this
}

@BindingAdapter("changeIconTintFromTint")
fun ImageView.changeIconTintFromTint(theme: Theme?) {
    doImageTintChange(if (theme == Theme.LIGHT) R.color.light_black else android.R.color.white)
}

fun ImageView.doImageTintChange(@ColorRes colorInt: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(
            ContextCompat.getColor(
                context,
                colorInt
            )
        )
    )
}

