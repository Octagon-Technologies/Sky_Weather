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
import java.util.*

@BindingAdapter("getWeatherIconFrom")
fun ImageView.getWeatherIconFrom(weatherCode: WeatherCode?) {
    contentDescription = weatherCode?.getWeatherTitle()

    // TODO: Change this
    Glide.with(context)
        .load(weatherCode?.getWeatherIcon(12))
        .centerCrop()
        .into(this)
}

//fun WeatherCode?.getDrawableFromWeatherCode(hourIn24HourSystem: Int) = this?.value?.let {
//    when {
//        it.contains("rain") -> R.drawable.raining_clouds
//        it.contains("cloudy") -> if (hourIn24HourSystem in 4..18) R.drawable.cloudy else R.drawable.cloudy_night
//        it.contains("clear") -> if (hourIn24HourSystem in 4..18) R.drawable.sun else R.drawable.moon
//        it.contains("storm") -> if (hourIn24HourSystem in 4..18) R.drawable.storm else R.drawable.stormy_night
//        else -> R.drawable.cloudy_windy
//    }
//} ?: -1

@BindingAdapter("changeLunarTime", "addTimeFormatToLunarTime")
fun TextView.changeLunarTime(lunarTime: String?, timeFormat: TimeFormat?) {
    text = lunarTime?.changeLunarTime(timeFormat) ?: "--"
}

fun String.changeLunarTime(timeFormat: TimeFormat?): String {
    val hourIn24System = split(":")[0].toInt()
    val minutes = split(":")[1]

    return if (timeFormat == TimeFormat.HALF_DAY) {
        if (hourIn24System <= 11)
            "$hourIn24System:$minutes am"
        else
            "${hourIn24System - 12}:$minutes pm"
    } else this
}

@BindingAdapter("changeWeatherIconTint", "isDayForWeatherIcon")
fun ImageView.changeWeatherIconTint(theme: Theme?, isDayForWeatherIcon: Boolean) {
    when (isDayForWeatherIcon) {
        true -> {
            setImageResource(R.drawable.yellow_sun)
            doImageTintChange(R.color.dark_orange)
        }

        false -> {
            setImageResource(R.drawable.moon)
            doImageTintChange(if (theme == Theme.LIGHT) R.color.color_black else android.R.color.white)
        }
    }
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

@BindingAdapter("doTextViewThemeChange", "addDarkColor", "isSelected", requireAll = false)
fun TextView.doTextViewThemeChange(
    theme: Theme?,
    darkColor: Int? = android.R.color.black,
    isSelected: Boolean?
) {

    val contextColorBlack = ContextCompat.getColor(context, R.color.color_black)
    val contextLightGrey = ContextCompat.getColor(context, R.color.light_grey)
    val contextAndroidWhite = ContextCompat.getColor(context, android.R.color.white)

    if (isSelected == true) {
        setTextColor(
            ContextCompat.getColor(
                context,
                if (darkColor == contextColorBlack) R.color.light_grey else android.R.color.white
            )
        )
        return
    }

    when (theme) {
        Theme.LIGHT -> darkColor
        else -> if (darkColor == contextColorBlack) contextLightGrey else contextAndroidWhite
    }?.let {
        setTextColor(it)
    }

}

@BindingAdapter("getWeatherStatus")
fun TextView.getWeatherStatus(singleForecast: SingleForecast?) {
    text = singleForecast?.weatherCode?.getWeatherTitle() ?: "--"
}