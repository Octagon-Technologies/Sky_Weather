package com.example.kotlinweatherapp.ui.find_location

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

@BindingAdapter("getFavouriteLocations")
fun RecyclerView.getFavouriteLocations(arrayOfLocationItem: ArrayList<LocationItem>?) {
    layoutManager = LinearLayoutManager(context)

    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    arrayOfLocationItem?.forEach {
        Timber.d("favouriteLocationItem is $it")
    }
}
//
//@BindingAdapter("getRecentRecyclerViewItems")
//fun RecyclerView.getRecentRecyclerViewItems() {
//
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCurrentLocationCity")
fun TextView.setCurrentLocationCity(reverseGeoCodingLocation: ReverseGeoCodingLocation?) {
    val address = reverseGeoCodingLocation?.reverseGeoCodingAddress
    text = "${address?.residential}, ${address?.city}"
}

@BindingAdapter("findLocationLoadingLayout")
fun LinearLayout.findLocationLoadingLayout(reverseGeoCodingLocation: ReverseGeoCodingLocation?) {
    visibility = View.VISIBLE
    Timber.d("reverseGeoCodingLocation in findLocationLoadingLayout is $reverseGeoCodingLocation")

    reverseGeoCodingLocation?.let {
        visibility = View.GONE
    }
}


