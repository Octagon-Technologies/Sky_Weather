package com.octagon_technologies.sky_weather.ui.daily_forecast

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.network.daily_forecast.*
import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item.EachDailyForecastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("addDailyForecast", "addDailyGroupAdapter")
fun RecyclerView.addDailyForecast(
    listOfDailyForecast: ArrayList<EachDailyForecast>?,
    groupAdapter: GroupAdapter<GroupieViewHolder>
) {
    groupAdapter.clear()

    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = groupAdapter

    listOfDailyForecast?.forEach {
        groupAdapter.add(EachDailyForecastItem(it))
    }
}

@BindingAdapter("getMonthFromDate")
fun TextView.getMonthFromDate(observationTime: ObservationTime?) {
    text = SimpleDateFormat(
        "MMMM",
        Locale.getDefault()
    ).format(DateTime(observationTime?.value).toDate())
}