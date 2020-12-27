package com.octagon_technologies.sky_weather.widgets

import android.content.Context
import androidx.lifecycle.*
import com.octagon_technologies.sky_weather.repository.SettingsRepo
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WidgetConfigureViewModel(private val context: Context) : ViewModel() {

    val settingsRepo by lazy { SettingsRepo(context) }
    private val widgetSettings by lazy { WidgetSettings(context) }
    val mainDataBase by lazy { MainDataBase.getInstance(context) }

    val reverseGeoCodingLocation = MutableLiveData<ReverseGeoCodingLocation>()

    val navigateToLocationFragment = MutableLiveData(false)
    val shouldCreateWidget = MutableLiveData<Boolean>(false)

    private var _widgetWeatherForecast = MutableLiveData<SingleForecast>()
    val widgetWeatherForecast: LiveData<SingleForecast> = _widgetWeatherForecast

    init {
        initDataWithLocalData()
    }

    private fun initDataWithLocalData() {
        viewModelScope.launch(Dispatchers.IO) {
            val localReverseLocation = try {
                mainDataBase.locationDao.getLocationDatabaseClass().reversedLocation
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

    class Factory(val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            WidgetConfigureViewModel(context) as T
    }

}