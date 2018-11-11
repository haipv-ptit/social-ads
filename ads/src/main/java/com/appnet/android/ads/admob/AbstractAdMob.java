package com.appnet.android.ads.admob;

import com.appnet.android.ads.OnAdLoadListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public abstract class AbstractAdMob extends AdListener {
    private AdRequest mAdRequest;
    private AdRequest.Builder mBuilder;
    protected OnAdLoadListener mOnAdLoadListener;

    AbstractAdMob() {
        mBuilder = new AdRequest.Builder();
        mBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
    }

    AdRequest getAdRequest() {
        if(mAdRequest == null) {
            mAdRequest = mBuilder.build();
        }
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

    public void addTestDevice(String device) {
        mBuilder.addTestDevice(device);
    }
}
