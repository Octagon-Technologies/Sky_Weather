package com.example.kotlinweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.CurrentDatabaseClass
import com.example.kotlinweatherapp.network.futureforecast.All
import com.example.kotlinweatherapp.network.FutureWeatherItem
import com.example.kotlinweatherapp.database.DataClass
import com.example.kotlinweatherapp.database.FutureDatabaseClass
import com.example.kotlinweatherapp.database.WeatherDataBase
import com.example.kotlinweatherapp.network.CurrentWeatherItem
import com.example.kotlinweatherapp.network.currentweather.CurrentWeatherDataClass
import com.example.kotlinweatherapp.network.futureforecast.FutureWeatherDataClass
import com.example.kotlinweatherapp.repository.WeatherRepo
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.UnknownHostException

var cityName = "Hello"

enum class Status{LOADING, NO_INTERNET, LOCATION_ERROR, DONE}

class HomeViewModel(
    private val weatherDataBase: WeatherDataBase
): ViewModel() {
   private var _futureListProperties = MutableLiveData<FutureWeatherDataClass?>()
    val futureListProperties: LiveData<FutureWeatherDataClass?>
        get() = _futureListProperties

    var mainText = MutableLiveData<String>()
    var liveCityName = MutableLiveData<String>()

    private var _doesErrorExist = MutableLiveData<Boolean>()
    val doesErrorExist: LiveData<Boolean>
        get() = _doesErrorExist

    private var _all = MutableLiveData<List<All>>()
    val all: LiveData<List<All>>
        get() = _all

    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var _globalAll = MutableLiveData<All>()
    val globalAll: LiveData<All>
        get() = _globalAll

    var useDeviceLocation = MutableLiveData<Boolean>()

    private var _currentWeatherInstance = MutableLiveData<CurrentWeatherDataClass>()
    val currentWeatherInstance: LiveData<CurrentWeatherDataClass>
        get() = _currentWeatherInstance

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val repo = WeatherRepo(weatherDataBase)



    fun insertCityName(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val data = DataClass(name_text = mainText.value, useDeviceLocation = useDeviceLocation.value)
                weatherDataBase.databaseDao.insertString(data)
            }
        }
    }

    private fun getData(){
        uiScope.launch {
            mainText.value = suspendGetData()
            cityName = mainText.value!!
        }
    }

    private suspend fun suspendGetData(): String{
        return withContext(Dispatchers.IO){
            weatherDataBase.databaseDao.getFormerString()?.name_text ?: "Hello"
        }
    }

    fun setGlobalAll(all: All){
        _globalAll.value = all
    }

    fun doneNavigateToEachWeather(){
        _globalAll.value = null
    }

    fun getFutureProperties() {
        uiScope.launch {

            repo.refreshData()

            _status.value = Status.LOADING
            Log.i("ViewModelReal", "cityName is $cityName")

            try {
                _futureListProperties.value = repo.futureWeather?.future_weather

                Log.i("ViewModel", "Add futureDataClass to Room successfully")

                _doesErrorExist.value = false
                _all.value = _futureListProperties.value?.list
                _status.value = Status.DONE

                liveCityName.value = futureListProperties.value?.city?.name
                Log.i("ViewModelReal", "liveCityName value = ${liveCityName.value} and listProperties.city.name value = ${futureListProperties.value?.city?.name}")
            }
            catch (noInternetError: UnknownHostException){
                Log.e("ViewModelReal", "$noInternetError")
                liveCityName.value = "$noInternetError"
                _doesErrorExist.value = true
                _status.value = Status.NO_INTERNET

            }
            catch (locationError: HttpException){
                _doesErrorExist.value = true
                _status.value = Status.LOCATION_ERROR
            }
        }
    }

    fun getCurrentProperties(){
        uiScope.launch {
            repo.refreshData()

            _status.value = Status.LOADING

            try{
                _currentWeatherInstance.value = repo.currentWeather?.current_weather
                liveCityName.value = _currentWeatherInstance.value?.sys?.country
                Log.i("ViewModel", "Add currentDataClass to Room successfully")
                _status.value = Status.DONE
                Log.i("ViewModelReal", "currentLiveCityName value = ${liveCityName.value}")
            }
            catch (noInternetError: UnknownHostException){
                _status.value = Status.NO_INTERNET

            }
            catch (locationError: HttpException){
                _status.value = Status.LOCATION_ERROR
            }
            catch(t: Throwable){
                Log.e("ViewModelCurrent", "$t")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
/*
java.net.UnknownHostException: Unable to resolve host "api.openweathermap.org": No address associated with hostname
2020-06-30 11:06:49.037 5659-5659/com.example.settingsapp E/ViewModelReal: retrofit2.HttpException: HTTP 404 Not Found
 */