package com.octagon_technologies.sky_weather.utils

import com.octagon_technologies.sky_weather.BuildConfig

object Constants {

    // TODO - MOve to build config (reference soundcloud app)
        val AD_ID = if (BuildConfig.DEBUG)
            "ca-app-pub-3940256099942544/2247696110"
        else
            "ca-app-pub-9000696988586021/5977866204"
//    const val AD_ID = "ca-app-pub-3940256099942544/2247696110"

}