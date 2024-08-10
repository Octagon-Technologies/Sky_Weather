package com.octagontechnologies.sky_weather.utils

import androidx.lifecycle.MutableLiveData
import retrofit2.HttpException
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.UnknownHostException

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