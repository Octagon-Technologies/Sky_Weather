package com.octagon_technologies.sky_weather.ui.current_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.CurrentForecastFragmentBinding
import com.octagon_technologies.sky_weather.notification.CustomNotificationCompat
import com.octagon_technologies.sky_weather.ui.hourly_forecast.HourlyForecastViewModel
import com.octagon_technologies.sky_weather.ui.hourly_forecast.HourlyForecastViewModelFactory

class CurrentForecastFragment : Fragment() {

    private lateinit var binding: CurrentForecastFragmentBinding
    private val viewModel by viewModels<CurrentForecastViewModel> { CurrentForecastViewModelFactory(requireContext()) }
    private val hourlyForecastViewModel by viewModels<HourlyForecastViewModel>{  HourlyForecastViewModelFactory(requireContext()) }
    private val mainActivity by lazy { (activity as MainActivity) }
    private val customNotificationCompat by lazy { CustomNotificationCompat(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentForecastFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.units = mainActivity.liveUnits
            it.windDirectionUnits = mainActivity.liveWindDirectionUnits
            it.timeFormat = mainActivity.liveTimeFormat
            it.hourlyForecastViewModel = hourlyForecastViewModel
        }

        viewModel.singleForecast.observe(viewLifecycleOwner) {
            if (!mainActivity.hasNotificationChanged && mainActivity.liveNotificationAllowed.value!!) {
                customNotificationCompat.createNotification(
                    it,
                    mainActivity.liveLocation.value,
                    mainActivity.liveTimeFormat.value
                )
                mainActivity.hasNotificationChanged = true
            }
        }

        viewModel.statusCode.observe(viewLifecycleOwner) {
            val message = when(it ?: return@observe) {
                StatusCode.Success -> return@observe
                StatusCode.NoNetwork -> getStringResource(R.string.no_network_availble_plain_text)
                StatusCode.ApiLimitExceeded -> getStringResource(R.string.api_limit_exceeded_plain_text)
            }

            showLongToast(message)
        }

        binding.swipeToRefreshLayout.setOnRefreshListener {
            viewModel.getLocalLocation(mainActivity.liveUnits.value, mainActivity.liveLocation.value)
            hourlyForecastViewModel.getHourlyForecastAsync(mainActivity.liveLocation.value, mainActivity.liveUnits.value, false)
        }

        hourlyForecastViewModel.hourlyForecast.observe(viewLifecycleOwner, {
            binding.swipeToRefreshLayout.isRefreshing = false
        })

        mainActivity.liveLocation.observe(viewLifecycleOwner, {
            it?.also {
                viewModel.getLocalLocation(mainActivity.liveUnits.value, it)
                hourlyForecastViewModel.getHourlyForecastAsync(it, mainActivity.liveUnits.value, false)
            }
        })

        binding.seeMoreBtn.setOnClickListener {
            findNavController().navigate(
                CurrentForecastFragmentDirections.actionCurrentForecastFragmentToSeeMoreFragment(
                    mainActivity.singleForecastJsonAdapter
                        .toJson(viewModel.singleForecast.value) ?: return@setOnClickListener
                )
            )
        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        mainActivity.liveTheme.observe(viewLifecycleOwner) {
            addToolbarAndBottomNav(it)
        }
    }
}