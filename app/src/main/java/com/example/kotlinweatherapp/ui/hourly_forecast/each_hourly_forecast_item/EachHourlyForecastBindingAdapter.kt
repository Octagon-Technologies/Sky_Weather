package com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.network.current_forecast.ObservationTime
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter("getEachHourlyTime")
fun TextView.getEachHourlyTime(eachHourlyForecast: EachHourlyForecast?) {
    val isoDate = eachHourlyForecast?.observationTime?.value
    isoDate?.let {
        val date = DateTime(isoDate).toLocalDate().toDate()
        val stringTime = SimpleDateFormat("hh:mm", Locale.getDefault()).format(date)
        Timber.d(stringTime)
        text = stringTime
        return
    }

    text = "25:00"
}

