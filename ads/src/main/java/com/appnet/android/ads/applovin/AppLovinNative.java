package com.appnet.android.ads.applovin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdService;
import com.applovin.sdk.AppLovinSdk;

import java.util.List;

public class AppLovinNative {
    private ViewGroup mViewNativeAdRoot;
    private ImageView mImvNativeAdIcon;
    private TextView mTvNativeAdTitle;
    private TextView mTvNativeAdSponsored;
    private TextView mTvNativeAdSocialContext;
    private TextView mTvNativeAdBody;
    private TextView mTvNativeAdBodySub;
    private View mViewNativeAdActionContainer;
    private Button mBtnNativeAdCallToAction;
    private LinearLayout mAdChoicesContainer;

    public AppLovinNative(Context context) {
    }

    public void loadAd(final Context context) {
        final AppLovinSdk sdk = AppLovinSdk.getInstance(context);
        final AppLovinNativeAdService nativeAdService = sdk.getNativeAdService();
        nativeAdService.loadNextAd(new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List<AppLovinNativeAd> nativeAds) {
                if(nativeAds == null || nativeAds.isEmpty()) {
                    return;
                }
            }

            @Override
            public void onNativeAdsFailedToLoad(int errorCode) {

            }
        });
    }

    private void renderAd(AppLovinNativeAd ad) {

    }
}
