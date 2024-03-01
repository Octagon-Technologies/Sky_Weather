package com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.EachDailyForecastItemBinding
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.daily.getFormattedTemp
import com.octagon_technologies.sky_weather.utils.getDayOfMonth
import com.octagon_technologies.sky_weather.utils.getFirstLetterOfDay
import com.octagon_technologies.sky_weather.utils.loadWeatherIcon
import com.xwray.groupie.databinding.BindableItem
import java.util.*

class EachDailyForecastItem(private val dailyForecast: DailyForecast, private val selectDailyForecast: (DailyForecast) -> Unit) :
    BindableItem<EachDailyForecastItemBinding>() {
    @SuppressLint("SetTextI18n")
    override fun bind(binding: EachDailyForecastItemBinding, position: Int) {
        binding.dayText.text = dailyForecast.timeInEpochSeconds.getFirstLetterOfDay()
        binding.dailyDateText.text = dailyForecast.timeInEpochSeconds.getDayOfMonth()

        binding.minTempOfTheDay.text = dailyForecast.nightTime.getFormattedTemp()
        binding.maxTempOfTheDay.text = dailyForecast.dayTime.getFormattedTemp()

        if (dailyForecast.dayTime.temp != null && dailyForecast.nightTime.temp != null) {
            val tempDiff = (dailyForecast.dayTime.temp -dailyForecast.nightTime.temp).toInt()
                .coerceAtLeast(1)
            binding.tempBar.getBarHeightFromTempRange(tempDiff)
        }

        binding.humidityDisplayText.text = dailyForecast.dayTime.getFormattedHumidity()

        // Setting time to null so that it brings the automatic day weather icon... since the null alternative
        // is 12th hour of the day
        binding.weatherImage.loadWeatherIcon(null, dailyForecast.dayTime.weatherCode)

        binding.root.setOnClickListener { selectDailyForecast(dailyForecast) }
    }

    private fun ImageView.getBarHeightFromTempRange(tempDiff: Int) {
        // 12: 60 (*5)
        updateLayoutParams {
            height = resources.getDimensionPixelSize(
                when (tempDiff) {
                    in 0..5 -> R.dimen._55sdp
                    in 0..6 -> R.dimen._56sdp
                    in 0..7 -> R.dimen._57sdp
                    in 0..8 -> R.dimen._58sdp
                    in 0..9 -> R.dimen._59sdp
                    in 0..10 -> R.dimen._60sdp
                    in 0..11 -> R.dimen._61sdp
                    in 0..12 -> R.dimen._62sdp
                    in 0..13 -> R.dimen._63sdp
                    in 0..14 -> R.dimen._64sdp
                    in 0..15 -> R.dimen._65sdp
                    in 0..16 -> R.dimen._66sdp
                    in 0..17 -> R.dimen._67sdp
                    in 0..18 -> R.dimen._68sdp
                    in 0..19 -> R.dimen._69sdp
                    in 0..20 -> R.dimen._70sdp
                    else -> R.dimen._75sdp
                }
            )
            width = resources.getDimensionPixelSize(R.dimen._16sdp)
        }
    }

    override fun getLayout() = R.layout.each_daily_forecast_item
}
