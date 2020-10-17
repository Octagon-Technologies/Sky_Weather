package com.example.kotlinweatherapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinweatherapp.databinding.SettingsFragmentBinding
import com.example.kotlinweatherapp.ui.shared_code.addToolbarAndBottomNav
import com.example.kotlinweatherapp.ui.shared_code.removeToolbarAndBottomNav
import com.example.kotlinweatherapp.ui.weather_forecast_objects.DisplayMode
import com.example.kotlinweatherapp.ui.weather_forecast_objects.TimeFormat
import com.example.kotlinweatherapp.ui.weather_forecast_objects.Units
import com.example.kotlinweatherapp.ui.weather_forecast_objects.WindDirectionUnits
import timber.log.Timber

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding
    private var initialBasicSettingsDataClass: BasicSettingsDataClass? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(requireContext())).get(SettingsViewModel::class.java)

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        viewModel.basicSettingsDataClass.observe(viewLifecycleOwner, {
            it?.run {
                if (it.units != initialBasicSettingsDataClass?.units) updateUnitLayout()
                if (it.windDirectionUnits != initialBasicSettingsDataClass?.windDirectionUnits) updateWindLayout()
                if (it.timeFormat != initialBasicSettingsDataClass?.timeFormat) updateTimeLayout()
                if (it.displayMode != initialBasicSettingsDataClass?.displayMode) updateDisplayLayout()

                initialBasicSettingsDataClass = this
            }
            Timber.d("basicSettingsDataClass is $it")
        })

        viewModel.loadData()
        initialBasicSettingsDataClass = viewModel.basicSettingsDataClass.value


        return binding.root
    }


    private fun BasicSettingsDataClass.updateUnitLayout() {
        binding.unitsLeftEachItemName.doTextChange(units == Units.IMPERIAL, true)
        binding.unitsLeftEachItemDescription.doTextChange(units == Units.IMPERIAL, false)
        binding.unitsRightEachItemName.doTextChange(units == Units.METRIC, true)
        binding.unitsRightEachItemDescription.doTextChange(units == Units.METRIC, false)

        binding.unitsLeftLayout.doUnitsChange(viewModel, binding)
        binding.unitsRightLayout.doUnitsChange(viewModel, binding)
    }

    private fun BasicSettingsDataClass.updateWindLayout() {
        binding.windLeftEachItemName.doTextChange(windDirectionUnits == WindDirectionUnits.CARDINAL, true)
        binding.windLeftEachItemDescription.doTextChange(windDirectionUnits == WindDirectionUnits.CARDINAL, false)
        binding.windRightEachItemName.doTextChange(windDirectionUnits == WindDirectionUnits.DEGREES, true)
        binding.windRightEachItemDescription.doTextChange(windDirectionUnits == WindDirectionUnits.DEGREES, false)

        binding.windLeftLayout.doWindDirectionChange(viewModel, binding)
        binding.windRightLayout.doWindDirectionChange(viewModel, binding)
    }

    private fun BasicSettingsDataClass.updateTimeLayout() {
        binding.timeLeftEachItemName.doTextChange(timeFormat == TimeFormat.HALF_DAY, true)
        binding.timeLeftEachItemDescription.doTextChange(timeFormat == TimeFormat.HALF_DAY, false)
        binding.timeRightEachItemName.doTextChange(timeFormat == TimeFormat.FULL_DAY, true)
        binding.timeRightEachItemDescription.doTextChange(timeFormat == TimeFormat.FULL_DAY, false)

        binding.timeLeftLayout.doTimeFormatChange(viewModel, binding)
        binding.timeRightLayout.doTimeFormatChange(viewModel, binding)
    }

    private fun BasicSettingsDataClass.updateDisplayLayout() {
        binding.displayLeftEachItemName.doTextChange(displayMode == DisplayMode.LIGHT, true)
        binding.displayRightEachItemName.doTextChange(displayMode == DisplayMode.LIGHT, true)

        binding.displayLeftLayout.doDisplayModeChange(viewModel, binding)
        binding.displayRightLayout.doDisplayModeChange(viewModel, binding)
    }

    override fun onStart() {
        super.onStart()
        removeToolbarAndBottomNav(android.R.color.white)
    }

    override fun onStop() {
        super.onStop()
        addToolbarAndBottomNav()
    }
}