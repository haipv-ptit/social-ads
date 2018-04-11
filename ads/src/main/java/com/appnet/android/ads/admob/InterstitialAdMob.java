package com.appnet.android.ads.admob;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdMob extends AbstractAdMob {
    private static final int MAX_LAUNCH_TIMES = 5;
    private static final String PREFS_NAME = "admob_config";
    private static final String KEY_LAUNCH_TIMES = "KEY_LAUNCH_TIMES";

    private SharedPreferences mPrefs;

    private InterstitialAd mInterstitialAd;

    public InterstitialAdMob(Context context, String unitId) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(unitId);
        mPrefs =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

    public boolean show() {
        boolean isShow = false;
        if(mInterstitialAd.isLoaded()) {
            int times = getLaunchTimes();
            if(times >= MAX_LAUNCH_TIMES) {
                times = 1;
                isShow = true;
                mInterstitialAd.show();
            } else {
                times++;
            }
            registerLaunchTimes(times);
        }
        return isShow;
    }

    private void registerLaunchTimes(int times) {
        mPrefs.edit().putInt(KEY_LAUNCH_TIMES, times).apply();
    }

    private int getLaunchTimes() {
        return mPrefs.getInt(KEY_LAUNCH_TIMES, 1);
    }
}
