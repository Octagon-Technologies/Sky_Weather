package com.example.kotlinweatherapp.repository

import android.util.Log
import com.example.kotlinweatherapp.cityName
import com.example.kotlinweatherapp.database.*
import com.example.kotlinweatherapp.network.CurrentWeatherItem
import com.example.kotlinweatherapp.network.FutureWeatherItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepo(private val weatherDataBase: WeatherDataBase) {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    var currentWeather = localGetCurrentDataClass()
    var futureWeather = localGetFutureDataClass()

    fun localGetCurrentDataClass(): CurrentDatabaseClass? {
        var localCurrentDatabaseClass: CurrentDatabaseClass? = null

        uiScope.launch {
                localCurrentDatabaseClass = suspendlocalGetCurrentDataClass()
        }

        return localCurrentDatabaseClass
    }

    suspend fun suspendlocalGetCurrentDataClass(): CurrentDatabaseClass {
        return withContext(Dispatchers.IO) {
            weatherDataBase.currentDao.getCurrentDataClass(CURRENT_WEATHER_VALUE)
        }
    }

    fun localGetFutureDataClass(): FutureDatabaseClass? {
        var localFutureDataClass: FutureDatabaseClass? = null

        uiScope.launch {
            localFutureDataClass = suspendlocalGetFutureDataClass()
        }

        return localFutureDataClass
    }

    suspend fun suspendlocalGetFutureDataClass(): FutureDatabaseClass {
        return withContext(Dispatchers.IO){ weatherDataBase.futureDao.getFutureDataClass(
            FUTURE_WEATHER_VALUE) }
    }

    suspend fun refreshData(){
        withContext(Dispatchers.IO) {
            cityName = weatherDataBase.databaseDao.getFormerString()?.name_text.toString()
            Log.i("WeatherRepo", "cityName is $cityName")
            val getDeferredFutureProperties =
                FutureWeatherItem.futureRetrofitService.getFutureWeatherAsync(name = cityName)
            val getDeferredCurrentProperties =
                CurrentWeatherItem.currentRetrofitService.getCurrentWeatherAsync(name = cityName)

            try {
                val futureListProperties = getDeferredFutureProperties.await()
                val futureDatabaseClass = FutureDatabaseClass(future_weather = futureListProperties)
                weatherDataBase.futureDao.insertFutureAllClass(futureDatabaseClass)
                Log.i("WeatherRepo", "Value of futureListProperties city is ${futureDatabaseClass.future_weather.city.name}")
                Log.i("WeatherRepo", "Inserted FutureAllClass in database")

                val currentListProperties = getDeferredCurrentProperties.await()
                val currentDatabaseClass = CurrentDatabaseClass(current_weather = currentListProperties)
                weatherDataBase.currentDao.insertCurrentDataClass(currentDatabaseClass)
                Log.i("WeatherRepo", "Value of currentListProperties city is ${currentDatabaseClass.current_weather.name}")
                Log.i("WeatherRepo", "Inserted CurrentDataClass in database")

            } catch (t: Throwable) {
                Log.i("WeatherRepo", "$t")
            }
            finally{
                currentWeather = weatherDataBase.currentDao.getCurrentDataClass(
                    CURRENT_WEATHER_VALUE)
                futureWeather = weatherDataBase.futureDao.getFutureDataClass(FUTURE_WEATHER_VALUE)

                Log.i("WeatherRepo", "Value of current city is ${currentWeather?.current_weather?.name}")
                Log.i("WeatherRepo", "Value of future city is ${futureWeather?.future_weather?.city?.name }")
            }
        }
    }


}

//    suspend fun getFutureProperties() {
//
//
//            Log.i("ViewModelReal", "cityName is $cityName")
//            val getDeferredFutureProperties = FutureWeatherItem.futureRetrofitService.getFutureWeatherAsync(name = cityName)
//
//            try {
//                futureListProperties = getDeferredFutureProperties.await()
//
//                val futureDataBase = FutureDatabaseClass(future_weather = futureListProperties)
//                withContext(Dispatchers.IO) {
//                    weatherDataBase.futureDao.insertFutureAllClass(futureDataBase)
//                }
//
//                Log.i("ViewModel", "Add futureDataClass to Room successfully")
//
//                _doesErrorExist.value = false
//                _all.value = futureListProperties.list
//                _status.value = Status.DONE
//
//                liveCityName.value = futureListProperties.city.name
//                Log.i("ViewModelReal", "liveCityName value = ${liveCityName.value} and listProperties.city.name value = ${futureListProperties.city.name}")
//            }
//            catch (noInternetError: UnknownHostException){
//                Log.e("ViewModelReal", "$noInternetError")
//                liveCityName.value = "$noInternetError"
//                _doesErrorExist.value = true
//                _status.value = Status.NO_INTERNET
//
//            }
//            catch (locationError: HttpException){
//                _doesErrorExist.value = true
//                _status.value = Status.LOCATION_ERROR
//            }
//        }
//    }
//
//    fun getCurrentProperties(){
//        uiScope.launch {
//            _status.value = Status.LOADING
//            Log.i("ViewModelCurrent", "cityName is $cityName")
//            val getDeferredFutureProperties =
//                CurrentWeatherItem.currentRetrofitService.getCurrentWeatherAsync(name = cityName)
//
//            try {
//                _currentWeatherInstance.value = getDeferredFutureProperties.await()
//                val currentDatabase = CurrentDatabaseClass(current_weather = _currentWeatherInstance.value!!)
//                withContext(Dispatchers.IO) {
//                    weatherDataBase.currentDao.insertCurrentDataClass(currentDatabase)
//                }
//                Log.i("ViewModel", "Add currentDataClass to Room successfully")
//                _status.value = Status.DONE
//            }
//            catch (noInternetError: UnknownHostException){
//                _status.value = Status.NO_INTERNET
//
//            }
//            catch (locationError: HttpException){
//                _status.value = Status.LOCATION_ERROR
//            }
//            catch(t: Throwable){
//                Log.e("ViewModelCurrent", "Value of cityName is $cityName")
//                Log.e("ViewModelCurrent", "$t")
//            }
//        }
//    }
