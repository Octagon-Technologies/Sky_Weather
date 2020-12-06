package com.octagon_technologies.sky_weather.ui.search_location

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

fun RecyclerView.addLocationSuggestionsToRecyclerView(
    theme: Theme?,
    mapOfLocationItems: Map<String?, Location>,
    groupAdapter: GroupAdapter<GroupieViewHolder>,
    favouriteItemsMap: Map<String?, Location>,
    addToFavourite: (EachSearchResultItem) -> Unit
) {
    adapter = groupAdapter
    layoutManager = LinearLayoutManager(context)

    groupAdapter.clear()

        mapOfLocationItems.values.sortedBy { it.displayName }.forEach {
            val eachSearchResultItem = EachSearchResultItem(
                theme = theme,
                isLikedByUser = it in favouriteItemsMap.values,
                location = it,
                lambda = addToFavourite
            )
            groupAdapter.add(eachSearchResultItem)
        }
}
