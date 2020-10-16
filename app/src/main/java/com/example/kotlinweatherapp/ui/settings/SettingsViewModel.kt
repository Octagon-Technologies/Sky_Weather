package com.example.kotlinweatherapp.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.ui.weather_forecast_objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val context: Context) : ViewModel() {
    private val mainSettings: MainSettings by lazy { MainSettings(context) }


    private var _basicSettingsDataClass = MutableLiveData<BasicSettingsDataClass>()
    val basicSettingsDataClass: LiveData<BasicSettingsDataClass>
        get() = _basicSettingsDataClass

    fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            _basicSettingsDataClass.value = BasicSettingsDataClass(
                units = mainSettings.getUnits(),
                windDirectionUnits = mainSettings.getWindDirections(),
                timeFormat = mainSettings.getTimeFormat(),
                displayMode = mainSettings.getDisplayMode(),
                mainSettings = mainSettings
            )
        }
    }
}