package com.example.kotlinweatherapp.ui.current_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.CurrentForecastFragmentBinding
import com.example.kotlinweatherapp.ui.shared_code.addToolbarAndBottomNav
import timber.log.Timber

class CurrentForecastFragment : Fragment() {

    private lateinit var viewModel: CurrentForecastViewModel
    private lateinit var binding: CurrentForecastFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.current_forecast_fragment, container, false)
        viewModel = ViewModelProvider(this, CurrentForecastViewModelFactory(requireContext())).get(CurrentForecastViewModel::class.java)

        viewModel.shouldNavigate.observe(viewLifecycleOwner, {
            if (it) findNavController().navigate(R.id.action_currentForecastFragment_to_findLocationFragment)
        })
        
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadData()
        setOnClickListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this.addToolbarAndBottomNav()
    }

    private fun setOnClickListeners() {
        binding.seeMoreBtn.setOnClickListener {
            seeMoreConditionsOnClick()
        }
        binding.seeWeeklyOutlookBtn.setOnClickListener {
            seeWeeklyOutlookOnClick()
        }
        binding.lookingAheadDisplayText.setOnClickListener {
            lookingAheadOnClick()
        }
    }

    private fun seeWeeklyOutlookOnClick() {
        Timber.d("seeWeeklyOutlookOnClick called")
    }

    private fun seeMoreConditionsOnClick() {
        Timber.d("seeMoreConditionsOnClick called")
    }

    private fun lookingAheadOnClick() {
        Timber.d("lookingAheadOnClick called")
    }

}