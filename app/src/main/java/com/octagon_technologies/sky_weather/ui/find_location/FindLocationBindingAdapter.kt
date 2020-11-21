package com.octagon_technologies.sky_weather.ui.find_location

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Job

fun RecyclerView.getFavouriteLocations(
    theme: Theme?,
    arrayOfLocationItem: ArrayList<LocationItem>?,
    groupAdapter: GroupAdapter<GroupieViewHolder>?,
    lambda: (EachSearchResultItem) -> Job
) {
    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter

    arrayOfLocationItem?.forEach {
        groupAdapter?.add(
            EachSearchResultItem(
                theme = theme,
                isLikedByUser = true,
                locationItem = it,
                lambda = lambda
            )
        )
    }
}

fun RecyclerView.getRecentLocations(
    theme: Theme?,
    arrayOfLocationItem: ArrayList<LocationItem>?,
    groupAdapter: GroupAdapter<GroupieViewHolder>?,
    lambda: (EachSearchResultItem) -> Job
) {
    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter

    arrayOfLocationItem?.forEach {
        groupAdapter?.add(
            EachSearchResultItem(
                theme = theme,
                isLikedByUser = true,
                locationItem = it,
                lambda = lambda
            )
        )
    }
}

