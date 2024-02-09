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
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.FindLocationFragmentBinding
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.main_activity.MainActivity
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindLocationFragment : Fragment() {

    private lateinit var binding: FindLocationFragmentBinding
    private val viewModel by viewModels<FindLocationViewModel>()
    private val mainActivity by lazy { activity as? MainActivity }
//    private val widgetConfigureActivity by lazy { activity as? WidgetConfigureActivity }
//    private val liveLocation by lazy {
//        mainActivity?.liveLocation ?: widgetConfigureActivity?.viewModel?.reverseGeoCodingLocation
//    }

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


        setUpFavouriteRecyclerView()
        setUpRecentRecyclerView()
        setUpTheme()
        setOnClickListeners()

        checkPermissions()


        listOf(favouriteGroupAdapter, recentGroupAdapter).forEach { adapter ->
            adapter.setOnItemClickListener { item, _ ->
                (item as EachSearchResultItem).location.apply {
                    val mainActivity = activity as? MainActivity
                    if (mainActivity != null)
                        viewModel.setNewLocation(this)

                    popBackStack()
                }
            }
        }


        viewModel.location.observe(viewLifecycleOwner) { location ->
            if (location?.isGps == true) {
                binding.gpsLocationLayout.visibility = View.VISIBLE
                binding.enableLocationLayout.visibility = View.GONE

                binding.gpsLocationCity.text = location.displayNameWithoutCountryCode
                binding.gpsLocationCountry.text = location.country
            } else {
                viewModel.setIsLoadingAsFalse()
            }
        }

        return binding.root
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.useGPSLocation(requireContext())
        }
    }

    private fun setUpRecentRecyclerView() {
        binding.recentRecyclerView.adapter = recentGroupAdapter

        viewModel.recentLocationsList.observe(viewLifecycleOwner) { recentLocationsList ->
            if (recentLocationsList != null) {
                recentGroupAdapter.update(
                    recentLocationsList.map { location ->
                        EachSearchResultItem(
                            theme = viewModel.theme,
                            location = location
                        ) { removeFromRecent(location) }

                    }
                )
                checkIfRecentListIsEmpty()
            }
        }
    }

    fun setUpFavouriteRecyclerView() {
        binding.favouriteRecyclerView.adapter = favouriteGroupAdapter

        viewModel.favouriteLocationsList.observe(viewLifecycleOwner) { favouriteLocationsList ->
            if (favouriteLocationsList != null) {
                favouriteGroupAdapter.update(
                    favouriteLocationsList.map { location ->
                        EachSearchResultItem(
                            theme = viewModel.theme,
                            location = location
                        ) { removeFromFavourites(location) }
                    }
                )
            }
            checkIfFavouriteListIsEmpty()
        }
    }

    private fun setUpTheme() {
        viewModel.theme.observe(viewLifecycleOwner) { theme ->
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
                viewModel.location.value?.let {
                    findNavController().popBackStack()
                } ?: run {
                    showShortToast(
                        getStringResource(R.string.location_is_needed_to_get_weather_forecast)
                    )
                    mainActivity?.finish()
                }
            } ?: run {
//                widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
            }
        }
        binding.searchQuery.setOnClickListener {
            findNavController().navigate(
                if (activity is MainActivity) R.id.searchLocationFragment
                else R.id.widgetSearchLocationFragment
            )
        }
        binding.enableLocationLayout.setOnClickListener {
            viewModel.checkIfPermissionIsGranted(requireContext())
        }

        binding.gpsLocationLayout.setOnClickListener {
            viewModel.useGPSLocation(requireContext())

            if (viewModel.location.value != null)
                popBackStack()
        }
    }

    private fun popBackStack() {
        if (!mainActivity.isNull()) {
            findNavController().popBackStack(R.id.currentForecastFragment, false)
        } else {
//            widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
        }
    }

    private val removeFromRecent = { location: Location ->
        viewModel.removeFromRecent(location)
        recentGroupAdapter.apply {
//            remove(eachSearchResultItem)
            // removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
//            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfRecentListIsEmpty()
        }

        Unit
    }

    private val removeFromFavourites = { location: Location ->
        viewModel.removeFromFavourites(location)
        favouriteGroupAdapter.apply {
//            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
//            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
            checkIfFavouriteListIsEmpty()
        }

        Unit
    }
}
