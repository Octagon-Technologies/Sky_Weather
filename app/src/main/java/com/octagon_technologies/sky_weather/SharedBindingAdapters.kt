package com.octagon_technologies.sky_weather

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.repository.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.repository.network.single_forecast.WeatherCode
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("getWeatherIconFrom", "addObservationTime")
fun ImageView.getWeatherIconFrom(weatherCode: WeatherCode?, observationTime: ObservationTime?) {
    val hourIn24HourSystem = SimpleDateFormat(
        "HH",
        Locale.ENGLISH
    ).format(DateTime(observationTime?.value).toDate().time).toInt()
    val drawable = weatherCode.getDrawableFromWeatherCode(hourIn24HourSystem)

    contentDescription = weatherCode?.value?.capitalizeWordsWithUnderscore()

    Glide.with(context)
        .load(drawable)
        .centerCrop()
        .into(this)
}

fun WeatherCode?.getDrawableFromWeatherCode(hourIn24HourSystem: Int) = this?.value?.let {
    when {
        it.contains("rain") -> R.drawable.raining_clouds
        it.contains("cloudy") -> if (hourIn24HourSystem in 4..18) R.drawable.cloudy else R.drawable.cloudy_night
        it.contains("clear") -> if (hourIn24HourSystem in 4..18) R.drawable.sun else R.drawable.moon
        it.contains("storm") -> if (hourIn24HourSystem in 4..18) R.drawable.storm else R.drawable.stormy_night
        else -> R.drawable.cloudy_windy
    }
} ?: -1

@BindingAdapter("capitalizeWordsWithUnderscore")
fun TextView.capitalizeWordsWithUnderscore(weatherCode: WeatherCode?) {
    text = weatherCode?.value?.capitalizeWordsWithUnderscore()
}

@BindingAdapter("changeLunarTime", "addTimeFormatToLunarTime")
fun TextView.changeLunarTime(lunarTime: String?, timeFormat: TimeFormat?) {
    text = lunarTime?.changeLunarTime(timeFormat) ?: "--"
}

fun String.changeLunarTime(timeFormat: TimeFormat?): String {
    val hour_in_24_system = split(":")[0].toInt()
    val minutes = split(":")[1]

    return if (timeFormat == TimeFormat.HALF_DAY) {
        if (hour_in_24_system <= 11)
            "$this am"
        else
            "${hour_in_24_system - 12}:$minutes pm"
    }
    else this
}

@BindingAdapter("changeWeatherIconTint", "isDayForWeatherIcon")
fun ImageView.changeWeatherIconTint(theme: Theme?, isDayForWeatherIcon: Boolean) {
    when (isDayForWeatherIcon) {
        true -> {
            setImageResource(R.drawable.sun)
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
    text = (singleForecast?.weatherCode?.value ?: "--").capitalizeWordsWithUnderscore()
}

@BindingAdapter("getEachHourlyTime", "addTimeFormat", requireAll = false)
fun View.getWeatherImageBasedOnTime(observationTime: ObservationTime?, timeFormat: TimeFormat?) {
    observationTime?.value?.let {
        val date = DateTime(it).toDate()
        val hourIn24HourSystem = SimpleDateFormat("HH", Locale.getDefault()).format(date).toInt()
        Timber.d("hourIn24HourSystem is $hourIn24HourSystem")

        if (this is ImageView) {
            val drawable = when (hourIn24HourSystem) {
                in 6..19 -> R.drawable.sun
                else -> R.drawable.moon
            }
            Glide.with(this)
                .load(drawable)
                .into(this)
        } else {
            this as TextView
            text = timeFormat.getAmOrPmBasedOnTime(hourIn24HourSystem, date)
        }
    }
}