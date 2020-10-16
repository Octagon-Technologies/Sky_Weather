package com.example.kotlinweatherapp.ui.find_location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FindLocationFragmentBinding
import com.example.kotlinweatherapp.ui.shared_code.removeToolbarAndBottomNav
import timber.log.Timber

class FindLocationFragment : Fragment() {

    private lateinit var viewModel: FindLocationViewModel
    private lateinit var binding: FindLocationFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FindLocationFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, FindLocationViewModelFactory(requireContext())).get(FindLocationViewModel::class.java)

        binding.viewModel = viewModel

        setOnClickListeners()
        viewModel.getFavouriteLocations()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.turnOnLocation()
        }


        viewModel.isLoading.observe(viewLifecycleOwner, {
            it?.run {
                binding.enableLocationText.text = if(this) resources.getString(R.string.loading_plain_text) else resources.getString(R.string.enable_location_services_plain_text)
            }
        })

        viewModel.reversedGeoCodingLocation.observe(viewLifecycleOwner, {
            val address = it.reverseGeoCodingAddress
            Timber.d("address is $address")

            if(address != null) {
                binding.gpsLocationLayout.visibility = View.VISIBLE
                binding.enableLocationLayout.visibility = View.GONE

                binding.gpsLocationCity.text = "${address.suburb}, ${address.city}"
                binding.gpsLocationCountry.text = it.reverseGeoCodingAddress.country
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this.removeToolbarAndBottomNav()
    }

    private fun setOnClickListeners() {
        binding.closeBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchQuery.setOnClickListener { findNavController().navigate(R.id.action_findLocationFragment_to_searchLocationFragment) }
        binding.enableLocationLayout.setOnClickListener { viewModel.checkIfPermissionIsGranted() }

        binding.gpsLocationLayout.setOnClickListener {viewModel.addLocationToDatabase()}
    }

}