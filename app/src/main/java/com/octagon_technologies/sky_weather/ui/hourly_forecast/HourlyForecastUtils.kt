package com.octagon_technologies.sky_weather.ui.hourly_forecast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
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

            while (item is EachDayTextItem) {
                position++
                item = groupAdapter.getItem(position)
                Timber.d("modified position is $position")
            }

            val timeInISO =
                (item as EachHourlyForecastItem).eachHourlyForecast.observationTime?.value
            val date = getDayOfWeek(timeInISO)

            binding.topDayText.text.apply {
                if (date != this) {
                    binding.topDayText.text = date
                }
            }
        }
    })
}

fun getDayOfWeek(timeInISO: String?): String? {
    val date = DateTime(timeInISO).toLocalDate().toDate()
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
}