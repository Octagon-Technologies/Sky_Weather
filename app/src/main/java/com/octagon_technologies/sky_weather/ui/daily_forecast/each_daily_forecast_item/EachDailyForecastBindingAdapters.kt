package com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.daily_forecast.*
import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.fahrenheitToCelsius
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("getFirstLetterOfDay")
fun TextView.getFirstLetterOfDay(observationTime: ObservationTime?) {
    text = if (observationTime?.value == null) "--" else SimpleDateFormat(
        "EEEE",
        Locale.getDefault()
    ).format(DateTime(observationTime.value).toDate())[0].toString()
}

@BindingAdapter("getDayOfMonth")
fun TextView.getDayOfMonth(observationTime: ObservationTime?) {
    text = if (observationTime?.value == null) "--" else SimpleDateFormat(
        "dd",
        Locale.getDefault()
    ).format(DateTime(observationTime.value).toDate()).toString()
}

@BindingAdapter("getAverageHumidity")
fun TextView.getAverageHumidity(listOfHumidity: List<Humidity>?) {
    text = try {
        " ${(listOfHumidity?.get(1)?.max?.value?.nullPlus(listOfHumidity[0].min?.value)?.coerceAtLeast(1.0)?.toInt()!! / 2)}%"
    } catch (e: Exception) {
        " --%"
    }
}

@BindingAdapter("getBarHeightFromTempRange", "addUnits")
fun ImageView.getBarHeightFromTempRange(tempRange: List<Temp>?, units: Units?) {
    var tempDiff = tempRange?.get(1)?.max?.value?.nullPlus(tempRange[0].min?.value)?.coerceAtLeast(1.0) ?: 1.0
    if (units == Units.IMPERIAL) tempDiff = tempDiff.fahrenheitToCelsius()

    Timber.d("tempDiff is $tempDiff with units as $units")

    // 12: 60 (*5)
    updateLayoutParams {
        height = resources.getDimensionPixelSize(
            when (tempDiff.toInt()) {
                in 0..5 -> R.dimen._55sdp
                in 0..10 -> R.dimen._60sdp
                in 0..15 -> R.dimen._65sdp
                in 0..20 -> R.dimen._70sdp
                else -> R.dimen._75sdp
            }
        )
        width = resources.getDimensionPixelSize(R.dimen._16sdp)
    }
}

fun Double?.nullPlus(input: Double?) = ((this ?: 0.0) + (input ?: 0.0))
