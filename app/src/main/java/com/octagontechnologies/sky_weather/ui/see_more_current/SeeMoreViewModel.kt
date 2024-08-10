package com.octagontechnologies.sky_weather.ui.see_more_current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import com.octagontechnologies.sky_weather.utils.weather.getAdvancedWeatherConditions
import com.octagontechnologies.sky_weather.utils.weather.getCoreWeatherConditions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SeeMoreViewModel @Inject constructor(
    private val currentRepo: CurrentForecastRepo,
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val currentForecast = currentRepo.currentForecast
    val units =
        settingsRepo.units.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, Units.getDefault())
    val windDirectionUnits =
        settingsRepo.windDirectionUnits.asFlow()
            .stateIn(viewModelScope, SharingStarted.Eagerly, WindDirectionUnits.getDefault())

    val conditions = currentForecast.map { singleForecast ->
        singleForecast.getCoreWeatherConditions(units.value, windDirectionUnits.value
        ) + singleForecast.getAdvancedWeatherConditions(units.value)
    }


}