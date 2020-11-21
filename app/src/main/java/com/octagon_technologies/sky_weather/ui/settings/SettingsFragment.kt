package com.octagon_technologies.sky_weather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.SettingsFragmentBinding
import com.octagon_technologies.sky_weather.ui.shared_code.MainSettings
import timber.log.Timber

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding

    private val mainSettings by lazy { MainSettings(requireContext()) }
    private val mainActivity by lazy { activity as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

                when (input) {
                    is Units -> liveUnits.value = input
                    is WindDirectionUnits -> liveWindDirectionUnits.value = input
                    is Theme -> liveTheme.value = input
                    is TimeFormat -> liveTimeFormat.value = input
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

    override fun onStop() {
        super.onStop()
        addToolbarAndBottomNav(mainActivity.liveTheme.value)
    }
}