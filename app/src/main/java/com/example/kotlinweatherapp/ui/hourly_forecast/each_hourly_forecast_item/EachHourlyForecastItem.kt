package com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item

import android.widget.TextView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.EachHourlyForecastItemBinding
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem

class EachHourlyForecastItem(val eachHourlyForecast: EachHourlyForecast): BindableItem<EachHourlyForecastItemBinding>() {
    override fun bind(binding: EachHourlyForecastItemBinding, position: Int) {
        binding.eachHourlyForecast = eachHourlyForecast
    }

    override fun getLayout(): Int = R.layout.each_hourly_forecast_item
}

class EachDayTextItem(private val dayString: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.day_text).text = dayString
    }

    override fun getLayout(): Int = R.layout.each_day_text_item
}