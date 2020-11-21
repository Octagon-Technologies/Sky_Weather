package com.octagon_technologies.sky_weather.ui.shared_code

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.TimeFormat
import com.octagon_technologies.sky_weather.capitalizeWordsWithUnderscore
import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("changeLunarTime", "addTimeFormatToLunarTime")
fun TextView.changeLunarTime(lunarTime: String?, timeFormat: TimeFormat?) {
    text =
        if (timeFormat == TimeFormat.HALF_DAY) {
            lunarTime?.let {
                "$it ${if (it.take(2).toInt() <= 11) "am" else "pm"}"
            } ?: "--:--"
        } else lunarTime ?: "--"
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
        val hours = SimpleDateFormat("HH", Locale.getDefault()).format(date).toInt()
        Timber.d("hours is $hours")

        if (this is ImageView) {
            val drawable = when (hours) {
                in 6..19 -> R.drawable.sun
                else -> R.drawable.moon
            }
            Glide.with(this)
                .load(drawable)
                .into(this)
        } else {
            this as TextView
            val defaultLocale = Locale.getDefault()
            text = if (timeFormat == TimeFormat.FULL_DAY) SimpleDateFormat(
                "HH:mm",
                defaultLocale
            ).format(date)
            else "${
                SimpleDateFormat(
                    "hh:mm",
                    defaultLocale
                ).format(date)
            } ${if (hours <= 11) "am" else "pm"}"
        }
    }
}