package com.octagon_technologies.sky_weather.ui.find_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.FindLocationFragmentBinding
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.ui.search_location.toReverseGeoCodingLocation
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class FindLocationFragment : Fragment() {

    private lateinit var binding: FindLocationFragmentBinding
    private val viewModel by viewModels<FindLocationViewModel> {
        FindLocationViewModelFactory(
            requireContext()
        )
    }
    private val theme by lazy {
        (activity as MainActivity).liveTheme.value
    }

    private val favouriteGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val recentGroupAdapter = GroupAdapter<GroupieViewHolder>()

    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FindLocationFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.theme = theme

        viewModel.favouriteLocationsList.observe(viewLifecycleOwner, {
            favouriteGroupAdapter.clear()

            binding.favouriteRecyclerView.getFavouriteLocations(
                theme, it, favouriteGroupAdapter, removeFromFavourites
            )
            checkIfFavouriteListIsEmpty()
        })

        viewModel.recentLocationsList.observe(viewLifecycleOwner, {
            recentGroupAdapter.clear()
            binding.recentRecyclerView.getRecentLocations(
                theme, it, recentGroupAdapter, removeFromRecent
            )
            checkIfRecentListIsEmpty()
        })

        setOnClickListeners()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.turnOnCurrentLocation()
        }

        listOf(favouriteGroupAdapter, recentGroupAdapter).forEach {
            it.setOnItemClickListener { item, _ ->
                (item as EachSearchResultItem).locationItem.apply {
                    val reverseGeoCodingLocation = toReverseGeoCodingLocation().apply {
                        viewModel.editLocationInDatabase(activity, this)
                    }
                    (activity as MainActivity).liveLocation.value = reverseGeoCodingLocation
                    findNavController().popBackStack(R.id.currentForecastFragment, false)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.enableLocationText.text =
                if (it!!) resources.getString(R.string.loading_plain_text) else resources.getString(
                    R.string.enable_location_services_plain_text
                )
        })

        viewModel.reversedGeoCodingLocation.observe(viewLifecycleOwner, {
            it.reverseGeoCodingAddress?.apply {
                binding.gpsLocationLayout.visibility = View.VISIBLE
                binding.enableLocationLayout.visibility = View.GONE

                binding.gpsLocationCity.text =
                    if (suburb == null || city == null) it.getDisplayLocation() else "${suburb}, $city"
                binding.gpsLocationCountry.text = country
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        removeToolbarAndBottomNav(if (theme == Theme.LIGHT) R.color.line_grey else R.color.color_black)
    }

    private fun checkIfFavouriteListIsEmpty() {
        binding.recentPlainText.layoutParams =
            (binding.recentPlainText.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin = resources.getDimensionPixelSize(
                    if (favouriteGroupAdapter.itemCount <= 0) R.dimen._60sdp else R.dimen._30sdp
                )
            }
        binding.emptyFavouriteListText.visibility =
            if (favouriteGroupAdapter.itemCount <= 0) View.VISIBLE else View.GONE
    }

    private fun checkIfRecentListIsEmpty() {
        binding.emptyRecentListText.visibility =
            if (recentGroupAdapter.itemCount <= 0) View.VISIBLE else View.GONE
    }

    private fun setOnClickListeners() {
        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchQuery.setOnClickListener { findNavController().navigate(R.id.action_findLocationFragment_to_searchLocationFragment) }
        binding.enableLocationLayout.setOnClickListener { viewModel.checkIfPermissionIsGranted() }

        binding.gpsLocationLayout.setOnClickListener {
            viewModel.addCurrentLocationToDatabase(activity)
            viewModel.reversedGeoCodingLocation.value?.let {
                (activity as MainActivity).liveLocation.value = it
                findNavController().popBackStack(R.id.currentForecastFragment, false)
            }
        }
    }

    private val removeFromRecent = { eachSearchResultItem: EachSearchResultItem ->
        viewModel.removeFromRecent(eachSearchResultItem.locationItem)
        recentGroupAdapter.apply {
            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfRecentListIsEmpty()
        }

        Unit
    }

    private val removeFromFavourites = { eachSearchResultItem: EachSearchResultItem ->
        viewModel.removeFromFavourites(eachSearchResultItem.locationItem)
        favouriteGroupAdapter.apply {
            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfFavouriteListIsEmpty()
        }

        Unit
    }
}
