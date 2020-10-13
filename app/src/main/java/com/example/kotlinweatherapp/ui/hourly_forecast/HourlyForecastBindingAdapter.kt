package com.example.kotlinweatherapp.ui.hourly_forecast

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.databinding.EachHourlyForecastItemBinding
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import com.example.kotlinweatherapp.ui.current_forecast.EachCurrentForecastDescriptionItem
import com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
import com.example.kotlinweatherapp.ui.see_more_current.getAdvancedForecastDescription
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

@BindingAdapter("getHourlyForecastRecyclerView")
fun RecyclerView.getHourlyForecastRecyclerView(arrayOfEachHourlyForecast: ArrayList<EachHourlyForecast>?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    var firstDay = getDayOfWeek(arrayOfEachHourlyForecast?.get(0)?.observationTime?.value)

    arrayOfEachHourlyForecast?.forEach {
        val currentDay = getDayOfWeek(it.observationTime?.value)

        if (firstDay != currentDay) {
            firstDay = currentDay
            groupAdapter.add(EachDayTextItem(currentDay ?: "Andre"))
        }

        groupAdapter.add(EachHourlyForecastItem(it))
    }

}

@BindingAdapter("formatIsoDate")
fun TextView.formatIsoDate(hourlyForecast: EachHourlyForecast?) {
    text = getDayOfWeek(hourlyForecast?.observationTime?.value)
}

@BindingAdapter("getSelectedHourlyRecyclerView")
fun RecyclerView.getSelectedHourlyRecyclerView(singleForecast: SingleForecast?) {
    layoutManager = LinearLayoutManager(context)

    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    val arrayOfEachWeatherDescriptions = getAdvancedForecastDescription(singleForecast)

    arrayOfEachWeatherDescriptions.forEach {
        groupAdapter.add(EachCurrentForecastDescriptionItem(arrayOfEachWeatherDescriptions.size - 1, it, true))
    }
}


