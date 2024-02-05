package com.octagon_technologies.sky_weather.repository.repo

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.octagon_technologies.sky_weather.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    suspend fun changeIsNotificationAllowed(isNotificationAllowed: Boolean) {
        dataStore.edit { it[notificationAllowedKey] = isNotificationAllowed }
    }
    suspend fun changeUnits(units: Units) { dataStore.edit { it[unitsKey] = units.value } }
    suspend fun changeWindDirectionUnits(windDirectionUnits: WindDirectionUnits)
    { dataStore.edit { it[windDirectionKey] = windDirectionUnits.name } }
    suspend fun changeTimeFormat(timeFormat: TimeFormat) { dataStore.edit { it[timeFormatKey] = timeFormat.name } }
    suspend fun changeTheme(theme: Theme) { dataStore.edit { it[themeKey] = theme.name } }

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

    val units = getDataStoreData(unitsName).map { Units.valueOf(it) }.asLiveData()
    val windDirectionUnits = getDataStoreData(windDirectionName).map { WindDirectionUnits.valueOf(it) }.asLiveData()
    val timeFormat = getDataStoreData(timeFormatName).map{ TimeFormat.valueOf(it) }.asLiveData()
    val theme: LiveData<Theme> = getDataStoreData(themeName).map { Theme.valueOf(it) }.asLiveData()

    val isNotificationAllowed = getDataStoreData(notificationAllowedName).map { it.toBoolean() }.asLiveData()
}