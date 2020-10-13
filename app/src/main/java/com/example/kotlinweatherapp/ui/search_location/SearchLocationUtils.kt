package com.example.kotlinweatherapp.ui.search_location

import com.example.kotlinweatherapp.network.location.LocationItem

data class EachAdapterLocationItem(var isFavourite: Boolean = false, var locationItem: LocationItem)