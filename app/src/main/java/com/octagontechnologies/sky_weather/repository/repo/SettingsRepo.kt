package com.octagontechnologies.sky_weather.repository.repo

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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


    private val unitsKey = preferencesKey<String>(unitsName)
    private val windDirectionKey = preferencesKey<String>(windDirectionName)
    private val timeFormatKey = preferencesKey<String>(timeFormatName)
    private val themeKey = preferencesKey<String>(themeName)
    private val notificationAllowedKey = preferencesKey<Boolean>(notificationAllowedName)


    val units = getDataStoreData(unitsName).map { Units.valueOf(it) }.asLiveData()
    val windDirectionUnits =
        getDataStoreData(windDirectionName).map { WindDirectionUnits.valueOf(it) }.asLiveData()
    val timeFormat = getDataStoreData(timeFormatName).map { TimeFormat.valueOf(it) }.asLiveData()
    val theme: LiveData<Theme> = getDataStoreData(themeName).map { Theme.valueOf(it) }.asLiveData()


    @OptIn(DelicateCoroutinesApi::class)
    val isNotificationAllowed =
        getDataStoreData(notificationAllowedName).map { it.toBooleanStrict() }
            .stateIn(GlobalScope, SharingStarted.Eagerly, false)


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
                unitsName -> it[unitsKey] ?: Units.getDefault().toString()
                windDirectionName -> it[windDirectionKey] ?: WindDirectionUnits.getDefault().toString()
                timeFormatName -> it[timeFormatKey] ?: TimeFormat.getDefault().toString()
                themeName -> it[themeKey] ?: Theme.getDefault().toString()

                notificationAllowedName -> it[notificationAllowedKey]?.toString() ?: "false"

                else -> throw RuntimeException("Unexpected parameter. preferencesName is $preferencesName")
            }
        }
    }
}