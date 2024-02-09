package com.octagon_technologies.sky_weather.ui.see_more_current

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.SeeMoreFragmentBinding
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.getAdvancedForecastDescription
import com.octagon_technologies.sky_weather.utils.getWeatherIconFrom
import com.octagon_technologies.sky_weather.utils.isLastIndex
import com.octagon_technologies.sky_weather.utils.removeToolbarAndBottomNav
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
        binding.seeMoreTemp.text = currentForecast.getFormattedTemp()
        binding.seeMoreTempUnits.text = if (settingsRepo.units.value == Units.IMPERIAL) "F" else "C"
        binding.seeMoreWeatherImage.getWeatherIconFrom(currentForecast.weatherCode)

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
                    eachWeatherDescription = it,
                    theme = settingsRepo.theme.value
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        val theme = settingsRepo.theme.value
        removeToolbarAndBottomNav(
            if (theme == Theme.LIGHT) R.color.light_theme_blue else R.color.dark_theme_blue,
            true
        )
    }

}