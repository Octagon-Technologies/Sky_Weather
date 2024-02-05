package com.octagon_technologies.sky_weather.widgets

import android.content.Context
import androidx.lifecycle.*
import com.octagon_technologies.sky_weather.models.WidgetData
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WidgetConfigureViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepo: SettingsRepo,
    private val locationRepo: LocationRepo,
    private val widgetSettings: WidgetSettings
) : ViewModel() {
    val location = locationRepo.location

    val navigateToLocationFragment = MutableLiveData(false)
    val shouldCreateWidget = MutableLiveData(false)

    fun launchCreateWidget() { shouldCreateWidget.value = true }

    fun addWidget(widgetId: Int, transparencyOutOf255: Int) {
        viewModelScope.launch {
            val location = location.value ?: run {
                Timber.d("location.value is $location.value in addWidget()")
                return@launch
            }

            val widgetData = WidgetData(
                widgetId = widgetId,
                location = location,
                transparencyOutOf255 = transparencyOutOf255,
                timeFormat = settingsRepo.timeFormat.value ?: TimeFormat.FULL_DAY,
                units = settingsRepo.units.value ?: Units.METRIC
            )
            widgetSettings.addWidgetId(widgetData)
        }
    }

    fun navigateToLocationFragment() {
        navigateToLocationFragment.value = true
    }

}