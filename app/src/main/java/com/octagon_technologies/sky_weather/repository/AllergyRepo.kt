package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.database.AllergyDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.AllergyForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.allergy_forecast.Allergy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

// TODO - For ALL REPOs - Check the Retrofit and Room  implementations to remove withContext(Dispatchers.IO) - They have inbuilt support
// TODO - can simplify the return type by - Remove the "withContext(Dispatchers.IO)" and then assign the db call to the func using equals - also remove the return type
// TODO - Replace on other repos as well
object AllergyRepo {

    suspend fun getAllergyValueAsync(
        weatherDataBase: WeatherDataBase?,
        coordinates: Coordinates
    ): Allergy? {
        return withContext(Dispatchers.IO) {
            try {
                val remoteAllergyForecast =
                    AllergyForecastRetrofitItem.allergyRetrofitService.getAllergyAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon
                    )

                insertAllergyForecastToLocalStorage(weatherDataBase, remoteAllergyForecast)

                remoteAllergyForecast
            } catch (e: HttpException) {
                Timber.e(e)
                getLocalAllergyForecastAsync(weatherDataBase)
            } catch (e: UnknownHostException) {
                Timber.e(e)
                getLocalAllergyForecastAsync(weatherDataBase)
            }
        }
    }

    private suspend fun insertAllergyForecastToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        allergyForecast: Allergy
    ) {
        withContext(Dispatchers.IO) {
            val allergyForecastDatabaseClass =
                AllergyDatabaseClass(allergyForecast = allergyForecast)
            weatherDataBase?.allergyForecastDao?.insertAllergyForecastDatabaseClass(
                allergyForecastDatabaseClass
            )
        }
    }

    // TODO - Don't return nullable types
    // TODO - No need to have the Async suffix on your method names
    private suspend fun getLocalAllergyForecastAsync(weatherDataBase: WeatherDataBase?) =
            weatherDataBase?.allergyForecastDao?.getAllergyForecastDatabaseClass()?.allergyForecast

}