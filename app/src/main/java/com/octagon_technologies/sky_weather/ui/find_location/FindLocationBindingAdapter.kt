package com.octagon_technologies.sky_weather.ui.find_location

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

fun RecyclerView.getFavouriteLocations(
    theme: Theme?,
    arrayOfLocation: ArrayList<Location>?,
    groupAdapter: GroupAdapter<GroupieViewHolder>?,
    lambda: (EachSearchResultItem) -> Unit
) {
    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter

    arrayOfLocation?.forEach {
        groupAdapter?.add(
            EachSearchResultItem(
                theme = theme,
                isLikedByUser = true,
                location = it,
                lambda = lambda
            )
        )
    }
}

fun RecyclerView.getRecentLocations(
    theme: Theme?,
    arrayOfLocation: ArrayList<Location>?,
    groupAdapter: GroupAdapter<GroupieViewHolder>?,
    lambda: (EachSearchResultItem) -> Unit
) {
    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter

    arrayOfLocation?.forEach {
        groupAdapter?.add(
            EachSearchResultItem(
                theme = theme,
                isLikedByUser = true,
                location = it,
                lambda = lambda
            )
        )
    }
}

