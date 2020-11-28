package com.octagon_technologies.sky_weather.ui.daily_forecast.daily_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.TimeFormat
import com.octagon_technologies.sky_weather.WindDirectionUnits
import com.octagon_technologies.sky_weather.databinding.DailyTabFragmentBinding
import com.octagon_technologies.sky_weather.network.selected_daily_forecast.Min
import com.octagon_technologies.sky_weather.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.network.single_forecast.WindDirection
import com.octagon_technologies.sky_weather.network.single_forecast.WindGust
import com.octagon_technologies.sky_weather.network.single_forecast.WindSpeed
import com.octagon_technologies.sky_weather.ui.current_forecast.EachCurrentForecastDescriptionItem
import com.octagon_technologies.sky_weather.ui.current_forecast.EachWeatherDescription
import com.octagon_technologies.sky_weather.ui.current_forecast.MainWind
import com.octagon_technologies.sky_weather.ui.current_forecast.getActualWind
import com.octagon_technologies.sky_weather.ui.daily_forecast.DailyForecastViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber
import kotlin.properties.Delegates

class DailyTabFragment : Fragment() {

    lateinit var theme: LiveData<Theme>
    lateinit var windDirectionUnits: LiveData<WindDirectionUnits>
    lateinit var dailyForecastViewModel: DailyForecastViewModel
    lateinit var timeFormat: LiveData<TimeFormat>
    var isDay by Delegates.notNull<Boolean>()

    companion object {
        fun getInstance(
            constructorIsDay: Boolean,
            mainActivity: MainActivity,
            constructorDailyForecastViewModel: DailyForecastViewModel
        ) =
            DailyTabFragment().apply {
                isDay = constructorIsDay
                theme = mainActivity.liveTheme
                windDirectionUnits = mainActivity.liveWindDirectionUnits
                timeFormat = mainActivity.liveTimeFormat
                dailyForecastViewModel = constructorDailyForecastViewModel

            }
    }

    val groupAdapter = GroupAdapter<GroupieViewHolder>()

    val binding by lazy {
        DailyTabFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.theme = theme
            it.isDay = isDay
            it.lunarForecast = dailyForecastViewModel.lunarForecast
            it.timeFormat = timeFormat
            it.selectedDailyForecast = dailyForecastViewModel.selectedDailyForecast
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectedDayRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupAdapter
        }

        dailyForecastViewModel.selectedDailyForecast.observe(viewLifecycleOwner) { selectedDailyForecast ->
            groupAdapter.clear()
            selectedDailyForecast?.apply {
                Timber.d("selectedDailyForecast called in DailyTabFragment with isDay as $isDay.")

                val dailyConditions = arrayListOf(
                    getFeelsLike(),
                    getWind(),
                    getRainFall(),
                    getAirPressure()
                ).filterNotNull()

                dailyConditions.forEach {
                    groupAdapter.add(
                        EachCurrentForecastDescriptionItem(
                            dailyConditions.indexOf(it), it, theme.value
                        )
                    )
                }
            }
        }
    }

    private fun SelectedDailyForecast.getRainFall(): EachWeatherDescription? {
        val rainInMM = try {
            precipitation?.get(if (isDay) 1 else 0)
        } catch (e: Exception) {
            precipitation?.get(0)
        }?.max?.value
        return EachWeatherDescription("Rain Amount", "$rainInMM mm")
    }

    private fun SelectedDailyForecast.getFeelsLike(): EachWeatherDescription? {
        return try {
            feelsLike?.get(if (isDay) 1 else 0)
        } catch (e: Exception) {
            feelsLike?.get(0)
        }?.let {
            EachWeatherDescription(
                "FeelsLike ${if (isDay) "High" else "Low"}",
                (if (isDay) it.max else it.min)?.value?.toString()
            )
        }
    }

    private fun SelectedDailyForecast.getAirPressure(): EachWeatherDescription? {
        val it = try {
            baroPressure?.get(if (isDay) 1 else 0)
        } catch (e: Exception) {
            baroPressure?.get(0)
        }
        return EachWeatherDescription(
            "Pressure",
            "${(if (isDay) it?.max else it?.min)?.value?.toInt() ?: "--"} mbar"
        )
    }

    private fun SelectedDailyForecast.getWind(): EachWeatherDescription {
        val values =
            try {
                Pair(windDirection?.get(if (isDay) 1 else 0), windSpeed?.get(if (isDay) 1 else 0))
            } catch (e: Exception) {
                Pair(windDirection?.get(0), windSpeed?.get(0))
            }

        val descriptionData =
            if (isDay) getMainWind(values.first?.max, values.second?.max)
            else getMainWind(values.first?.min, values.second?.min)

        return EachWeatherDescription("Average Wind", descriptionData)
    }

    private fun getMainWind(minWindDirection: Min?, minWindSpeed: Min?): String? {
        val mainWind = MainWind(
            WindDirection(minWindDirection?.units, minWindDirection?.value),
            WindSpeed(minWindSpeed?.units, minWindSpeed?.value),
            WindGust(null, null)
        )

        return getActualWind(mainWind, windDirectionUnits.value)
    }
}