package com.octagontechnologies.sky_weather.ads

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.octagontechnologies.sky_weather.BuildConfig
import com.octagontechnologies.sky_weather.databinding.NativeAdLayoutBinding
import timber.log.Timber

class AdRepo {

    private var currentNativeAd: NativeAd? = null

    // Test Native Unit: ca-app-pub-3940256099942544/2247696110
    // Actual Native Unit: ca-app-pub-2375349794400927/1791768204// ca-app-pub-9146124333040114/8697153563
    private val NATIVE_AD_UNIT =
        if (BuildConfig.DEBUG)
            "ca-app-pub-3940256099942544/2247696110"
        else "ca-app-pub-2375349794400927/1791768204"// "ca-app-pub-9146124333040114/8697153563"

    /*

    onAdCompleted -> Called when the ad loading is over. Boolean is whether the ad succeeded or failed.
     */
    private fun Fragment.getNativeAdLoader(
        context: Context,
        nativeAdBinding: NativeAdLayoutBinding,
        onAdCompleted: (Boolean) -> Unit
    ) = AdLoader.Builder(context, NATIVE_AD_UNIT)
        .forNativeAd { nativeAd ->
            try {
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.
                val activity = requireActivity()
                if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd

                onAdCompleted(true)
                populateNativeAdView(nativeAd, nativeAdBinding)
            } catch (e: Exception) {
                Timber.e(e)

                try {
                    currentNativeAd?.destroy()
                } catch (ne: Exception) {
                    Timber.e(ne)
                }
            }
        }
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                onAdCompleted(false)
            }
        })
        .withNativeAdOptions(
            NativeAdOptions.Builder()
                .build()
        )
        .build()


    private fun populateNativeAdView(nativeAd: NativeAd, nativeAdBinding: NativeAdLayoutBinding) {
        Timber.d("nativeAd is $nativeAd")
        Timber.d("nativeAd.mediaContent?.hasVideoContent() is ${nativeAd.mediaContent?.hasVideoContent()}")

        val adView = nativeAdBinding.root

        val nativeAdImage = nativeAdBinding.nativeAdImage
        nativeAdImage.mediaContent = nativeAd.mediaContent
        nativeAdImage.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        adView.mediaView = nativeAdImage

        val headlineView = nativeAdBinding.nativeAdText
        headlineView.text = nativeAd.headline
        adView.headlineView = headlineView

        adView.setNativeAd(nativeAd)
    }


    fun getNativeAd(
        fragment: Fragment,
        nativeAdBinding: NativeAdLayoutBinding,
        onAdCompleted: (Boolean) -> Unit
    ) {
        val adLoader =
            fragment.getNativeAdLoader(fragment.requireContext(), nativeAdBinding, onAdCompleted)
        adLoader.loadAd(AdRequest.Builder().build())
    }

}