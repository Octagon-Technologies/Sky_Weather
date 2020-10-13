package com.example.kotlinweatherapp.ui.search_location.each_search_result_item

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.location.Address
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject.insertFavouriteLocationToLocalStorage
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject.removeFavouriteLocationToLocalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@BindingAdapter("getSearchResultCityName")
fun TextView.getSearchResultCityName(address: Address?) {
    val finalAddress = "${address?.suburb}, ${address?.city}, ${address?.countryCode?.toUpperCase(Locale.getDefault())}"
    Timber.d("finalAddress is $finalAddress")
    text = finalAddress
}

@BindingAdapter("addToFavouriteIcon")
fun ImageView.addToFavouriteIcon(locationItem: LocationItem) {
    val uiScope: CoroutineScope by lazy {CoroutineScope(Dispatchers.Main) }
    val addImage: Drawable.ConstantState? by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_add_circle_outline_black, null)?.constantState }
    val mainDataBase: MainDataBase? by lazy { MainDataBase.getInstance(context) }

    setOnClickListener {
        uiScope.launch {
                setImageResource(
                        if (drawable.constantState == addImage) {
                            insertFavouriteLocationToLocalStorage(mainDataBase, locationItem)
                            R.drawable.ic_check_circle_black
                        } else {
                            removeFavouriteLocationToLocalStorage(mainDataBase, locationItem)
                            R.drawable.ic_add_circle_outline_black
                        }
                )
        }
    }

}