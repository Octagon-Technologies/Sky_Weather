package com.octagontechnologies.sky_weather.ui.hourly_forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagontechnologies.sky_weather.databinding.SelectedHourlyForecastLayoutBinding
import com.octagontechnologies.sky_weather.domain.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import com.octagontechnologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagontechnologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.HeaderMiniHourlyForecast
import com.octagontechnologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.MiniHourlyForecast
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.addToolbarAndBottomNav
import com.octagontechnologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagontechnologies.sky_weather.utils.getAdvancedForecastDescription
import com.octagontechnologies.sky_weather.utils.getHoursAndMinsWithDay
import com.octagontechnologies.sky_weather.utils.loadWeatherIcon
import com.octagontechnologies.sky_weather.utils.setUpToastMessage
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

    private val mainActivity by lazy { requireActivity() as MainActivity }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HourlyForecastFragmentBinding.bind(view)
        setUpSelectedLayout()
        setUpAd()

        setUpStatusCode()
        setUpBottomSheet()
        setUpLiveData()
        setUpHourlyRecyclerView()
    }

    private fun setUpLiveData() {
        viewModel.listOfHourlyForecast.observe(viewLifecycleOwner) { listOfDailyForecast ->
            val timeInMillis = listOfDailyForecast?.firstOrNull()?.timeInEpochMillis
            binding.topDayText.text = getDayOfWeek(timeInMillis)
        }
    }

    private fun setUpAd() {
        viewModel.adRepo.getNativeAd(
            fragment = this,
            nativeAdBinding = binding.selectedHourlyForecastLayout.selectedHourAdView,
            onAdCompleted = { isSuccess ->
                Timber.d("isSuccess is $isSuccess")

                binding.selectedHourlyForecastLayout.selectedHourAdView.root.visibility =
                    if (isSuccess) View.VISIBLE else View.GONE
            })
    }

    private fun setUpSelectedLayout() {
        selectedHourlyForecastBinding = binding.selectedHourlyForecastLayout
        val selectedCard = selectedHourlyForecastBinding.selectedHourlyCard

        selectedCard.shapeAppearanceModel = selectedCard.shapeAppearanceModel.toBuilder().apply {
            setTopLeftCornerSize(resources.getDimension(R.dimen._12sdp))
            setTopRightCornerSize(resources.getDimension(R.dimen._12sdp))
            setBottomLeftCornerSize(0f)
            setBottomRightCornerSize(0f)

        }.build()

        setUpSelectedHourlyRecyclerView()

        selectedHourlyForecastBinding.hourlyTempUnit.text =
            if (viewModel.units.value == Units.METRIC) "°C" else "°F"

        viewModel.selectedSingleForecast.observe(viewLifecycleOwner) { selectedSingleForecast ->
            selectedHourlyForecastBinding.apply {
                weatherStatus.text = selectedSingleForecast.weatherCode.getWeatherTitle()
                weatherImage.loadWeatherIcon(selectedSingleForecast?.timeInEpochMillis, selectedSingleForecast?.weatherCode)

                tempText.text = selectedSingleForecast.getFormattedTemp(viewModel.isImperial())
                selectedHourlyHumidity.text = resources.getString(R.string.humidity_format, selectedSingleForecast.getFormattedHumidity())
                selectedHourlyDateText.text = selectedSingleForecast.timeInEpochMillis.getHoursAndMinsWithDay(viewModel.timeFormat.value)
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
            Timber.d("SelectedSingleForecast is $selectedSingleForecast")

            listOfForecastDescription.forEach {
                selectedForecastGroupAdapter.add(
                    MiniForecastDescription(
                        isLastItem = false,
                        eachWeatherDescription = it
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
                val currentDay = getDayOfWeek(hourlyForecast.timeInEpochMillis)

                if (firstDay != currentDay) {
                    firstDay = currentDay
                    hourlyForecastGroupAdapter.add(HeaderMiniHourlyForecast(currentDay ?: "Andre"))
                }

                val miniHourlyForecast = MiniHourlyForecast(
                    hourlyForecast = hourlyForecast,
                    units = viewModel.units.value,
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
                    mainActivity.binding.navView.visibility = View.GONE
                    changeSystemNavigationBarColor(
                        if (viewModel.theme.value == Theme.LIGHT) android.R.color.white
                        else R.color.dark_black
                    )
                    Timber.d("State changed to $newState")
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mainActivity.binding.navView.visibility = View.VISIBLE
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
        viewModel.statusCode.setUpToastMessage(this) {
            viewModel.onStatusCodeDisplayed()
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
