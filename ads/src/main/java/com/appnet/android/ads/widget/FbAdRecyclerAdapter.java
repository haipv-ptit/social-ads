package com.appnet.android.ads.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnet.android.ads.R;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

public abstract class FbAdRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements NativeAdsManager.Listener {
    private static final int TYPE_FB_AD = 2000;
    private static final int NUMBER_FB_AD = 4;
    private static final int MAX_FB_ADS = 4;
    private NativeAdsManager mAdManager;
    private boolean mAdVisibled = true;
    private boolean mIsActionVisible = false;

    protected Context mContext;
    protected final List<T> mData;
    private int mAdSteps = NUMBER_FB_AD;

    public FbAdRecyclerAdapter(Context context, List<T> data, String unitId) {
        mContext = context;
        mData = data;
        init(unitId, MAX_FB_ADS);
    }

    public FbAdRecyclerAdapter(Context context, String unitId) {
        mContext = context;
        mData = new ArrayList<>();
        init(unitId, MAX_FB_ADS);
    }

    public FbAdRecyclerAdapter(Context context, String unitId, int maxAds) {
        mContext = context;
        mData = new ArrayList<>();
        init(unitId, maxAds);
    }

    public FbAdRecyclerAdapter(Context context, String unitId, int maxAds, boolean isActionVisible) {
        mContext = context;
        mData = new ArrayList<>();
        mIsActionVisible = isActionVisible;
        init(unitId, maxAds);
    }

    private void init(String unitId, int maxAds) {
        int mMaxFbAds;
        if(maxAds > 10) {
            mMaxFbAds = 10;
        } else if (maxAds <= 0) {
            mMaxFbAds = 0;
            mAdVisibled = false;
        } else {
            mMaxFbAds = maxAds;
        }
        mAdManager = new NativeAdsManager(mContext, unitId, mMaxFbAds);
        mAdManager.setListener(this);
    }

    public void setStepAds(int stepFbAds) {
         if (stepFbAds < 2) {
            mAdSteps = 2;
        }
        mAdSteps = stepFbAds;
    }

    public void loadAd() {
        mAdManager.loadAds();
    }

    @Override
    public int getItemViewType(int position) {
        if (isAdIndex(position)) {
            return TYPE_FB_AD;
        }
        return getViewType(position);
    }

