package com.octagon_technologies.sky_weather.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * An extension function that removes the need to write ViewModelFactory by creating a factory based on ViewModel arguments
 *
 * Example:
 * private val viewModel: DailyForecastViewModel by createViewModel(this, DailyForecastViewModel(requireContext()))
 */
inline fun <reified VM : ViewModel> createViewModel(fragment: Fragment, viewModel: ViewModel): Lazy<VM> =
    fragment.viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return viewModel as T
            }
        }
    }


//inline fun <reified VM : ViewModel> ViewModel.createViewModel(fragment: Fragment): Lazy<VM> {
//    return lazy {
//        ViewModelProvider(fragment.viewModelStore,
//            object : ViewModelProvider.Factory {
//                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                    @Suppress("UNCHECKED_CAST")
//                    return this@createViewModel as T
//                }
//            }
//        ).get(VM::class.java)
//    }
//}