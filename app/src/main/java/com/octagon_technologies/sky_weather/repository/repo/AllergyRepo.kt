package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.allergy.AllergyDao
import com.octagon_technologies.sky_weather.repository.network.allergy.AllergyApi
import javax.inject.Inject

//TODO: Sign up for an affordable pollen api since GetAmbee have started charging
class AllergyRepo @Inject constructor(
    private val allergyApi: AllergyApi,
    private val allergyDao: AllergyDao
) {

    val allergy = allergyDao.getLocalAllergy().map { it?.allergy }

//    suspend fun setUpRefresh() {
//        locationRepo.location.asFlow().collectLatest { location ->
//            if (location != null)
//                refreshAllergyForecast(location)
//        }
//    }

    suspend fun refreshAllergyForecast(
        location: Location
    ) {
//        val allergyForecastResponse =
//            allergyApi.getAllergyResponse(
//                lat = location.lat.toDouble(),
//                lon = location.lon.toDouble()
//            )
//        allergyDao.insertData(allergyForecastResponse.toLocalAllergy())
    }
}