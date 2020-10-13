package com.example.kotlinweatherapp.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.ui.weather_forecast_objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val context: Context) : ViewModel() {
    private val mainSettings: MainSettings by lazy { MainSettings(context) }

    private var _liveUnits = MutableLiveData<Units>()
    val liveUnits: LiveData<Units>
        get() = _liveUnits

    private var _liveWindDirectionUnits = MutableLiveData<WindDirectionUnits>()
    val liveWindDirectionUnits: LiveData<WindDirectionUnits>
        get() = _liveWindDirectionUnits

    private var _liveTimeFormat = MutableLiveData<TimeFormat>()
    val liveTimeFormat: LiveData<TimeFormat>
        get() = _liveTimeFormat

    private var _liveDisplayMode = MutableLiveData<DisplayMode>()
    val liveDisplayMode: LiveData<DisplayMode>
        get() = _liveDisplayMode

    fun editDataStore(newValue: Any) {
        val eachDataStoreItem = when(newValue) {
            is Units -> EachDataStoreItem(mainSettings.unitsName, newValue)
            is WindDirectionUnits -> EachDataStoreItem(mainSettings.windDirectionName, newValue)
            is TimeFormat -> EachDataStoreItem(mainSettings.timeFormatName, newValue)
            is DisplayMode -> EachDataStoreItem(mainSettings.displayModeName, newValue)
            else -> throw RuntimeException("Unexpected parameter. newValue is $newValue")
        }
        mainSettings.editDataStoreSettings(eachDataStoreItem)
    }

    fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            _liveUnits.value = mainSettings.getUnits()
            _liveWindDirectionUnits.value = mainSettings.getWindDirections()
            _liveTimeFormat.value = mainSettings.getTimeFormat()
            _liveDisplayMode.value = mainSettings.getDisplayMode()
        }
    }
}