package com.example.kotlinweatherapp.ui.search_location.each_search_result_item

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.ui.search_location.toReverseGeoCodingLocation
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject.insertFavouriteLocationToLocalStorage
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject.removeFavouriteLocationToLocalStorage
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@BindingAdapter("getSearchResultCityName")
fun TextView.getSearchResultCityName(locationItem: LocationItem?) {
    val finalAddress = locationItem?.displayName
    Timber.d("finalAddress is $finalAddress")
    text = finalAddress
}

@BindingAdapter("addLocationToStorage")
fun ConstraintLayout.addLocationToStorage(locationItem: LocationItem?) {
    val mainDataBase: MainDataBase? by lazy {
        MainDataBase.getInstance(context)
    }
    setOnClickListener {
        locationItem?.apply {
            MainLocationObject.addSelectedLocation(mainDataBase, toReverseGeoCodingLocation())
            findNavController().popBackStack(R.id.currentForecastFragment, false)
        }
    }
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