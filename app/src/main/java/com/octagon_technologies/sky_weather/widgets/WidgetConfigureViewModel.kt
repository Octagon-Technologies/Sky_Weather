package com.octagon_technologies.sky_weather.widgets

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.octagon_technologies.sky_weather.models.WidgetData
import com.octagon_technologies.sky_weather.repository.SettingsRepo
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WidgetConfigureViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepo: SettingsRepo,
    private val weatherDataBase: WeatherDataBase,
    private val widgetSettings: WidgetSettings
    ) : ViewModel() {
    val reverseGeoCodingLocation = MutableLiveData<ReverseGeoCodingLocation>()

    val navigateToLocationFragment = MutableLiveData(false)
    val shouldCreateWidget = MutableLiveData(false)

    init {
        initDataWithLocalData()
    }

    private fun initDataWithLocalData() {
        viewModelScope.launch(Dispatchers.IO) {
            val localReverseLocation = try {
                weatherDataBase.locationDao.getLocationDatabaseClass().reversedLocation
            } catch (e: Exception) {
                Timber.e(e)
                return@launch
            }

            withContext(Dispatchers.Main.immediate) {
                reverseGeoCodingLocation.value = localReverseLocation
            }
        }
    }

    fun launchCreateWidget() { shouldCreateWidget.value = true }

    fun addWidget(widgetId: Int, transparencyOutOf255: Int) {
        viewModelScope.launch {
            val location = reverseGeoCodingLocation.value ?: run {
                Timber.d("reverseGeoCodingLocation.value is $reverseGeoCodingLocation.value in addWidget()")
                return@launch
            }

            val widgetData = WidgetData(
                widgetId = widgetId,
                reverseGeoCodingLocation = location,
                transparencyOutOf255 = transparencyOutOf255,
                timeFormat = settingsRepo.getTimeFormat(),
                units = settingsRepo.getUnits()
            )
            widgetSettings.addWidgetId(widgetData)
        }
    }

    fun navigateToLocationFragment() {
        navigateToLocationFragment.value = true
    }

}