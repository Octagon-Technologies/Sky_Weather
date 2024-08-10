package com.octagontechnologies.sky_weather.ui.settings

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.notification.CustomNotificationCompat
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val customNotificationCompat: CustomNotificationCompat
) : ViewModel() {

    val isNotificationAllowed = settingsRepo.isNotificationAllowed

    private val _showSystemPermissionDialog = MutableStateFlow(false)
    val showSystemPermissionDialog: StateFlow<Boolean> = _showSystemPermissionDialog

    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat
    val theme = settingsRepo.theme.asFlow().onEach { Timber.d("theme: $it") }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Theme.LIGHT)



    fun toggleNotificationAllowed(shouldTurnOn: Boolean) {
        viewModelScope.launch {
            Timber.d("shouldTurnOn is $shouldTurnOn")

            // We are turning notifications off
            if (!shouldTurnOn) {
                settingsRepo.changeIsNotificationAllowed(false)
                customNotificationCompat.clearNotification()
                return@launch
            }

            // Below Tiramisu - API 33, notifications do not need permission
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                settingsRepo.changeIsNotificationAllowed(true)
            } else {
                _showSystemPermissionDialog.value = true
            }
        }
    }


    fun handleNotificationPermissionResponse(isGranted: Boolean) = viewModelScope.launch {
        settingsRepo.changeIsNotificationAllowed(isGranted)
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


    fun resetSystemPermissionDialog() {
        _showSystemPermissionDialog.value = false
    }
}