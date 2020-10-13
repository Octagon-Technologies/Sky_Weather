package com.example.kotlinweatherapp.ui.see_more_current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.databinding.SeeMoreFragmentBinding

class SeeMoreFragment : Fragment() {

    private lateinit var viewModel: SeeMoreViewModel
    private lateinit var binding: SeeMoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SeeMoreFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, SeeMoreViewModelFactory(requireContext())).get(SeeMoreViewModel::class.java)
    }

}