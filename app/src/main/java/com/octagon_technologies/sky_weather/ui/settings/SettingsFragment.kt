package com.octagon_technologies.sky_weather.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.SettingsFragmentBinding
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import com.octagon_technologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagon_technologies.sky_weather.utils.removeToolbarAndBottomNav
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater)
        setUpSwitchButton()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            units = viewModel.units
            windDirectionUnits = viewModel.windDirectionUnits
            theme = viewModel.theme
            timeFormat = viewModel.timeFormat

            backBtn.setOnClickListener { findNavController().popBackStack() }

            unitsMetricLayout.setOnClickListener { viewModel.changeUnits(Units.METRIC) }
            unitsImperialLayout.setOnClickListener { viewModel.changeUnits(Units.IMPERIAL) }

            windCardinalLayout.setOnClickListener {
                viewModel.changeWindDirections(WindDirectionUnits.CARDINAL)
            }
            windDegreesLayout.setOnClickListener { viewModel.changeWindDirections(WindDirectionUnits.DEGREES) }

            time12hourLayout.setOnClickListener { viewModel.changeTimeFormat(TimeFormat.HALF_DAY) }
            time24hourLayout.setOnClickListener { viewModel.changeTimeFormat(TimeFormat.FULL_DAY) }

            displayLightLayout.setOnClickListener { viewModel.changeTheme(Theme.LIGHT) }
            displayDarkLayout.setOnClickListener { viewModel.changeTheme(Theme.DARK) }
        }

        return binding.root
    }

    private fun setUpSwitchButton() {
        val notificationManagerCompat = NotificationManagerCompat.from(requireContext())

        binding.enableNotificationSwitch.setCheckedImmediatelyNoEvent(viewModel.isNotificationAllowed.value == true)
        binding.enableNotificationSwitch.setOnCheckedChangeListener { view, isChecked ->
            viewModel.toggleNotificationAllowed(isChecked, view)
        }

        viewModel.isNotificationAllowed.observe(viewLifecycleOwner) { isNotificationAllowed ->
            Timber.d("viewModel.isNotificationAllowed is $isNotificationAllowed")

            binding.enableNotificationSwitch.setCheckedImmediatelyNoEvent(isNotificationAllowed == true)
            binding.enableNotificationSwitch.backColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isNotificationAllowed == true) R.color.colorAccent else R.color.color_black
                )
            )

            // Remove all notifications if user disabled them
            if (!isNotificationAllowed)
                notificationManagerCompat.cancelAll()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.theme.observe(viewLifecycleOwner) {
            if (it == Theme.LIGHT) {
                removeToolbarAndBottomNav(android.R.color.white, false)
            } else {
                removeToolbarAndBottomNav(R.color.dark_black, true)
            }
            changeSystemNavigationBarColor(
                if (it == Theme.LIGHT) android.R.color.white
                else R.color.dark_black
            )
        }
    }

}