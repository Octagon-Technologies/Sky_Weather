package com.octagontechnologies.sky_weather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.utils.removeToolbarAndBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

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


    override fun onStart() {
        super.onStart()
        removeToolbarAndBottomNav()
    }

}