package com.octagontechnologies.sky_weather.feature.seemore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.model.preferences.WindDirectionUnits
import com.octagontechnologies.sky_weather.data.repository.CurrentForecastRepo
import com.octagontechnologies.sky_weather.data.repository.SettingsRepo
import com.octagontechnologies.sky_weather.feature.current.presentation.formatter.getAdvancedWeatherConditions
import com.octagontechnologies.sky_weather.feature.current.presentation.formatter.getCoreWeatherConditions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SeeMoreViewModel
    @Inject
    constructor(
        private val currentRepo: CurrentForecastRepo,
        private val settingsRepo: SettingsRepo,
    ) : ViewModel() {
        val currentForecast = currentRepo.currentForecast
        val units =
            settingsRepo.units.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, Units.getDefault())
        val windDirectionUnits =
            settingsRepo.windDirectionUnits
                .asFlow()
                .stateIn(viewModelScope, SharingStarted.Eagerly, WindDirectionUnits.getDefault())

        val conditions =
            currentForecast.map { singleForecast ->
                singleForecast.getCoreWeatherConditions(
                    units.value,
                    windDirectionUnits.value,
                ) + singleForecast.getAdvancedWeatherConditions(units.value)
            }
    }
