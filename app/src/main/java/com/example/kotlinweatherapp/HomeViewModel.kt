package com.example.kotlinweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.network.All
import com.example.kotlinweatherapp.network.WeatherItem
import com.example.kotlinweatherapp.database.DataClass
import com.example.kotlinweatherapp.database.DatabaseDao
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.UnknownHostException

var cityName = "ChelixCity"

enum class Status{LOADING, NO_INTERNET, LOCATION_ERROR, DONE}

class HomeViewModel(private val dataSource: DatabaseDao): ViewModel() {
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


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            getData()
            getProperties()
        }
        Log.i("ViewModelReal", "mainText value is ${mainText.value} and init called")
        Log.i("ViewModelReal", "livecityname value is ${liveCityName.value} and init called")
    }

    fun insert(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val data = DataClass(name_text = mainText.value!!)
                dataSource.insertString(data)
            }
        }
    }

    fun getData(){
        uiScope.launch {
            mainText.value = suspendGetData()
            cityName = mainText.value!!
        }
    }

    private suspend fun suspendGetData(): String{
        return withContext(Dispatchers.IO){
            dataSource.getFormerString()?.name_text ?: "Hello"
        }
    }

    fun getProperties() {
        uiScope.launch {

            _status.value = Status.LOADING
            Log.i("ViewModelReal", "cityName is $cityName")
            val getDeferredProperties = WeatherItem.retrofitService.getWeatherAsync(name = cityName)

            try {
                val listProperties = getDeferredProperties.await()
                _doesErrorExist.value = false
                _all.value = listProperties.list
                _status.value = Status.DONE

                liveCityName.value = listProperties.city.name
                Log.i("ViewModelReal", "liveCityName value = ${liveCityName.value} and listProperties.city.name value = ${listProperties.city.name}")
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
}
/*
java.net.UnknownHostException: Unable to resolve host "api.openweathermap.org": No address associated with hostname
2020-06-30 11:06:49.037 5659-5659/com.example.settingsapp E/ViewModelReal: retrofit2.HttpException: HTTP 404 Not Found
 */