package com.octagon_technologies.sky_weather.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.SettingsFragmentBinding
import com.octagon_technologies.sky_weather.ui.shared_code.SettingsRepo
import timber.log.Timber

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding

    private val mainSettings by lazy { SettingsRepo(requireContext()) }
    private val mainActivity by lazy { activity as MainActivity }
    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater)

        binding.apply {
            backBtn.setOnClickListener { findNavController().popBackStack() }

            lifecycleOwner = viewLifecycleOwner
            mainActivity.let {
                units = it.liveUnits
                windDirectionUnits = it.liveWindDirectionUnits
                theme = it.liveTheme
                timeFormat = it.liveTimeFormat
            }

            enableNotificationSwitch.apply {
                setCheckedImmediately(mainActivity.liveNotificationAllowed.value == true)
                backColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        if (isChecked) R.color.colorAccent else R.color.color_black
                    )
                )

                setOnCheckedChangeListener { _, isChecked ->
                    addCustomClickListener(isChecked)
                    if (isChecked) {
                        backColor = ColorStateList.valueOf(
                            ContextCompat.getColor(context, R.color.colorAccent)
                        ).withAlpha(190)
                    } else {
                        backColor = ColorStateList.valueOf(
                            ContextCompat.getColor(context, R.color.color_black)
                        ).withAlpha(190)

                        notificationManagerCompat.cancelAll()
                    }
                }
            }

            unitsLeftLayout.addCustomClickListener(Units.IMPERIAL)
            unitsRightLayout.addCustomClickListener(Units.METRIC)

            windLeftLayout.addCustomClickListener(WindDirectionUnits.CARDINAL)
            windRightLayout.addCustomClickListener(WindDirectionUnits.DEGREES)

            timeLeftLayout.addCustomClickListener(TimeFormat.HALF_DAY)
            timeRightLayout.addCustomClickListener(TimeFormat.FULL_DAY)

            displayLeftLayout.addCustomClickListener(Theme.LIGHT)
            displayRightLayout.addCustomClickListener(Theme.DARK)
        }

        return binding.root
    }

    private fun View.addCustomClickListener(input: Any) {
        setOnClickListener {
            mainActivity.apply {
                mainSettings.editDataStore(input)
                hasNotificationChanged = false

                when (input) {
                    is Units -> liveUnits.value = input
                    is WindDirectionUnits -> liveWindDirectionUnits.value = input
                    is Theme -> liveTheme.value = input
                    is TimeFormat -> liveTimeFormat.value = input
                    is Boolean -> liveNotificationAllowed.value = input
                    else -> Timber.i("Unexpected input of $input")
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        mainActivity.liveTheme.observe(viewLifecycleOwner, {
            Timber.d("liveTheme changed in observe to $it")
            removeToolbarAndBottomNav(if (it == Theme.LIGHT) android.R.color.white else R.color.dark_black)
        })
    }

}