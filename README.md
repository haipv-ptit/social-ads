# social-ads
Google admob, Facebook native ads

### Google Admob
**Banner**:<br/>
```java
BannerAdMob mBannerAdMob = new BannerAdMob(Context context, String unitId);
mBannerAdMob.addView(ViewGroup viewContainer);
mBannerAdMob.loadAd();
```
**Interstitial**:<br/>
```java
InterstitialAdMob mInterstitialAdMob = new InterstitialAdMob(Context context, String unitId);
mInterstitialAdMob.loadAd();
mInterstitialAdMob.show();
```
**RewardVideoAdMob**:<br/>
```java
RewardVideoAdMob mRewardVideoAdMob = new RewardVideoAdMob(Context context, String unitId);
mRewardVideoAdMob.loadAd();
mRewardVideoAdMob.show();
```
### Facebook Native Ads
**Banner**:<br/>
```java
FacebookNativeAd.Builder builder = new FacebookNativeAd.Builder(Context context, String unitId);
builder.addDisplayView(ViewGroup viewContainer);
FacebookNativeAd fbAds = builder.build();
fbAds.load();
```
**RecyclerView**:<br/>
```java
class NewsRecycleAdapter extends FbAdRecyclerAdapter {
    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        //TODO
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO
    }
}
```
