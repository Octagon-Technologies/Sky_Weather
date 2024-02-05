package com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.EachDailyForecastItemBinding
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.daily.getFormattedTemp
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.getDayOfMonth
import com.octagon_technologies.sky_weather.utils.getFirstLetterOfDay
import com.octagon_technologies.sky_weather.utils.getWeatherIconFrom
import com.xwray.groupie.databinding.BindableItem
import org.joda.time.DateTime
import org.joda.time.Instant
import java.text.SimpleDateFormat
import java.util.*

class EachDailyForecastItem(private val dailyForecast: DailyForecast, private val selectDailyForecast: (DailyForecast) -> Unit) :
    BindableItem<EachDailyForecastItemBinding>() {
    @SuppressLint("SetTextI18n")
    override fun bind(binding: EachDailyForecastItemBinding, position: Int) {
        binding.dayText.text = getFirstLetterOfDay(dailyForecast.timeInMillis)
        binding.dailyDateText.text = getDayOfMonth(dailyForecast.timeInMillis)

        binding.minTempOfTheDay.text = dailyForecast.nightTime.getFormattedTemp()
        binding.maxTempOfTheDay.text = dailyForecast.dayTime.getFormattedTemp()

        val tempDiff = (dailyForecast.dayTime.temp - dailyForecast.nightTime.temp).toInt().coerceAtLeast(1)
        binding.tempBar.getBarHeightFromTempRange(tempDiff)

        binding.humidityDisplayText.text = dailyForecast.dayTime.getFormattedHumidity()
        binding.weatherImage.getWeatherIconFrom(dailyForecast.dayTime.weatherCode)

        binding.root.setOnClickListener { selectDailyForecast(dailyForecast) }
    }

    private fun ImageView.getBarHeightFromTempRange(tempDiff: Int) {
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

    override fun getLayout() = R.layout.each_daily_forecast_item
}
