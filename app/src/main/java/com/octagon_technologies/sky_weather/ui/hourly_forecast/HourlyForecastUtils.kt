package com.octagon_technologies.sky_weather.ui.hourly_forecast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.HeaderMiniHourlyForecast
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.MiniHourlyForecast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun RecyclerView.addCustomScrollListener(groupAdapter: GroupAdapter<GroupieViewHolder>, binding: HourlyForecastFragmentBinding) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            var position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            var item = groupAdapter.getItem(position)
            Timber.d("Initial position is $position")

            while (item is HeaderMiniHourlyForecast) {
                position++
                item = groupAdapter.getItem(position)
                Timber.d("modified position is $position")
            }

            val timeInISO =
                (item as MiniHourlyForecast).hourlyForecast.timeInMillis
            val date = getDayOfWeek(timeInISO)

            binding.topDayText.text.apply {
                if (date != this) {
                    binding.topDayText.text = date
                }
            }
        }
    })
}

fun getDayOfWeek(timeInISO: Long?): String? {
    val date = DateTime(timeInISO).toLocalDate().toDate()
    return if (timeInISO != null) SimpleDateFormat("EEEE", Locale.getDefault()).format(date) else "-----"
}