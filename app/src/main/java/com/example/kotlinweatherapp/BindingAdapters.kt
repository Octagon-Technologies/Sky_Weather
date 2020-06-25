package com.example.kotlinweatherapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.All
import com.example.kotlinweatherapp.network.Main
import com.example.kotlinweatherapp.network.Weather
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<All>?){
    val adapter = recyclerView.adapter as RecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("getDay")
fun TextView.getDay(timeLong: Long){
    val weekDayString = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault()).format(timeLong)

    val today = Date().time

    if (timeLong in today..today.plus(86_400_000) || timeLong in today.minus(86_400_000)..today ) {
        text = context.getString(R.string.today)
    }

    text = weekDayString
}

//@BindingAdapter("setWeatherStatus")
//fun TextView.setWeatherStatus(weather: Weather){
//    text = weather.description
//}

@BindingAdapter("setMaxTemp")
fun TextView.setMaxTemp(main: Main){
    text = main.temp_max.toString()
}

@BindingAdapter("setMinTemp")
fun TextView.setMinTemp(main: Main){
    text = main.temp_min.toString()
}
//
//@BindingAdapter("setWeatherImage")
//fun ImageView.setWeatherImage(weather: Weather){
//    setImageResource(when(weather.description){
//        "clear sky" -> R.drawable.sunny
//        "light rain" -> R.drawable.scattered_showers
//        else -> R.drawable.partly_cloudy
//    })
//}

// broken clouds
// scattered clouds
// few clouds
// clear sky
// light rain
// overcast clouds