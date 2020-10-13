package com.example.kotlinweatherapp.ui.search_location

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.database.LocationDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.location.Location
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.ui.find_location.FindLocationViewModel
import com.example.kotlinweatherapp.ui.search_location.each_search_result_item.EachSearchResultItem
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@BindingAdapter("addLocationSuggestionsToRecyclerView")
fun RecyclerView.addLocationSuggestionsToRecyclerView(locationItems: ArrayList<Location>?) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    layoutManager = LinearLayoutManager(context)

    val uiScope = CoroutineScope(Dispatchers.Main)
    val mainDataBase = MainDataBase.getInstance(context)
    val favouriteItemsMap = LinkedHashMap<String?, LocationItem?>()
    val arrayOfEachAdapterLocationItem =  ArrayList<EachAdapterLocationItem>()

    uiScope.launch {
        val arrayOfFavouriteLocationDatabaseClassItems = MainFavouriteLocationsObject.getFavouriteLocationsAsync(mainDataBase)
        arrayOfFavouriteLocationDatabaseClassItems?.forEach { eachFavouriteLocation ->
            eachFavouriteLocation?.let {
                favouriteItemsMap[it.placeId] = it
            }
            Timber.d("eachFavouriteLocation is $eachFavouriteLocation")
        }
    }

    Timber.d("List of location is ${locationItems?.size}")

    locationItems?.forEach {eachLocation ->
        Timber.d("eachLocation is $eachLocation")
        Timber.d("eachLocation.size is ${eachLocation.size}")

        eachLocation.forEach {
            Timber.d("eachLocationItem is $it")
        }
    }

    locationItems?.get(0)?.forEach {
        arrayOfEachAdapterLocationItem.add(EachAdapterLocationItem((it in favouriteItemsMap.values), it))
    }

    arrayOfEachAdapterLocationItem.forEach {
        val eachSearchResultItem = EachSearchResultItem(it)
        groupAdapter.add(eachSearchResultItem)
    }
}

@BindingAdapter("getNavController", "getFindLocationViewModel", "getLifecycleOwner")
fun Button.getCurrentLocation(navController: NavController, findLocationViewModel: FindLocationViewModel, lifecycleOwner: LifecycleOwner) {
    val mainDataBase: MainDataBase? by lazy { MainDataBase.getInstance(context) }
    findLocationViewModel.reversedGeoCodingLocation.observe(lifecycleOwner, {
        Timber.d("Button.getCurrentLocation: reversedGeoLocation is $it")
        val locationDatabaseClass = LocationDatabaseClass(reversedLocation = it)

        CoroutineScope(Dispatchers.IO).launch {
            mainDataBase?.locationDao?.insertLocationDatabaseClass(locationDatabaseClass)
        }
        navController.popBackStack(R.id.currentForecastFragment, false)
    })

    setOnClickListener {
        findLocationViewModel.checkIfPermissionIsGranted()
    }
}

@BindingAdapter("cancelInSearchLocation")
fun Button.cancelInSearchLocation(navController: NavController) {
    setOnClickListener {
        navController.popBackStack()
    }
}