    protected abstract int getViewType(int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FB_AD) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            NativeAdLayout view = (NativeAdLayout)inflater.inflate(R.layout.fb_native_ad, parent, false);
            return new FbAdRecyclerViewHolder(view);
        }
        return onCreateItemViewHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_FB_AD) {
            if (mAdManager != null && mAdManager.isLoaded()) {
                ((FbAdRecyclerViewHolder) holder).bindView(mContext, mAdManager.nextNativeAd());
            } else {
                ((FbAdRecyclerViewHolder) holder).bindView(mContext, null);
            }
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onAdsLoaded() {
        mAdVisibled = true;
        notifyDataSetChanged();
    }

    @Override
    public void onAdError(AdError adError) {
        mAdVisibled = false;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (position >= getItemCount()) {
            return null;
        }
        if (mData == null || mData.isEmpty() || isAdIndex(position)) {
            return null;
        }
        if(!mAdVisibled && position < mData.size()) {
            return mData.get(position);
        }
        int index = position - (position / mAdSteps);
        return mData.get(index);
    }

    public List<T> getNextItems(int position, int count) {
        List<T> data = new ArrayList<>();
        int size = getItemCount();
        int index = position + 1;
        int k = 0;
        while (index < size && k < count) {
            T item = getItem(index);
            if (item != null) {
                data.add(item);
                k++;
            }
            index++;
        }
        return data;
    }

    private boolean isAdIndex(int position) {
        if (mData == null || mData.isEmpty() || !mAdVisibled || mData.size() < mAdSteps ) {
            return false;
        } else if (position % mAdSteps == mAdSteps - 1 && mData.size() >= mAdSteps) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.isEmpty()) {
            return 0;
        } else if (!mAdVisibled || mData.size() < mAdSteps) {
            return mData.size();
        } else {
            return mData.size() + mData.size() / mAdSteps;
        }
    }

    public int getDataSize() {
        if (mData == null || mData.isEmpty()) {
            return 0;
        } else {
            return mData.size();
        }
    }

    private static class FbAdRecyclerViewHolder extends RecyclerView.ViewHolder {
        private MediaView ImvNativeAdIcon;
        private TextView TvNativeAdTitle;
        //private TextView TvNativeAdSponsored;
        private MediaView MvNativeAdMedia;
        private TextView TvNativeAdSocialContext;
        private TextView TvNativeAdBody;
       //private TextView TvNativeAdBodySub;
        private ViewGroup ViewNativeAdActionContainer;
        private Button BtnNativeAdCallToAction;
        private ViewGroup ViewAdChoicesContainer;

        private boolean mIsActionVisible = false;

        FbAdRecyclerViewHolder(NativeAdLayout view) {
            super(view);
            init(view, false);
        }

        FbAdRecyclerViewHolder(NativeAdLayout view, boolean isActionVisible) {
            super(view);
            init(view, isActionVisible);
        }

        private void init(NativeAdLayout view, boolean isActionVisible) {
            mIsActionVisible = isActionVisible;
            ImvNativeAdIcon = itemView.findViewById(R.id.native_ad_icon);
            TvNativeAdTitle = itemView.findViewById(R.id.native_ad_title);
            MvNativeAdMedia = itemView.findViewById(R.id.native_ad_media);
            TvNativeAdSocialContext = itemView.findViewById(R.id.native_ad_social_context);
            TvNativeAdBody = itemView.findViewById(R.id.native_ad_body);
            //TvNativeAdBodySub = itemView.findViewById(R.id.native_ad_body_sub);
            BtnNativeAdCallToAction = itemView.findViewById(R.id.native_ad_call_to_action);
            ViewNativeAdActionContainer = itemView.findViewById(R.id.native_ad_action_container);
            //TvNativeAdSponsored = itemView.findViewById(R.id.sponsored_label);
            ViewAdChoicesContainer = itemView.findViewById(R.id.ad_choices_container);
        }

        private void bindView(Context context, NativeAd ad) {
            if (ad != null) {
                setVisible(true);
                ad.unregisterView();
                TvNativeAdTitle.setText(ad.getAdvertiserName());
                //  if (mIsActionContainer) {
                TvNativeAdSocialContext.setText(ad.getAdSocialContext());
                TvNativeAdBody.setText(ad.getAdBodyText());
                BtnNativeAdCallToAction.setText(ad.getAdCallToAction());
                //  } else {
                //      mViewHolder.TvNativeAdBodySub.setText(ad.getAdBody());
                //  }

                NativeAdLayout nativeAdLayout = (NativeAdLayout) itemView;
                // Add the AdChoices icon
                ViewAdChoicesContainer.removeAllViews();
                AdOptionsView adChoicesView = new  AdOptionsView(context, ad, nativeAdLayout);
                ViewAdChoicesContainer.addView(adChoicesView);
                ad.registerViewForInteraction(itemView, MvNativeAdMedia, ImvNativeAdIcon);
                if(mIsActionVisible) {
                    ViewNativeAdActionContainer.setVisibility(View.VISIBLE);
                } else {
                    ViewNativeAdActionContainer.setVisibility(View.GONE);
                }
            } else {
                setVisible(false);
            }
        }

        private void setVisible(boolean isAd) {
            int visible = (isAd) ? View.VISIBLE : View.GONE;
            ImvNativeAdIcon.setVisibility(visible);
            TvNativeAdTitle.setVisibility(visible);
            MvNativeAdMedia.setVisibility(visible);
            ViewNativeAdActionContainer.setVisibility(visible);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }
}

/***
 k = 4
 position: 0  1  2  3  4  5  6  7  8  9  10  11
 i:        0  1  2  X  3  4  5  X  6  7   8   X
 adIndex = pos%k == k-1
 index = position - (position/k)
 ***/