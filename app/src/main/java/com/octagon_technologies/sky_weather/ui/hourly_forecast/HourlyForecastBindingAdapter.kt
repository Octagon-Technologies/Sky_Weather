package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.EachHourlyForecastItemBinding
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.current_forecast.EachCurrentForecastDescriptionItem
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.databinding.BindableItem
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

@SuppressLint("SetTextI18n")
@BindingAdapter(
    "formatSelectedDate",
    "addSelectedTimeFormat",
    "isDailySelected",
    requireAll = false
)
fun TextView.formatSelectedDate(
    observationTime: ObservationTime?,
    timeFormat: TimeFormat?,
    isDailySelected: Boolean = false
) {

    observationTime?.value?.let {
        val date = DateTime(it).toDate()

        text = if (isDailySelected) {
            SimpleDateFormat(
                "EEEE, MMMM dd",
                Locale.getDefault()
            ).format(date)
        } else {
            SimpleDateFormat(
                "${if (timeFormat == TimeFormat.FULL_DAY) "HH" else "hh"}:mm, EEEE",
                Locale.getDefault()
            ).format(date)
        }

        if (timeFormat == TimeFormat.HALF_DAY && !isDailySelected) {
            val endFormat = if (SimpleDateFormat("HH", Locale.ENGLISH)
                    .format(date).toInt() <= 11) "am" else "pm"
            text = StringBuilder(text.toString())
                .insert(5, " $endFormat ")
        }

        // Monday, October 12
        Timber.d("Final text is $text")
        return
    }
    text = "--"

}

@BindingAdapter("addGroupAdapter", "addRecyclerViewTimeFormat", "getHourlyForecastRecyclerView")
fun RecyclerView.getHourlyForecastRecyclerView(
    groupAdapter: GroupAdapter<GroupieViewHolder>,
    timeFormat: TimeFormat?,
    arrayOfEachHourlyForecast: ArrayList<EachHourlyForecast>?,
) {
    groupAdapter.clear()

    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter

    val mapOfEachHourlyForecast =
        LinkedHashMap<String?, BindableItem<EachHourlyForecastItemBinding>>()

    var firstDay = getDayOfWeek(arrayOfEachHourlyForecast?.get(0)?.observationTime?.value)

    arrayOfEachHourlyForecast?.forEach {
        val currentDay = getDayOfWeek(it.observationTime?.value)

        if (firstDay != currentDay) {
            firstDay = currentDay
            groupAdapter.add(EachDayTextItem(currentDay ?: "Andre"))
        }

        val eachHourlyForecastItem = EachHourlyForecastItem(it, timeFormat)
        mapOfEachHourlyForecast[it.observationTime?.value] = eachHourlyForecastItem
        groupAdapter.add(eachHourlyForecastItem)
    }
}

@BindingAdapter("formatIsoDate")
fun TextView.formatIsoDate(hourlyForecast: EachHourlyForecast?) {
    text = getDayOfWeek(hourlyForecast?.observationTime?.value)
}

@BindingAdapter("getSelectedHourlyRecyclerView", "addSelectedHourlyTheme", "addUnitSystemToSelectedHourly", "addWindDirectionToSelectedHourly")
fun RecyclerView.getSelectedHourlyRecyclerView(singleForecast: SingleForecast?, theme: Theme?, units: Units?, windDirectionUnits: WindDirectionUnits?) {
    layoutManager = LinearLayoutManager(context)

    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    val arrayOfEachWeatherDescriptions = getAdvancedForecastDescription(singleForecast, units, windDirectionUnits)

    arrayOfEachWeatherDescriptions.forEach {
        groupAdapter.add(
            EachCurrentForecastDescriptionItem(
                arrayOfEachWeatherDescriptions.size - 1,
                it,
                theme
            )
        )
    }
}

