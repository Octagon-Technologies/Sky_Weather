package com.example.kotlinweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.network.WeatherDataClass
import com.example.kotlinweatherapp.network.WeatherItem
import kotlinx.coroutines.*

class FirstViewModel: ViewModel() {
    val viewModelJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _property = MutableLiveData<WeatherDataClass>()
    val property: LiveData<WeatherDataClass>
        get() = _property

    private val _temp = MutableLiveData<String>()
    val temp: LiveData<String>
        get() = _temp

    init{
        getPropertyValue()
    }

    private fun getPropertyValue(){
        scope.launch{
            val getDeferredProperties = WeatherItem.retrofitService.getWeather()

            try {
                val listOfProperties = getDeferredProperties.await()
                _temp.value = listOfProperties.main.temp.toString()
                Log.i("ViewModel", "Value of _temp.value is ${_temp.value}")
            }
            catch (t: Throwable){
                _temp.value = "10"
                Log.e("ViewModel", "$t")
            }
        }
    }
}