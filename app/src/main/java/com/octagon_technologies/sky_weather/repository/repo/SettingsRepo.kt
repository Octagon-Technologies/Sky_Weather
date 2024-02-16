package com.octagon_technologies.sky_weather.repository.repo

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    @ApplicationContext private val context: Context
) {

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
    private val isGpsOnName = "is_gps_on"

    private val unitsKey = preferencesKey<String>(unitsName)
    private val windDirectionKey = preferencesKey<String>(windDirectionName)
    private val timeFormatKey = preferencesKey<String>(timeFormatName)
    private val themeKey = preferencesKey<String>(themeName)
    private val notificationAllowedKey = preferencesKey<Boolean>(notificationAllowedName)
    private val isGpsOnKey = preferencesKey<Boolean>(isGpsOnName)

    val units = getDataStoreData(unitsName).map { Units.valueOf(it) }.asLiveData()
    val windDirectionUnits = getDataStoreData(windDirectionName).map { WindDirectionUnits.valueOf(it) }.asLiveData()
    val timeFormat = getDataStoreData(timeFormatName).map{ TimeFormat.valueOf(it) }.asLiveData()
    val theme: LiveData<Theme> = getDataStoreData(themeName).map { Theme.valueOf(it) }.asLiveData()

    val isNotificationAllowed = getDataStoreData(notificationAllowedName).map { it.toBooleanStrict() }
    val isNotificationAllowedFlow = getDataStoreData(notificationAllowedName).map { it.toBooleanStrict() }
    val isGpsOn = getDataStoreData(isGpsOnName).map { it.toBooleanStrict() }


    init {
//        isGpsOn.observeForever { }
//        isNotificationAllowed.observeForever {  }
    }

    suspend fun changeIsGpsOn(isGpsOn: Boolean) {
        dataStore.edit { it[isGpsOnKey] = isGpsOn }
    }

    suspend fun changeIsNotificationAllowed(isNotificationAllowed: Boolean) {
        dataStore.edit { it[notificationAllowedKey] = isNotificationAllowed }
    }

    suspend fun changeUnits(units: Units) {
        dataStore.edit { it[unitsKey] = units.toString() }
    }

    suspend fun changeWindDirectionUnits(windDirectionUnits: WindDirectionUnits) {
        dataStore.edit { it[windDirectionKey] = windDirectionUnits.toString() }
    }

    suspend fun changeTimeFormat(timeFormat: TimeFormat) {
        dataStore.edit { it[timeFormatKey] = timeFormat.toString() }
    }

    suspend fun changeTheme(theme: Theme) {
        dataStore.edit { it[themeKey] = theme.toString() }
    }

    private fun getDataStoreData(preferencesName: String): Flow<String> {
        return dataStore.data.map {
            when (preferencesName) {
                unitsName -> it[unitsKey] ?: Units.METRIC.toString()
                windDirectionName -> it[windDirectionKey] ?: WindDirectionUnits.CARDINAL.toString()
                timeFormatName -> it[timeFormatKey] ?: TimeFormat.FULL_DAY.toString()
                themeName -> it[themeKey] ?: Theme.DARK.toString()
                notificationAllowedName -> it[notificationAllowedKey]?.toString() ?: "false"
                isGpsOnName -> it[isGpsOnKey]?.toString() ?: "false"
                else -> throw RuntimeException("Unexpected parameter. preferencesName is $preferencesName")
            }
        }
    }
}