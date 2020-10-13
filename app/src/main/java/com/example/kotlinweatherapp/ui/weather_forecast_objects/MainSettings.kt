package com.example.kotlinweatherapp.ui.weather_forecast_objects

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class MainSettings (private val context: Context) {

    private val dataStore: DataStore<Preferences> by lazy {
        context.createDataStore(
            "settings"
        )
    }

    val unitsName = "units"
    val windDirectionName = "wind_direction"
    val timeFormatName = "time_format"
    val displayModeName = "display_mode"

    private val unitsKey = preferencesKey<Units>(unitsName)
    private val windDirectionKey = preferencesKey<WindDirectionUnits>(windDirectionName)
    private val timeFormatKey = preferencesKey<TimeFormat>(timeFormatName)
    private val displayModeKey = preferencesKey<DisplayMode>(displayModeName)


    fun editDataStoreSettings(eachDataStoreItem: EachDataStoreItem) {
        val newValue = eachDataStoreItem.newValue
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit {
                when (eachDataStoreItem.preferencesName) {
                    unitsName -> it[unitsKey] = newValue as Units
                    windDirectionName -> it[windDirectionKey] = newValue as WindDirectionUnits
                    timeFormatName -> it[timeFormatKey] = newValue as TimeFormat
                    displayModeName -> it[displayModeKey] = newValue as DisplayMode
                    else -> throw RuntimeException("Unexpected parameter. eachDataStoreItem is $eachDataStoreItem")
                }
                Timber.d("Success. DataStore edited")
            }
        }
    }

    private fun getDataStoreData(preferencesName: String): Flow<Any?> {
        return dataStore.data.map {
            when (preferencesName) {
                unitsName -> unitsKey
                windDirectionName -> windDirectionKey
                timeFormatName -> timeFormatKey
                displayModeName -> displayModeKey
                else -> throw RuntimeException("Unexpected parameter. preferencesName is $preferencesName")
            }
        }
    }

    suspend fun getUnits(): Units? {
        return getDataStoreData(unitsName).first() as Units?
    }

    suspend fun getWindDirections(): WindDirectionUnits? {
        return getDataStoreData(windDirectionName).first() as WindDirectionUnits?
    }

    suspend fun getTimeFormat(): TimeFormat? {
        return getDataStoreData(timeFormatName).first() as TimeFormat?
    }

    suspend fun getDisplayMode(): DisplayMode? {
        return getDataStoreData(displayModeName).first() as DisplayMode?
    }
}