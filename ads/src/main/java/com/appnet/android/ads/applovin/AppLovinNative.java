package com.appnet.android.ads.applovin;

import android.app.Activity;
import android.content.Context;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdService;
import com.applovin.sdk.AppLovinSdk;

import java.util.List;

public class AppLovinNative {

    public AppLovinNative(Context context) {
    }

    public void loadAd(final Activity context) {
        final AppLovinSdk sdk = AppLovinSdk.getInstance(context);
        final AppLovinNativeAdService nativeAdService = sdk.getNativeAdService();
        nativeAdService.loadNextAd(new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List<AppLovinNativeAd> nativeAds) {
                if(nativeAds == null || nativeAds.isEmpty()) {
                    return;
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        renderAd(nativeAds.get(0));
                    }
                });
            }

            @Override
            public void onNativeAdsFailedToLoad(int errorCode) {

            }
        });
    }

    private void renderAd(AppLovinNativeAd ad) {

    }
}
