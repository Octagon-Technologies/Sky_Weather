package com.octagontechnologies.sky_weather.utils


import android.net.http.HttpException
import com.octagontechnologies.sky_weather.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

/**
 * Wrapper class that data returned during network/DB calls while catching errors
 *
 * @param data - The value of the operation if successful
 * @param errorType - The ErrorType if the operation is unsuccessful
 * @param resMessage - The string ID for the error message to be displayed
 */
sealed class Resource<T>(
    val data: T? = null,
    private val errorType: ErrorType? = null,
    open val resMessage: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorType: ErrorType, resMessage: Int? = null, data: T? = null) : Resource<T>(data, errorType) {
        override val resMessage = resMessage ?: when (errorType) {
            ErrorType.ApiError -> R.string.api_limit_exceeded_plain_text
            ErrorType.NoNetworkError -> R.string.no_network_availble_plain_text
            ErrorType.Other -> R.string.error_occured
        }
    }

    class Loading<T>(data: T? = null) : Resource<T>(data)
}

enum class ErrorType {
    ApiError,
    NoNetworkError,
    Other
}

/**
 * Performs the network call and DB operation on the IO thread while catching any possible errors
 */
suspend fun <T> doOperation(operation: suspend () -> Resource<T>): Resource<T> =
    withContext(Dispatchers.IO) {
        return@withContext try {
            operation()
        } catch (noNetworkError: UnknownHostException) {
            Resource.Error(ErrorType.NoNetworkError)
        }
        catch (apiError: HttpException) {
            Resource.Error(ErrorType.ApiError)
        }
//    catch (timeOut: SocketTimeoutException) {
//        Resource.Error(ErrorType.ApiError)
//    }
    }