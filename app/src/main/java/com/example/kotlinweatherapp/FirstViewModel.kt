package com.example.kotlinweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.network.All
import com.example.kotlinweatherapp.network.WeatherDataClass
import com.example.kotlinweatherapp.network.WeatherItem
import kotlinx.coroutines.*

class FirstViewModel: ViewModel() {
    private val viewModelJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _all = MutableLiveData<List<All>>()
    val all: LiveData<List<All>>
        get() = _all

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
                _all.value = listOfProperties.list
                Log.i("RecyclerViewModel", "${all.value?.size}")

                _temp.value = listOfProperties.city.population.toString()
                Log.i("ViewModel", "Value of _temp.value is ${_temp.value}")
            }
            catch (t: Throwable){
                _temp.value = "10 chars"
                Log.e("ViewModel", "$t")
            }
        }
    }
}