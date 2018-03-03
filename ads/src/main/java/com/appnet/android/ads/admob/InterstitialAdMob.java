package com.appnet.android.ads.admob;

import android.content.Context;

import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdMob extends AbstractAdMob {
    private InterstitialAd mInterstitialAd;

    public InterstitialAdMob(Context context, String unitId) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(unitId);
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        loadAd();
    }

    @Override
    public void loadAd() {
        mInterstitialAd.loadAd(getAdRequest());
    }

    public void show() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
