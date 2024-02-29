package com.octagon_technologies.sky_weather.ui.daily_forecast.daily_tab

import androidx.lifecycle.ViewModel
import com.octagon_technologies.sky_weather.ads.AdRepo
import com.octagon_technologies.sky_weather.domain.daily.TimePeriod
import com.octagon_technologies.sky_weather.domain.daily.getFormattedCloudCeiling
import com.octagon_technologies.sky_weather.domain.daily.getFormattedCloudCover
import com.octagon_technologies.sky_weather.domain.daily.getFormattedFeelsLike
import com.octagon_technologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.daily.getFormattedTemp
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.Units
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyTabViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val adRepo = AdRepo()

    fun getWeatherDescriptionFromTimePeriod(timePeriod: TimePeriod): List<EachWeatherDescription> =
        listOf(
            EachWeatherDescription(
                "Temp",
                timePeriod.getFormattedTemp()
            ),
            EachWeatherDescription(
                "FeelsLike",
                timePeriod.getFormattedFeelsLike()
            ),
            EachWeatherDescription(
                "Humidity",
                timePeriod.getFormattedHumidity()
            ),
            EachWeatherDescription(
                "Average Wind",
                timePeriod.wind.getWindSpeedWithDirection(
                    settingsRepo.units.value,
                    settingsRepo.windDirectionUnits.value
                )
            ),
            EachWeatherDescription(
                "Pressure",
                "${timePeriod.pressure?.toInt() ?: "-- "} ${if (settingsRepo.units.value == Units.IMPERIAL) "inHg" else "mbar"}"
            ),
            EachWeatherDescription(
                "Cloud Cover",
                timePeriod.getFormattedCloudCover()
            ),
            EachWeatherDescription(
                "Cloud Ceiling",
                timePeriod.getFormattedCloudCeiling()
            )
        )
}