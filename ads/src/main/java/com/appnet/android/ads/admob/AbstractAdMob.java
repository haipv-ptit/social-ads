package com.appnet.android.ads.admob;

import com.appnet.android.ads.OnAdLoadListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

abstract class AbstractAdMob extends AdListener {
    private AdRequest mAdRequest;
    protected OnAdLoadListener mOnAdLoadListener;

    AbstractAdMob() {
        mAdRequest = new AdRequest.Builder().build();
    }

    AdRequest getAdRequest() {
        return mAdRequest;
    }

    public void setOnLoadListener(OnAdLoadListener listener) {
        mOnAdLoadListener = listener;
    }

    @Override
    public void onAdFailedToLoad(int i) {
        if(mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdFailed();
        }
    }

    @Override
    public void onAdLoaded() {
        if(mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdLoaded();
        }
    }

    public abstract void loadAd();
}
