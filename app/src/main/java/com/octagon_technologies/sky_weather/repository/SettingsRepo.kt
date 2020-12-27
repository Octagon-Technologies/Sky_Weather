package com.octagon_technologies.sky_weather.repository

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.octagon_technologies.sky_weather.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsRepo (private val context: Context) {

    private val dataStore: DataStore<Preferences> by lazy {
        context.createDataStore(
            "settings"
        )
    }

    private val unitsName = "units"
    private val windDirectionName = "wind_direction"
    private val timeFormatName = "time_format"
    private val themeName = "theme_name"
    private val notificationAllowedName = "notification_allowed"

    private val unitsKey = preferencesKey<String>(unitsName)
    private val windDirectionKey = preferencesKey<String>(windDirectionName)
    private val timeFormatKey = preferencesKey<String>(timeFormatName)
    private val themeKey = preferencesKey<String>(themeName)
    private val notificationAllowedKey = preferencesKey<Boolean>(notificationAllowedName)


    private fun editDataStoreSettings(eachDataStoreItem: EachDataStoreItem) {
        val newValue = eachDataStoreItem.newValue
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit {
                when (eachDataStoreItem.preferencesName) {
                    unitsName -> it[unitsKey] = newValue.toString()
                    windDirectionName -> it[windDirectionKey] = newValue.toString()
                    timeFormatName -> it[timeFormatKey] = newValue.toString()
                    themeName -> it[themeKey] = newValue.toString()
                    notificationAllowedName -> it[notificationAllowedKey] = newValue as Boolean
                    else -> throw RuntimeException("Unexpected parameter. eachDataStoreItem is $eachDataStoreItem")
                }
                Timber.d("Success. DataStore edited")
            }
        }
    }

    private fun getDataStoreData(preferencesName: String): Flow<String> {
        return dataStore.data.map {
            when (preferencesName) {
                unitsName -> it[unitsKey] ?: Units.METRIC.toString()
                windDirectionName -> it[windDirectionKey] ?: WindDirectionUnits.CARDINAL.toString()
                timeFormatName -> it[timeFormatKey] ?: TimeFormat.FULL_DAY.toString()
                themeName -> it[themeKey] ?: Theme.DARK.toString()
                notificationAllowedName -> it[notificationAllowedKey]?.toString() ?: "true"
                else -> throw RuntimeException("Unexpected parameter. preferencesName is $preferencesName")
            }
        }
    }

    fun editDataStore(newValue: Any) {
        val eachDataStoreItem = when(newValue) {
            is Units -> EachDataStoreItem(unitsName, newValue)
            is WindDirectionUnits -> EachDataStoreItem(windDirectionName, newValue)
            is TimeFormat -> EachDataStoreItem(timeFormatName, newValue)
            is Theme -> EachDataStoreItem(themeName, newValue)
            is Boolean -> EachDataStoreItem(notificationAllowedName, newValue)
            else -> throw RuntimeException("Unexpected parameter. newValue is $newValue")
        }

        editDataStoreSettings(eachDataStoreItem)
    }

    suspend fun getUnits(): Units = Units.valueOf(getDataStoreData(unitsName).first())
    suspend fun getWindDirections(): WindDirectionUnits = WindDirectionUnits.valueOf(getDataStoreData(windDirectionName).first())
    suspend fun getTimeFormat(): TimeFormat = TimeFormat.valueOf(getDataStoreData(timeFormatName).first())
    suspend fun getTheme(): Theme = Theme.valueOf(getDataStoreData(themeName).first())
    suspend fun getNotificationAllowed(): Boolean = getDataStoreData(notificationAllowedName).first().toBoolean()
}