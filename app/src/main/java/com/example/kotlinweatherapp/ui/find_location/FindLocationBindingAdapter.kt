package com.example.kotlinweatherapp.ui.find_location

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import timber.log.Timber

//@BindingAdapter("getFavouritesRecyclerViewItems")
//fun RecyclerView.getFavouritesRecyclerViewItems() {
//
//}
//
//@BindingAdapter("getRecentRecyclerViewItems")
//fun RecyclerView.getRecentRecyclerViewItems() {
//
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCurrentLocationCity")
fun TextView.setCurrentLocationCity(reverseGeoCodingLocation: ReverseGeoCodingLocation?) {
    val address = reverseGeoCodingLocation?.address
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


