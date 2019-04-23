package com.appnet.android.ads.applovin;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdService;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.R;

import java.util.List;

public class AppLovinNative {
    private View mAdView;
    private ImageView mImvNativeAdIcon;
    private TextView mTvNativeAdTitle;
    private TextView mTvNativeAdSponsored;
    private TextView mTvNativeAdSocialContext;
    private TextView mTvNativeAdBody;
    private TextView mTvNativeAdBodySub;
    private View mViewNativeAdActionContainer;
    private Button mBtnNativeAdCallToAction;
    private LinearLayout mAdChoicesContainer;

    private OnAdLoadListener mOnAdLoadListener;

    public AppLovinNative(Context context) {
        mAdView = View.inflate(context, R.layout.applovin_native_ad, null);
        mImvNativeAdIcon = mAdView.findViewById(R.id.native_ad_icon);
        mTvNativeAdTitle = mAdView.findViewById(R.id.native_ad_title);
        mTvNativeAdSocialContext = mAdView.findViewById(R.id.native_ad_social_context);
        mTvNativeAdBody = mAdView.findViewById(R.id.native_ad_body);
        mTvNativeAdBodySub = mAdView.findViewById(R.id.native_ad_body_sub);
        mBtnNativeAdCallToAction = mAdView.findViewById(R.id.native_ad_call_to_action);
        mViewNativeAdActionContainer = mAdView.findViewById(R.id.native_ad_action_container);
        mTvNativeAdSponsored = mAdView.findViewById(R.id.sponsored_label);
        mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);
    }

    public void loadAd(final Context context) {
        final AppLovinSdk sdk = AppLovinSdk.getInstance(context);
        final AppLovinNativeAdService nativeAdService = sdk.getNativeAdService();
        nativeAdService.loadNextAd(new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List<AppLovinNativeAd> nativeAds) {
                AppLovinSdkUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nativeAds == null || nativeAds.isEmpty()) {
                            if(mOnAdLoadListener != null) {
                                mOnAdLoadListener.onAdFailed();
                            }
                            return;
                        }
                        renderAd(nativeAds.get(0));
                        if(mOnAdLoadListener != null) {
                            mOnAdLoadListener.onAdLoaded();
                        }
                    }
                });
            }

            @Override
            public void onNativeAdsFailedToLoad(int errorCode) {
                if(mOnAdLoadListener != null) {
                    mOnAdLoadListener.onAdFailed();
                }
            }
        });
    }

    private void renderAd(AppLovinNativeAd ad) {
        mTvNativeAdTitle.setText(ad.getTitle());
        mTvNativeAdBody.setText(ad.getDescriptionText());
        // Icon
        final int iconSizeDp = 25; // match the size defined in the XML layout
        AppLovinSdkUtils.safePopulateImageView(mImvNativeAdIcon, Uri.parse(ad.getIconUrl()), iconSizeDp);
        ad.trackImpression();
    }

    public void setOnLoadListener(OnAdLoadListener listener) {
        mOnAdLoadListener = listener;
    }

    public View getView() {
        return mAdView;
    }
}
