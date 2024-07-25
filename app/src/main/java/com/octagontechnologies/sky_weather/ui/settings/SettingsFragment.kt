package com.octagontechnologies.sky_weather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.SettingsFragmentBinding
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.removeToolbarAndBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding
    private val viewModel by viewModels<SettingsViewModel>()


    private fun setUpComposeUI() =
        ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SettingsScreen(findNavController())
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setUpComposeUI()
    }

//    private fun setUpSwitchButton() {
//        val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
//
//        binding.enableNotificationSwitch.setCheckedImmediatelyNoEvent(viewModel.isNotificationAllowed.value == true)
//        binding.enableNotificationSwitch.setOnCheckedChangeListener { view, isChecked ->
//            viewModel.toggleNotificationAllowed(isChecked, view)
//        }
//
//        viewModel.isNotificationAllowed.observe(viewLifecycleOwner) { isNotificationAllowed ->
//            Timber.d("viewModel.isNotificationAllowed is $isNotificationAllowed")
//
//            binding.enableNotificationSwitch.setCheckedImmediatelyNoEvent(isNotificationAllowed == true)
//            binding.enableNotificationSwitch.backColor = ColorStateList.valueOf(
//                ContextCompat.getColor(
//                    requireContext(),
//                    if (isNotificationAllowed == true) R.color.colorAccent else R.color.color_black
//                )
//            )
//
//            // Remove all notifications if user disabled them
//            if (!isNotificationAllowed)
//                notificationManagerCompat.cancelAll()
//        }
//    }

    override fun onStart() {
        super.onStart()
        viewModel.theme.asLiveData().observe(viewLifecycleOwner) {
            if (it == Theme.LIGHT) {
                removeToolbarAndBottomNav(android.R.color.white, false)
            } else {
                removeToolbarAndBottomNav(R.color.dark_black, true)
            }
        }
//            changeSystemNavigationBarColor(
//                if (it == Theme.LIGHT) android.R.color.white
//                else R.color.dark_black
//            )
//        }
    }

}