package com.appnet.android.ads.admob;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class RewardVideoAdMob extends AbstractAdMob implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private String mUnitId;

    public RewardVideoAdMob(Context context, String unitId) {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mUnitId = unitId;
    }

    @Override
    public void loadAd() {
        mRewardedVideoAd.loadAd(mUnitId, getAdRequest());
    }

    public void show() {
        if(mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if(mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdLoaded();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadAd();
        if(mOnAdLoadListener != null) {
            mOnAdLoadListener.onAdFailed();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
