package com.octagontechnologies.sky_weather.ui.see_more_current

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.SeeMoreFragmentBinding
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagontechnologies.sky_weather.utils.getAdvancedForecastDescription
import com.octagontechnologies.sky_weather.utils.isImperial
import com.octagontechnologies.sky_weather.utils.loadWeatherIcon
import com.octagontechnologies.sky_weather.utils.isLastIndex
import com.octagontechnologies.sky_weather.utils.removeToolbarAndBottomNav
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreFragment : Fragment(R.layout.see_more_fragment) {

    @Inject
    lateinit var currentRepo: CurrentForecastRepo
    @Inject
    lateinit var settingsRepo: SettingsRepo
    private lateinit var binding: SeeMoreFragmentBinding

    private val navArgs: SeeMoreFragmentArgs by navArgs()
    private val currentForecast by lazy { navArgs.currentForecast }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SeeMoreFragmentBinding.bind(view)

        binding.seeMoreHighlight.text = currentForecast.weatherCode.getWeatherTitle()
        binding.seeMoreTemp.text = currentForecast.getFormattedTemp(settingsRepo.units.value.isImperial())
        binding.seeMoreTempUnits.text = if (settingsRepo.units.value.isImperial()) "F" else "C"
        binding.seeMoreWeatherImage.loadWeatherIcon(currentForecast.timeInEpochMillis, currentForecast.weatherCode)

        setUpSeeMoreConditions()

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpSeeMoreConditions() {
        binding.seeMoreDescriptionRecyclerview.adapter = groupAdapter

        val listOfWeatherDescriptions = getAdvancedForecastDescription(
            singleForecast = currentForecast,
            units = settingsRepo.units.value,
            windDirectionUnits = settingsRepo.windDirectionUnits.value
        )

        groupAdapter.update(
            listOfWeatherDescriptions.map {
                MiniForecastDescription(
                    isLastItem = listOfWeatherDescriptions.isLastIndex(it),
                    eachWeatherDescription = it
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        val theme = settingsRepo.theme.value
        removeToolbarAndBottomNav()
    }

}