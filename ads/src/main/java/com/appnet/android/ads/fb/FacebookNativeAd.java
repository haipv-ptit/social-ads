package com.appnet.android.ads.fb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appnet.android.ads.OnAdLoadListener;
import com.appnet.android.ads.R;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FacebookNativeAd extends AbstractAdListener implements NativeAdListener {
    private String mUnitId;
    private WeakReference<Context> mContext;
    private NativeAd mNativeAd;
    private NativeAdLayout mAdView;

    private boolean mIsMedia = false;
    private boolean mIsActionContainer = false;

    private int mRetry;
    private OnAdLoadListener mOnAdLoadListener;

    private ViewGroup mViewNativeAdRoot;
    private MediaView mImvNativeAdIcon;
    private TextView mTvNativeAdTitle;
    private TextView mTvNativeAdSponsored;
    private MediaView mMvNativeAdMedia;
    private TextView mTvNativeAdSocialContext;
    private TextView mTvNativeAdBody;
    private TextView mTvNativeAdBodySub;
    private View mViewNativeAdActionContainer;
    private Button mBtnNativeAdCallToAction;
    private LinearLayout mAdChoicesContainer;

    private FacebookNativeAd(Context context, String unitId) {
        mUnitId = unitId;
        mRetry = 0;
        mContext = new WeakReference<>(context);
        initFan();
    }

    private void initFan() {
        mAdView = (NativeAdLayout) View.inflate(mContext.get(), R.layout.fb_native_ad, null);
        mViewNativeAdRoot = mAdView.findViewById(R.id.view_native_ad_root);
        mImvNativeAdIcon = mAdView.findViewById(R.id.native_ad_icon);
        mTvNativeAdTitle = mAdView.findViewById(R.id.native_ad_title);
        mMvNativeAdMedia = mAdView.findViewById(R.id.native_ad_media);
        mTvNativeAdSocialContext = mAdView.findViewById(R.id.native_ad_social_context);
        mTvNativeAdBody = mAdView.findViewById(R.id.native_ad_body);
        mTvNativeAdBodySub = mAdView.findViewById(R.id.native_ad_body_sub);
        mBtnNativeAdCallToAction = mAdView.findViewById(R.id.native_ad_call_to_action);
        mViewNativeAdActionContainer = mAdView.findViewById(R.id.native_ad_action_container);
        mTvNativeAdSponsored = mAdView.findViewById(R.id.sponsored_label);
        mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);
        mNativeAd = new NativeAd(mContext.get(), mUnitId);
        mNativeAd.setAdListener(this);
    }

    private void checkViewConfig() {
        if (mIsMedia) {
            mMvNativeAdMedia.setVisibility(View.VISIBLE);
        } else {
            mMvNativeAdMedia.setVisibility(View.GONE);
        }
        if (mIsActionContainer) {
            mTvNativeAdBodySub.setVisibility(View.GONE);
            mViewNativeAdActionContainer.setVisibility(View.VISIBLE);
        } else {
            mTvNativeAdBodySub.setVisibility(View.VISIBLE);
            mViewNativeAdActionContainer.setVisibility(View.GONE);
            mTvNativeAdSponsored.setVisibility(View.GONE);
        }
    }

    private void addView(ViewGroup viewGroup) {
        viewGroup.addView(mAdView);
    }

    private void retry(boolean isCountRetry) {
        if (isCountRetry) {
            mRetry++;
        }
        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }
        mNativeAd = new NativeAd(mContext.get(), mUnitId);
        mNativeAd.setAdListener(this);
        mNativeAd.loadAd();
    }

    public void loadAd() {
        mNativeAd.loadAd();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        if (adError.getErrorCode() == AdError.NETWORK_ERROR_CODE) {
            retry(false);
            return;
        }
        if (mRetry < 2) {
            retry(true);
            return;
        }
        mRetry = 0;
        if (mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdFailed();
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (mNativeAd == null) {
            return;
        }
        inflateAd(ad);
    }

    private void inflateAd(Ad ad) {
        mRetry = 0;
        mNativeAd.unregisterView();
        // Set the Text.
        mTvNativeAdTitle.setText(mNativeAd.getAdvertiserName());
        if (mIsActionContainer) {
            mTvNativeAdSocialContext.setText(mNativeAd.getAdSocialContext());
            mTvNativeAdBody.setText(mNativeAd.getAdBodyText());
            mBtnNativeAdCallToAction.setText(mNativeAd.getAdCallToAction());
        } else {
            mTvNativeAdBodySub.setText(mNativeAd.getAdBodyText());
        }

        // Add the AdChoices icon

        AdOptionsView adChoicesView = new AdOptionsView(mContext.get(), mNativeAd, mAdView);
        mAdChoicesContainer.removeAllViews();
        mAdChoicesContainer.addView(adChoicesView, 0);
        //
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(mTvNativeAdTitle);
        clickableViews.add(mTvNativeAdBodySub);
        mNativeAd.registerViewForInteraction(mAdView, mMvNativeAdMedia, mImvNativeAdIcon, clickableViews);
        if (mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdLoaded();
        }
    }

    @Override
    public void onMediaDownloaded(Ad ad) {

    }

    public static class Builder {
        private final Context context;
        private final String unitId;

        private boolean adMedia = true;
        private boolean adActionContainer = false;
        private OnAdLoadListener mOnAdLoadListener;

        private ViewGroup mAdViewContainer;

        public Builder(Context context, String unitId) {
            this.context = context;
            this.unitId = unitId;
        }

        public Builder enableMedia(boolean value) {
            adMedia = value;
            return this;
        }

        public Builder enableActionContainer(boolean value) {
            adActionContainer = value;
            return this;
        }

        public Builder setOnAdLoadListener(OnAdLoadListener listener) {
            this.mOnAdLoadListener = listener;
            return this;
        }

        public Builder addDisplayView(ViewGroup viewGroup) {
            mAdViewContainer = viewGroup;
            return this;
        }

        public FacebookNativeAd build() {
            FacebookNativeAd adView = new FacebookNativeAd(context, unitId);
            adView.mIsMedia = adMedia;
            adView.mIsActionContainer = adActionContainer;
            adView.mOnAdLoadListener = mOnAdLoadListener;
            adView.checkViewConfig();
            if (mAdViewContainer != null) {
                adView.addView(mAdViewContainer);
            }
            return adView;
        }
    }
}
