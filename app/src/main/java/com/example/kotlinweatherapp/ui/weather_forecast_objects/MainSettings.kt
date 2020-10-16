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

    private val unitsKey = preferencesKey<String>(unitsName)
    private val windDirectionKey = preferencesKey<String>(windDirectionName)
    private val timeFormatKey = preferencesKey<String>(timeFormatName)
    private val displayModeKey = preferencesKey<String>(displayModeName)


    private fun editDataStoreSettings(eachDataStoreItem: EachDataStoreItem) {
        val newValue = eachDataStoreItem.newValue
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit {
                when (eachDataStoreItem.preferencesName) {
                    unitsName -> it[unitsKey] = newValue.toString()
                    windDirectionName -> it[windDirectionKey] = newValue.toString()
                    timeFormatName -> it[timeFormatKey] = newValue.toString()
                    displayModeName -> it[displayModeKey] = newValue.toString()
                    else -> throw RuntimeException("Unexpected parameter. eachDataStoreItem is $eachDataStoreItem")
                }
                Timber.d("Success. DataStore edited")
            }
        }
    }

    private fun getDataStoreData(preferencesName: String): Flow<String?> {
        return dataStore.data.map {
            when (preferencesName) {
                unitsName -> it[unitsKey]
                windDirectionName -> it[windDirectionKey]
                timeFormatName -> it[timeFormatKey]
                displayModeName -> it[displayModeKey]
                else -> throw RuntimeException("Unexpected parameter. preferencesName is $preferencesName")
            }
        }
    }

    fun editDataStore(newValue: Any) {
        val eachDataStoreItem = when(newValue) {
            is Units -> EachDataStoreItem(unitsName, newValue)
            is WindDirectionUnits -> EachDataStoreItem(windDirectionName, newValue)
            is TimeFormat -> EachDataStoreItem(timeFormatName, newValue)
            is DisplayMode -> EachDataStoreItem(displayModeName, newValue)
            else -> throw RuntimeException("Unexpected parameter. newValue is $newValue")
        }

        editDataStoreSettings(eachDataStoreItem)
    }

    suspend fun getUnits(): Units {
        return Units.valueOf(getDataStoreData(unitsName).first() ?: "METRIC")
    }

    suspend fun getWindDirections(): WindDirectionUnits {
        return WindDirectionUnits.valueOf(getDataStoreData(windDirectionName).first() ?: "CARDINAL")
    }

    suspend fun getTimeFormat(): TimeFormat {
        return TimeFormat.valueOf(getDataStoreData(timeFormatName).first() ?: "FULL_DAY")
    }

    suspend fun getDisplayMode(): DisplayMode {
        return DisplayMode.valueOf(getDataStoreData(displayModeName).first() ?: "LIGHT")
    }
}