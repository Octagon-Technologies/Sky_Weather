package com.octagon_technologies.sky_weather.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val isNotificationAllowed = settingsRepo.isNotificationAllowed
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat
    val theme = settingsRepo.theme

    fun toggleNotificationAllowed(isChecked: Boolean) {
        viewModelScope.launch {
            // To avoid unnecessary edits, confirm that the new value is different from what we have stored
            if (isChecked != isNotificationAllowed.value)
                settingsRepo.changeIsNotificationAllowed(isChecked)
        }
    }

    fun changeUnits(newUnits: Units) {
        viewModelScope.launch {
            if (newUnits != units.value) settingsRepo.changeUnits(newUnits)
        }
    }

    fun changeWindDirections(newWindDirectionUnits: WindDirectionUnits) {
        viewModelScope.launch {
            if (newWindDirectionUnits != windDirectionUnits.value)
                settingsRepo.changeWindDirectionUnits(newWindDirectionUnits)
        }
    }

    fun changeTimeFormat(newTimeFormat: TimeFormat) {
        viewModelScope.launch {
            if (newTimeFormat != timeFormat.value) settingsRepo.changeTimeFormat(newTimeFormat)
        }
    }

    fun changeTheme(newTheme: Theme) {
        viewModelScope.launch {
            if (newTheme != theme.value) settingsRepo.changeTheme(newTheme)
        }
    }
}