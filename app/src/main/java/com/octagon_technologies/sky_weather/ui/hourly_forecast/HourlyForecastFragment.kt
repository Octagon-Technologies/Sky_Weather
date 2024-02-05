package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagon_technologies.sky_weather.databinding.SelectedHourlyForecastLayoutBinding
import com.octagon_technologies.sky_weather.domain.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.main_activity.MainActivity
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.HeaderMiniHourlyForecast
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.MiniHourlyForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.addToolbarAndBottomNav
import com.octagon_technologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagon_technologies.sky_weather.utils.getAdvancedForecastDescription
import com.octagon_technologies.sky_weather.utils.getHoursAndMinsWithDay
import com.octagon_technologies.sky_weather.utils.getStringResource
import com.octagon_technologies.sky_weather.utils.getWeatherIconFrom
import com.octagon_technologies.sky_weather.utils.isLastIndex
import com.octagon_technologies.sky_weather.utils.showLongToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HourlyForecastFragment : Fragment(R.layout.hourly_forecast_fragment) {

    private val viewModel: HourlyForecastViewModel by viewModels()
    private lateinit var binding: HourlyForecastFragmentBinding
    private lateinit var selectedHourlyForecastBinding: SelectedHourlyForecastLayoutBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HourlyForecastFragmentBinding.bind(view)
        setUpSelectedLayout()

        setUpStatusCode()
        setUpBottomSheet()
        setUpLiveData()
        setUpHourlyRecyclerView()
    }

    private fun setUpLiveData() {
        viewModel.listOfHourlyForecast.observe(viewLifecycleOwner) { listOfDailyForecast ->
            val timeInMillis = listOfDailyForecast?.firstOrNull()?.timeInMillis
            binding.topDayText.text = getDayOfWeek(timeInMillis)
        }
    }

    private fun setUpSelectedLayout() {
        selectedHourlyForecastBinding = binding.selectedHourlyForecastLayout.also {
            it.lifecycleOwner = viewLifecycleOwner
            it.theme = viewModel.theme
        }

        setUpSelectedHourlyRecyclerView()

        selectedHourlyForecastBinding.hourlyTempUnit.text =
            if (viewModel.units.value == Units.METRIC) "°C" else "°F"

        viewModel.selectedSingleForecast.observe(viewLifecycleOwner) { selectedSingleForecast ->
            selectedHourlyForecastBinding.apply {
                weatherStatus.text = selectedSingleForecast.weatherCode.getWeatherTitle()
                weatherImage.getWeatherIconFrom(selectedSingleForecast.weatherCode)

                tempText.text = selectedSingleForecast.getFormattedTemp()
                selectedHourlyHumidity.text = selectedSingleForecast.getFormattedHumidity()
                selectedHourlyDateText.text = selectedSingleForecast.timeInMillis.getHoursAndMinsWithDay(viewModel.timeFormat.value)
            }
        }
    }

    private fun setUpSelectedHourlyRecyclerView() {
        val selectedForecastGroupAdapter = GroupAdapter<GroupieViewHolder>()
        selectedHourlyForecastBinding.selectedRecyclerView.adapter =
            selectedForecastGroupAdapter

        viewModel.selectedSingleForecast.observe(viewLifecycleOwner) { selectedSingleForecast ->

            val listOfForecastDescription =
                getAdvancedForecastDescription(
                    selectedSingleForecast,
                    viewModel.units.value,
                    viewModel.windDirectionUnits.value
                )

            selectedForecastGroupAdapter.clear()

            listOfForecastDescription.forEach {
                selectedForecastGroupAdapter.add(
                    MiniForecastDescription(
                        isLastItem = listOfForecastDescription.isLastIndex(it),
                        eachWeatherDescription = it,
                        theme = viewModel.theme.value
                    )
                )
            }
        }
    }

    private fun setUpHourlyRecyclerView() {
        val hourlyForecastGroupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.hourlyRecyclerView.adapter = hourlyForecastGroupAdapter
        binding.hourlyRecyclerView.addCustomScrollListener(hourlyForecastGroupAdapter, binding)

        var firstDay: String? = null

        viewModel.listOfHourlyForecast.observe(viewLifecycleOwner) { listOfHourlyForecast ->
            // Clear first before adding fresh list of hourly forecast
            if (listOfHourlyForecast != null)
                hourlyForecastGroupAdapter.clear()

            listOfHourlyForecast?.forEach { hourlyForecast ->
                val currentDay = getDayOfWeek(hourlyForecast.timeInMillis)

                if (firstDay != currentDay) {
                    firstDay = currentDay
                    hourlyForecastGroupAdapter.add(HeaderMiniHourlyForecast(currentDay ?: "Andre"))
                }

                val miniHourlyForecast = MiniHourlyForecast(hourlyForecast = hourlyForecast,
                    timeFormat = viewModel.timeFormat.value,
                    openSelectedHourlyForecast = {
                        viewModel.selectHourlyForecast(hourlyForecast)
                        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                )

                hourlyForecastGroupAdapter.add(miniHourlyForecast)
            }
        }
    }

    private fun setUpBottomSheet() {
        bottomSheet = BottomSheetBehavior.from<View>(selectedHourlyForecastBinding.root).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mainActivity.binding.navView.visibility = View.GONE
                    changeSystemNavigationBarColor(
                        if (viewModel.theme.value == Theme.LIGHT) android.R.color.white
                        else R.color.dark_black
                    )
                    Timber.d("State changed to $newState")
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    mainActivity.binding.navView.visibility = View.VISIBLE
                    changeSystemNavigationBarColor(
                        if (viewModel.theme.value == Theme.LIGHT) R.color.light_theme_blue
                        else R.color.dark_theme_blue
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setUpStatusCode() {
        viewModel.statusCode.observe(viewLifecycleOwner) {
            val message = when (it ?: return@observe) {
                StatusCode.Success -> return@observe
                StatusCode.NoNetwork -> getStringResource(R.string.no_network_availble_plain_text)
                StatusCode.ApiLimitExceeded -> getStringResource(R.string.api_limit_exceeded_plain_text)
            }

            showLongToast(message)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.theme.observe(viewLifecycleOwner) {
            addToolbarAndBottomNav(it, bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED)
            changeSystemNavigationBarColor(
                if (it == Theme.LIGHT) R.color.light_theme_blue
                else R.color.dark_theme_blue
            )
        }
    }
}
