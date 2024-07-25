package com.octagontechnologies.sky_weather.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octagontechnologies.sky_weather.R
import retrofit2.HttpException
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.UnknownHostException

/*
In most fragments, we are receiving the network request status code from the viewModel

fragment - Fragment needed to provide viewLifecycleOwner and access Resources
onStatusCodeDisplayed() - Status code has been displayed. Call ViewModel function to reset LiveData.
                          This prevents configuration changes from causing re-triggers
 */
fun LiveData<StatusCode?>.setUpToastMessage(fragment: Fragment, onStatusCodeDisplayed: () -> Unit) {
    observe(fragment.viewLifecycleOwner) {
        val message = when (it ?: return@observe) {
            StatusCode.Success -> return@observe
            StatusCode.NoNetwork -> fragment.getStringResource(R.string.no_network_availble_plain_text)
            StatusCode.ApiLimitExceeded -> fragment.getStringResource(R.string.api_limit_exceeded_plain_text)
        }

        fragment.showLongToast(message)
        onStatusCodeDisplayed()
    }
}


suspend fun MutableLiveData<StatusCode?>.catchNetworkErrors(networkCall: suspend () -> Unit) {
    try {
        networkCall()
    } catch (http: HttpException) {
        value = StatusCode.ApiLimitExceeded
    } catch (unknown: UnknownHostException) {
        value = StatusCode.NoNetwork
    } catch (timeout: InterruptedIOException) {
        value = StatusCode.NoNetwork
    } catch (e: Exception) {
        Timber.e(e)
    }
}