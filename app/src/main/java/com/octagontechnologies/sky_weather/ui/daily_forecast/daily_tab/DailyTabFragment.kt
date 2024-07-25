package com.octagontechnologies.sky_weather.ui.daily_forecast.daily_tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.DailyTabFragmentBinding
import com.octagontechnologies.sky_weather.domain.daily.TimePeriod
import com.octagontechnologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.daily.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagontechnologies.sky_weather.utils.isLastIndex
import com.octagontechnologies.sky_weather.utils.loadWeatherIcon
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DailyTabFragment : Fragment(R.layout.daily_tab_fragment) {

    private val viewModel: DailyTabViewModel by viewModels()

    @Inject
    lateinit var settingsRepo: SettingsRepo

    private lateinit var binding: DailyTabFragmentBinding
    lateinit var timePeriod: LiveData<TimePeriod>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DailyTabFragmentBinding.bind(view)

        if (::binding.isInitialized)
            setUpAd()

        if (::timePeriod.isInitialized && ::binding.isInitialized) {
            setUpWeatherConditions()
        }
    }


    private fun setUpAd() {
        viewModel.adRepo.getNativeAd(
            fragment = this,
            nativeAdBinding = binding.dailyTabAdView,
            onAdCompleted = { isSuccess ->
                Timber.d("isSuccess is $isSuccess")

                binding.dailyTabAdView.root.visibility =
                    if (isSuccess) View.VISIBLE else View.GONE
            })
    }

    private fun setUpWeatherConditions() {
        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.selectedDayRecyclerview.adapter = groupAdapter

        timePeriod.observe(viewLifecycleOwner) { timePeriod ->
            Timber.d("timePeriod.observe called with $timePeriod")
            binding.selectedDayTemp.text = timePeriod.getFormattedTemp()
            binding.selectedDayHumidityText.text = requireContext().getString(
                R.string.humidity_format,
                timePeriod.getFormattedHumidity()
            )
            binding.selectedDayWeatherIcon.loadWeatherIcon(timePeriod.isDay, timePeriod.weatherCode)
            binding.selectedDayWeatherStatus.text = timePeriod.weatherCode.getWeatherTitle()

            binding.selectedSunHours.text = timePeriod.dailyLunar.getSunHoursFull()
            binding.selectedSunMinutes.text = timePeriod.dailyLunar.getSunMinutesFull()
            binding.selectedMoonHours.text = timePeriod.dailyLunar.getMoonHoursFull()
            binding.selectedMoonMinutes.text = timePeriod.dailyLunar.getMoonMinutesFull()

            val timeFormat = settingsRepo.timeFormat.value
            binding.selectedSunRiseDisplayTime.text = timePeriod.dailyLunar.getSunRiseDisplay(timeFormat)
            binding.selectedSunSetDisplayTime.text = timePeriod.dailyLunar.getSunSetDisplay(timeFormat)
            binding.selectedMoonRiseDisplayTime.text =
                timePeriod.dailyLunar.getMoonRiseDisplay(timeFormat)
            binding.selectedMoonSetDisplayTime.text = timePeriod.dailyLunar.getMoonSetDisplay(timeFormat)

            val dailyConditions = viewModel.getWeatherDescriptionFromTimePeriod(timePeriod)

            groupAdapter.update(
                dailyConditions.map {
                    MiniForecastDescription(
                        dailyConditions.isLastIndex(it),
                        it
                    )
                }
            )
        }
    }


    companion object {
        fun getInstance(
            selectedTimePeriod: LiveData<TimePeriod>
        ) =
            DailyTabFragment().apply {
                timePeriod = selectedTimePeriod
            }
    }
}