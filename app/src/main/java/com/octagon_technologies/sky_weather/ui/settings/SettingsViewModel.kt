package com.octagon_technologies.sky_weather.ui.settings

import android.Manifest
import android.os.Build
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val isNotificationAllowed = settingsRepo.isNotificationAllowed.asLiveData()
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat
    val theme = settingsRepo.theme

    fun toggleNotificationAllowed(isChecked: Boolean, rootView: View) {
        viewModelScope.launch {
            // To avoid unnecessary edits, confirm that the new value is different from what we have stored
//            if (isChecked != isNotificationAllowed.value) {
                confirmNotificationPermission(isChecked, rootView)
//            }
        }
    }

    private fun confirmNotificationPermission(isChecked: Boolean, rootView: View) {
        // If we are changing this to false or we are below Tiramisu, we don't need Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isChecked) {
            val snackBarListener = SnackbarOnDeniedPermissionListener.Builder
                .with(rootView, "Allow notifications in Settings")
                .withDuration(Snackbar.LENGTH_LONG)
                .withOpenSettingsButton("Open Settings")
                .build()

            val basePermissionListener = object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    super.onPermissionGranted(p0)

                    viewModelScope.launch {
                        settingsRepo.changeIsNotificationAllowed(isChecked)
                    }
                }
            }
            val compositePermissionListener =
                CompositePermissionListener(snackBarListener, basePermissionListener)
            Dexter.withContext(rootView.context)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(compositePermissionListener)
                .check()
        } else {
            viewModelScope.launch { settingsRepo.changeIsNotificationAllowed(isChecked) }
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