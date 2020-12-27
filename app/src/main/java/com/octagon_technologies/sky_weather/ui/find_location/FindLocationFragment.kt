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
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.FindLocationFragmentBinding
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.ui.search_location.toReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.utils.*
import com.octagon_technologies.sky_weather.widgets.WidgetConfigureActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class FindLocationFragment : Fragment() {

    private lateinit var binding: FindLocationFragmentBinding
    private val viewModel by viewModels<FindLocationViewModel> {
        FindLocationViewModelFactory(requireContext())
    }
    private val mainActivity by lazy { activity as? MainActivity }
    private val widgetConfigureActivity by lazy { activity as? WidgetConfigureActivity }
    private val theme by lazy { mainActivity?.liveTheme?.value ?: Theme.DARK }
    private val units by lazy { mainActivity?.liveUnits?.value ?: Units.METRIC }
    private val liveLocation by lazy {
        mainActivity?.liveLocation ?: widgetConfigureActivity?.viewModel?.reverseGeoCodingLocation
    }

    private val favouriteGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val recentGroupAdapter = GroupAdapter<GroupieViewHolder>()

    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FindLocationFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.theme = theme
        binding.units = units

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
                (item as EachSearchResultItem).location.apply {
                    val reverseGeoCodingLocation = toReverseGeoCodingLocation().apply {
                        mainActivity?.let {
                            viewModel.editLocationInDatabase(activity, this)
                        }
                    }
                    liveLocation?.value = reverseGeoCodingLocation
                    popBackStack()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.enableLocationText.text =
                if (it!!) resources.getString(R.string.loading_plain_text) else resources.getString(
                    R.string.enable_location_services_plain_text
                )
        })

        viewModel.reversedGeoCodingLocation.observe(viewLifecycleOwner) {
            it?.reverseGeoCodingAddress?.apply {
                binding.gpsLocationLayout.visibility = View.VISIBLE
                binding.enableLocationLayout.visibility = View.GONE

                binding.gpsLocationCity.text = it.getDisplayLocation()
                binding.gpsLocationCountry.text = country
            } ?: run {
                viewModel.setIsLoadingAsFalse()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (theme == Theme.LIGHT) {
            removeToolbarAndBottomNav(R.color.line_grey, false)
        } else {
            removeToolbarAndBottomNav(R.color.color_black, true)
        }
        changeSystemNavigationBarColor(
            if (theme == Theme.LIGHT) android.R.color.white
            else R.color.dark_black
        )
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
            mainActivity?.also {
                liveLocation?.value?.let {
                    findNavController().popBackStack()
                } ?: run {
                    showShortToast(
                        getStringResource(R.string.location_is_needed_to_get_weather_forecast)
                    )
                    mainActivity?.finish()
                }
            } ?: run {
                widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
            }
        }
        binding.searchQuery.setOnClickListener {
            findNavController().navigate(if (activity is MainActivity) R.id.searchLocationFragment else R.id.widgetSearchLocationFragment)
        }
        binding.enableLocationLayout.setOnClickListener { viewModel.checkIfPermissionIsGranted() }

        binding.gpsLocationLayout.setOnClickListener {
            viewModel.addCurrentLocationToDatabase(activity)

            viewModel.reversedGeoCodingLocation.value?.let {
                liveLocation?.value = it
                popBackStack()
            }
        }
    }

    private fun popBackStack() {
        if (!mainActivity.isNull()) {
            findNavController().popBackStack(R.id.currentForecastFragment, false)
        } else {
            widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
        }
    }

    private val removeFromRecent = { eachSearchResultItem: EachSearchResultItem ->
        viewModel.removeFromRecent(eachSearchResultItem.location)
        recentGroupAdapter.apply {
            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfRecentListIsEmpty()
        }

        Unit
    }

    private val removeFromFavourites = { eachSearchResultItem: EachSearchResultItem ->
        viewModel.removeFromFavourites(eachSearchResultItem.location)
        favouriteGroupAdapter.apply {
            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfFavouriteListIsEmpty()
        }

        Unit
    }
}
