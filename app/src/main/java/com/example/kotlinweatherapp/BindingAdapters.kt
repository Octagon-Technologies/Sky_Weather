package com.example.kotlinweatherapp

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.*
import java.text.SimpleDateFormat
import java.util.*

// Temps are in Kelvins == Celsius + 273.15

// TODO: Create the option in settings for the user to see the temprature in degrees F°
// TODO: Show cardinal direction based on wind.degrees in the setWindSpeed app module

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<All>?){
    val adapter = recyclerView.adapter as RecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("getDay")
fun TextView.getDay(date: String){
    val weekDayString: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
    val dateInMillis = weekDayString.time

    val newString = SimpleDateFormat("EE, dd MMM, HH:mm", Locale.getDefault()).format(dateInMillis)

    text = newString
}

@BindingAdapter("getEachDayString")
fun TextView.getEachDay(date: String){
    val weekDayString: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date)!!
    val dateInMillis = weekDayString.time

    val newString = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(dateInMillis)

    text = newString
}

@BindingAdapter("setWeatherStatus")
fun TextView.setWeatherStatus(weather: Weather){
    text = weather.description
}

//@BindingAdapter("setMaxTemp")
//fun TextView.setMaxTemp(main: Main){
//    text = (main.temp_max - 273.15).toInt().toString()
//}
//
//@BindingAdapter("setMinTemp")
//fun TextView.setMinTemp(main: Main){
//    text = (main.temp_min - 273.15).toInt().toString()
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTemp")
fun TextView.setTemp(main: Main){
    text = "${(main.temp - 273.15).toInt()}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setHumidity")
fun TextView.setHumidity(main: Main){
    text = "${main.humidity}%"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCloudCover")
fun TextView.setCloudCover(cloudCover: Clouds){
    val cloudPerc = (cloudCover.all * 8 / 100)
    text = if (cloudPerc == 1) "$cloudPerc Okta" else "$cloudPerc Oktas"

}

@SuppressLint("SetTextI18n")
@BindingAdapter("setPressure")
fun TextView.setPressure(main: Main){
    text = "${main.pressure} mBars"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setWindSpeed")
fun TextView.setWindSpeed(windSpeed: Wind){
    val speed = (windSpeed.speed * 1.852).toInt()
    text = "$speed km/h"
}


@SuppressLint("SetTextI18n")
@BindingAdapter("setFeelsLikeTemp")
fun TextView.setFeelsLikeTemp(main: Main){
    text = "Feels like ${(main.temp - 273.15).toInt()}°"
}

@BindingAdapter("setWeatherImage")
fun ImageView.setWeatherImage(weather: Weather){
    setImageResource(when(weather.description){
        "clear sky" -> R.drawable.sunny
        "light rain" -> R.drawable.scattered_showers
        else -> R.drawable.partly_cloudy
    })
}

@BindingAdapter("marsApiImages")
fun imageVisibility(image: ImageView, status: Status?){
    status?.let {
        when (status) {
            Status.LOADING -> {
                image.visibility = View.VISIBLE
                image.setImageResource(R.drawable.loading_animation)
            }
            Status.NO_INTERNET -> {
                image.visibility = View.VISIBLE
                image.setImageResource(R.drawable.ic_connection_error)
            }
            Status.LOCATION_ERROR -> {
                image.visibility = View.VISIBLE
                image.setImageResource(R.drawable.ic_connection_error)
            }
            Status.DONE -> {
                image.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("locationText")
fun locationText(textView: TextView, status: Status?){
    status?.let {
        when(status){
            Status.LOCATION_ERROR -> textView.visibility = View.VISIBLE
            else -> textView.visibility = View.GONE
        }
    }
}

// broken clouds
// scattered clouds
// few clouds
// clear sky
// light rain
// overcast clouds