package com.example.kotlinweatherapp.ui.shared_code

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.network.current_forecast.ObservationTime
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.ui.see_more_current.removeUnderScoreFromString
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("getWeatherStatus")
fun TextView.getWeatherStatus(singleForecast: SingleForecast?){
    text = removeUnderScoreFromString(singleForecast?.weatherCode?.value ?: "Infinity")
}

@BindingAdapter("getWeatherImageBasedOnTime")
fun ImageView.getWeatherImageBasedOnTime(observationTime: ObservationTime?) {
    observationTime?.value?.let {
        val date = DateTime(it).toLocalDate().toDate()
        Timber.d("Joda datetime is ${date.time}")
        val hours = SimpleDateFormat("hh", Locale.getDefault()).format(date)
        Timber.d("hours is $hours")

        val drawable = when (hours.toInt()) {
            in 8..18 -> R.drawable.sun
            else -> R.drawable.moon
        }

        Glide.with(this)
            .load(drawable)
            .into(this)

        return
    }

    Glide.with(context)
        .load(R.drawable.radar)
        .into(this)
}