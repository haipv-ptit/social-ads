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
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

public class FacebookNativeAd extends AbstractAdListener implements NativeAdListener {
    private Context mContext;
    private String mUnitId;

    private NativeAd mNativeAd;
    private ViewGroup mAdView;
    private FbAdViewHolder mViewHolder;
    // private ViewGroup mAdViewContainer;

    private boolean mIsMedia = false;
    private boolean mIsActionContainer = false;

    private int mRetry;
    private OnAdLoadListener mOnAdLoadListener;

    private FacebookNativeAd(Context context, String unitId) {
        mContext = context;
        mUnitId = unitId;
        mRetry = 0;
        initFan();
    }

    private void initFan() {
        mAdView = (ViewGroup) View.inflate(mContext, R.layout.fb_native_ad, null);
        mViewHolder = new FbAdViewHolder();
        mViewHolder.ImvNativeAdIcon = mAdView.findViewById(R.id.native_ad_icon);
        mViewHolder.TvNativeAdTitle = mAdView.findViewById(R.id.native_ad_title);
        mViewHolder.MvNativeAdMedia = mAdView.findViewById(R.id.native_ad_media);
        mViewHolder.TvNativeAdSocialContext = mAdView.findViewById(R.id.native_ad_social_context);
        mViewHolder.TvNativeAdBody = mAdView.findViewById(R.id.native_ad_body);
        mViewHolder.TvNativeAdBodySub = mAdView.findViewById(R.id.native_ad_body_sub);
        mViewHolder.BtnNativeAdCallToAction = mAdView.findViewById(R.id.native_ad_call_to_action);
        mViewHolder.ViewNativeAdActionContainer = mAdView.findViewById(R.id.native_ad_action_container);
        mViewHolder.TvNativeAdSponsored = mAdView.findViewById(R.id.sponsored_label);
        mNativeAd = new NativeAd(mContext, mUnitId);
        mNativeAd.setAdListener(this);
    }

    private void checkViewConfig() {
        if (mIsMedia) {
            mViewHolder.MvNativeAdMedia.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.MvNativeAdMedia.setVisibility(View.GONE);
        }
        if (mIsActionContainer) {
            mViewHolder.TvNativeAdBodySub.setVisibility(View.GONE);
            mViewHolder.TvNativeAdSponsored.setVisibility(View.VISIBLE);
            mViewHolder.ViewNativeAdActionContainer.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.TvNativeAdBodySub.setVisibility(View.VISIBLE);
            mViewHolder.ViewNativeAdActionContainer.setVisibility(View.GONE);
            mViewHolder.TvNativeAdSponsored.setVisibility(View.GONE);
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
        mNativeAd = new NativeAd(mContext, mUnitId);
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
        mViewHolder.TvNativeAdTitle.setText(mNativeAd.getAdvertiserName());
        if (mIsActionContainer) {
            mViewHolder.TvNativeAdSocialContext.setText(mNativeAd.getAdSocialContext());
            mViewHolder.TvNativeAdBody.setText(mNativeAd.getAdBodyText());
            mViewHolder.BtnNativeAdCallToAction.setText(mNativeAd.getAdCallToAction());
        } else {
            mViewHolder.TvNativeAdBodySub.setText(mNativeAd.getAdBodyText());
        }

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(mContext, mNativeAd, true);
        adChoicesContainer.addView(adChoicesView);
        if (mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdLoaded();
        }
        mNativeAd.registerViewForInteraction(mAdView,  mViewHolder.MvNativeAdMedia, mViewHolder.ImvNativeAdIcon);
    }

    @Override
    public void onMediaDownloaded(Ad ad) {

    }

    private static class FbAdViewHolder {
        AdIconView ImvNativeAdIcon;
        TextView TvNativeAdTitle;
        TextView TvNativeAdSponsored;
        MediaView MvNativeAdMedia;
        TextView TvNativeAdSocialContext;
        TextView TvNativeAdBody;
        TextView TvNativeAdBodySub;
        View ViewNativeAdActionContainer;
        Button BtnNativeAdCallToAction;
    }

    public static class Builder {
        private final Context context;
        private final String unitId;

        private boolean adMedia = false;
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
