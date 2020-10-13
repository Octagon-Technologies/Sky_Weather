package com.example.kotlinweatherapp.ui.search_location

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinweatherapp.databinding.SearchLocationFragmentBinding
import com.example.kotlinweatherapp.ui.find_location.FindLocationViewModel
import com.example.kotlinweatherapp.ui.find_location.FindLocationViewModelFactory
import com.example.kotlinweatherapp.ui.shared_code.addToolbarAndBottomNav
import com.example.kotlinweatherapp.ui.shared_code.removeToolbarAndBottomNav
import timber.log.Timber

class SearchLocationFragment : Fragment() {

    private lateinit var viewModel: SearchLocationViewModel
    private lateinit var findLocationViewModel: FindLocationViewModel
    private lateinit var binding: SearchLocationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchLocationFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, SearchLocationViewModelFactory(requireContext())).get(SearchLocationViewModel::class.java)
        findLocationViewModel = ViewModelProvider(this, FindLocationViewModelFactory(requireContext())).get(FindLocationViewModel::class.java)
        binding.lifecycleOwner = this

        binding.findLocationViewModel = findLocationViewModel
        binding.locationItems = viewModel.locationSuggestions
        binding.navController = findNavController()
        binding.fragmentLifecycleOwner = viewLifecycleOwner

        val handler = object : CountDownTimer(1200, 500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                viewModel.getLocationSuggestions(binding.searchQuery.text.toString())
            }
        }

        binding.searchQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.cancel()
                handler.start()
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        return binding.root
    }
}