package com.octagon_technologies.sky_weather.ads

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.octagon_technologies.sky_weather.databinding.NativeAdsLayoutBinding
import com.octagon_technologies.sky_weather.utils.Constants
import com.octagon_technologies.sky_weather.utils.isNull
import timber.log.Timber

class AdHelper(private val activity: AppCompatActivity) {
    val builder = AdLoader.Builder(activity.applicationContext, Constants.AD_ID)
    private var currentNativeAd: UnifiedNativeAd? = null

    fun loadAd(nativeAdsLayoutBinding: NativeAdsLayoutBinding, onComplete: (Boolean) -> Unit) {
        try {
            getAdLoader(nativeAdsLayoutBinding, onComplete)?.loadAd(getAdRequest())
        } catch (e: Throwable) {
            Timber.e(e, "Evil exception hiding in ads")
        }
    }

    // TODO - Avoid supressing deprecations
    @Suppress("DEPRECATION")
    private fun getAdLoader(
        nativeAdsLayoutBinding: NativeAdsLayoutBinding,
        onComplete: (Boolean) -> Unit
    ): AdLoader? {
        return builder
            .forUnifiedNativeAd { unifiedNativeAd ->
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.

                var activityDestroyed = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    activityDestroyed = activity.isDestroyed
                }
                if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    unifiedNativeAd.destroy()
                    return@forUnifiedNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                currentNativeAd?.destroy()
                currentNativeAd = unifiedNativeAd
                displayUnifiedNativeAd(nativeAdsLayoutBinding, currentNativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    onComplete(true)
                }
                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    onComplete(false)
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_BOTTOM_RIGHT)
                    .setRequestCustomMuteThisAd(true)
                    .setImageOrientation(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE)
                    .build()
            )
            .build()
    }

    private fun getAdRequest(): AdRequest =
        AdRequest.Builder().build()

    private fun displayUnifiedNativeAd(nativeAdsLayoutBinding: NativeAdsLayoutBinding, ad: UnifiedNativeAd?) {
        if (ad == null) return

        val root = (nativeAdsLayoutBinding.root as UnifiedNativeAdView).also{
            it.mediaView = nativeAdsLayoutBinding.adMediaView
        }

        root.mediaView.setMediaContent(ad.mediaContent)
        Timber.d("ad.price is ${ad.price}")
        Timber.d("ad.mediaContent.isNull() is ${ad.mediaContent.isNull()}")

        // Call the UnifiedNativeAdView's setNativeAd method to register the
        // NativeAdObject.
        root.setNativeAd(ad)
    }
}