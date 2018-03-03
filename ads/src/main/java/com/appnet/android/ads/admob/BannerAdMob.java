package com.appnet.android.ads.admob;


import android.content.Context;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class BannerAdMob extends AbstractAdMob {
    private AdView mAdView;

    public BannerAdMob(Context context, String unitId) {
        super();
        mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(unitId);
        mAdView.setAdListener(this);
    }

    public void addView(ViewGroup viewGroup) {
        viewGroup.addView(mAdView);
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        loadAd();
    }

    @Override
    public void loadAd() {
        mAdView.loadAd(getAdRequest());
    }
}
