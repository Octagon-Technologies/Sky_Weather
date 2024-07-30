package com.octagontechnologies.sky_weather.ui.find_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.find_location.screen.FindLocationScreen
import com.octagontechnologies.sky_weather.utils.removeToolbarAndBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindLocationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
                    Column(Modifier.padding(paddingValues)) {
                        FindLocationScreen(
                            navController = findNavController(),
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        removeToolbarAndBottomNav()
    }
}

//    private lateinit var binding: FindLocationFragmentBinding
//    private val viewModel by viewModels<FindLocationViewModel>()
//    private val mainActivity by lazy { activity as? MainActivity }
////    private val widgetConfigureActivity by lazy { activity as? WidgetConfigureActivity }
////    private val liveLocation by lazy {
////        mainActivity?.liveLocation ?: widgetConfigureActivity?.viewModel?.reverseGeoCodingLocation
////    }
//
//    private val favouriteGroupAdapter = GroupAdapter<GroupieViewHolder>()
//    private val recentGroupAdapter = GroupAdapter<GroupieViewHolder>()
//
//    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FindLocationFragmentBinding.inflate(inflater)
//
//        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
//
//
//        setUpFavouriteRecyclerView()
//        setUpRecentRecyclerView()
//        setUpTheme()
//        setOnClickListeners()
//
////        checkPermissions()
//
//
//        listOf(favouriteGroupAdapter, recentGroupAdapter).forEach { adapter ->
//            adapter.setOnItemClickListener { item, _ ->
//                (item as EachSearchResultItem).location.apply {
//                    onLocationSelected()
//                }
//            }
//        }
//
//        viewModel.navigateHome.observe(viewLifecycleOwner) { navigateHome ->
//            if (navigateHome == true) {
//                popBackStack()
//                viewModel.onNavigateHomeDone()
//            }
//        }
//
//        viewModel.location.observe(viewLifecycleOwner) { location ->
//            if (location?.isGps == true) {
//                binding.gpsLocationLayout.visibility = View.VISIBLE
//                binding.enableLocationLayout.visibility = View.GONE
//
//                binding.gpsLocationCity.text = location.displayNameWithoutCountryCode
//                binding.gpsLocationCountry.text = location.country
//            } else {
//                viewModel.setIsLoadingAsFalse()
//            }
//        }
//
//        return binding.root
//    }
//
//    private fun Location.onLocationSelected() {
//        val mainActivity = activity as? MainActivity
//        if (mainActivity != null)
//            viewModel.setNewLocation(this)
//
//        popBackStack()
//    }
//
//    private fun setUpRecentRecyclerView() {
//        binding.recentRecyclerView.adapter = recentGroupAdapter
//
//
//        viewModel.recentLocationsList.observe(viewLifecycleOwner) { recentLocationsList ->
//            if (recentLocationsList != null) {
////                recentGroupAdapter.clear()
//
//                recentGroupAdapter.updateAsync(
//                    recentLocationsList.map { location ->
//                        EachSearchResultItem(
//                            theme = viewModel.theme,
//                            isLikedByUser = true,
//                            location = location,
//                            onLocationSelected =  { location.onLocationSelected() },
//                            onChangeFavoriteStatus = { removeFromRecent(location) }
//                        )
//                    }
//                )
//                checkIfRecentListIsEmpty()
//            }
//        }
//    }
//
//    fun setUpFavouriteRecyclerView() {
//        binding.favouriteRecyclerView.adapter = favouriteGroupAdapter
//
//        viewModel.favouriteLocationsList.observe(viewLifecycleOwner) { favouriteLocationsList ->
//            if (favouriteLocationsList != null) {
//                favouriteGroupAdapter.updateAsync(
//                    favouriteLocationsList.map { location ->
//                        EachSearchResultItem(
//                            theme = viewModel.theme,
//                            isLikedByUser = true,
//                            location = location,
//                            onLocationSelected =  { location.onLocationSelected() },
//                            onChangeFavoriteStatus = { removeFromFavourites(location) }
//                        )
//                    }
//                )
//            }
//            checkIfFavouriteListIsEmpty()
//        }
//    }
//
//    private fun setUpTheme() {
//        viewModel.theme.observe(viewLifecycleOwner) { theme ->
//            if (theme == Theme.LIGHT) {
//                removeToolbarAndBottomNav()
//            } else {
//                removeToolbarAndBottomNav()
//            }
//            changeSystemNavigationBarColor(
//                if (theme == Theme.LIGHT) android.R.color.white
//                else R.color.dark_black
//            )
//        }
//    }
//
//    private fun checkIfFavouriteListIsEmpty() {
//        binding.recentPlainText.layoutParams =
//            (binding.recentPlainText.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                topMargin = resources.getDimensionPixelSize(
//                    if (favouriteGroupAdapter.itemCount <= 0) R.dimen._60sdp else R.dimen._30sdp
//                )
//            }
//        binding.emptyFavouriteListText.visibility =
//            if (favouriteGroupAdapter.itemCount <= 0) View.VISIBLE else View.GONE
//    }
//
//    private fun checkIfRecentListIsEmpty() {
//        binding.emptyRecentListText.visibility =
//            if (recentGroupAdapter.itemCount <= 0) View.VISIBLE else View.GONE
//    }
//
//    private fun setOnClickListeners() {
//        binding.closeBtn.setOnClickListener {
//            mainActivity?.also {
//                viewModel.location.value?.let {
//                    findNavController().popBackStack()
//                } ?: run {
//                    showShortToast(
//                        getStringResource(R.string.location_is_needed_to_get_weather_forecast)
//                    )
//                    mainActivity?.finish()
//                }
//            } ?: run {
////                widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
//            }
//        }
//        binding.searchQuery.setOnClickListener {
//            findNavController().navigate(
//                if (activity is MainActivity) R.id.searchLocationFragment
//                else R.id.widgetSearchLocationFragment
//            )
//        }
//        binding.enableLocationLayout.setOnClickListener {
//            viewModel.checkIfPermissionIsGranted(requireContext())
//        }
//
//        binding.gpsLocationLayout.setOnClickListener {
//            viewModel.useAndSaveGPSLocation(requireContext())
//            showLongToast("Fetching current location")
//
//            if (viewModel.location.value != null)
//                popBackStack()
//        }
//    }
//
//    private fun popBackStack() {
//        if (!mainActivity.isNull()) {
//            findNavController().popBackStack(R.id.currentForecastFragment, false)
//        } else {
////            widgetConfigureActivity?.viewModel?.navigateToLocationFragment?.value = false
//        }
//    }
//
//    private val removeFromRecent = { location: Location ->
//        viewModel.removeFromRecent(location)
//        recentGroupAdapter.apply {
////            remove(eachSearchResultItem)
//            // removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
////            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
//            checkIfRecentListIsEmpty()
//        }
//
//        Unit
//    }
//
//    private val removeFromFavourites = { location: Location ->
//        viewModel.removeFromFavourites(location)
//        favouriteGroupAdapter.apply {
////            removeGroupAtAdapterPosition(eachSearchResultItem.actualPosition)
////            (0 until itemCount).forEach { getItem(it).notifyChanged(it) }
//            checkIfFavouriteListIsEmpty()
//        }
//
//        Unit
//    }
//}
