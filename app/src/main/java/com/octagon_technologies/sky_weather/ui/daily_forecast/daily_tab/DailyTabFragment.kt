package com.octagon_technologies.sky_weather.ui.daily_forecast.daily_tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.DailyTabFragmentBinding
import com.octagon_technologies.sky_weather.domain.daily.TimePeriod
import com.octagon_technologies.sky_weather.domain.daily.getFormattedFeelsLike
import com.octagon_technologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.daily.getFormattedTemp
import com.octagon_technologies.sky_weather.lazy.adHelpers
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import com.octagon_technologies.sky_weather.utils.changeWeatherIconTint
import com.octagon_technologies.sky_weather.utils.isLastIndex
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber
import kotlin.properties.Delegates

class DailyTabFragment : Fragment(R.layout.daily_tab_fragment) {

    lateinit var settingsRepo: SettingsRepo

    private lateinit var binding: DailyTabFragmentBinding
    private lateinit var timePeriod: LiveData<TimePeriod>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DailyTabFragmentBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.theme = settingsRepo.theme

        setUpWeatherConditions()
//        setUpAd()
    }

//    private fun setUpAd() {
//        adHelper.loadAd(binding.dailyTabAdView) {
//            Timber.d("Load ad listener return $it")
//            // If it failed in onAdFailedToLoad(), hide the ad view
//            if (!it) {
//                binding.dailyTabAdView.root.visibility = View.GONE
//            }
//        }
//    }

    private fun setUpWeatherConditions() {
        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.selectedDayRecyclerview.adapter = groupAdapter

        timePeriod.observe(viewLifecycleOwner) { timePeriod ->
            binding.selectedDayTemp.text = timePeriod.getFormattedTemp()
            binding.selectedDayHumidityText.text = timePeriod.getFormattedHumidity()
            binding.selectedDayWeatherIcon.changeWeatherIconTint(
                settingsRepo.theme.value,
                timePeriod.isDay
            )
            binding.selectedDayWeatherStatus.text = timePeriod.weatherCode.getWeatherTitle()

            binding.selectedSunHours.text = timePeriod.lunar.sunHrsOnly
            binding.selectedSunMinutes.text = timePeriod.lunar.sunMinsOnly
            binding.selectedMoonHours.text = timePeriod.lunar.moonHrsOnly
            binding.selectedMoonMinutes.text = timePeriod.lunar.moonMinsOnly

            val timeFormat = settingsRepo.timeFormat.value
            binding.selectedSunRiseDisplayTime.text = timePeriod.lunar.getSunRiseDisplay(timeFormat)
            binding.selectedSunSetDisplayTime.text = timePeriod.lunar.getSunSetDisplay(timeFormat)
            binding.selectedMoonRiseDisplayTime.text =
                timePeriod.lunar.getMoonRiseDisplay(timeFormat)
            binding.selectedMoonSetDisplayTime.text = timePeriod.lunar.getMoonSetDisplay(timeFormat)

            val dailyConditions = listOf(
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
                    "${timePeriod.pressure.toInt()} ${if (settingsRepo.units.value == Units.IMPERIAL) "inHg" else "mbar"}"
                )
            )

            groupAdapter.update(
                dailyConditions.map {
                    MiniForecastDescription(
                        dailyConditions.isLastIndex(it),
                        it,
                        settingsRepo.theme.value
                    )
                }
            )
        }
    }


    companion object {
        fun getInstance(
            selectedTimePeriod: LiveData<TimePeriod>,
            provideSettingsRepo: SettingsRepo
        ) =
            DailyTabFragment().apply {
                timePeriod = selectedTimePeriod
                settingsRepo = provideSettingsRepo
            }
    }
}