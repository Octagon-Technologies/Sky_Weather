package com.octagontechnologies.sky_weather.utils

fun <T> List<T>.isLastIndex(item: T): Boolean =
    lastIndex == indexOf(item)


//fun doSafely(action: () -> Unit) {
//    try {
//        action()
//    } catch (e: Exception) {
//        Timber.e(e)
//    }
//}
//
//suspend fun <T> suspendSafely(action: suspend () -> T): T {
//    try {
//        action()
//    } catch (e: Exception) {
//        Timber.e(e)
//    }
//}