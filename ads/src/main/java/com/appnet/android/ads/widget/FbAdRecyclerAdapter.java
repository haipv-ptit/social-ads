package com.appnet.android.ads.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appnet.android.ads.R;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

public abstract class FbAdRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements NativeAdsManager.Listener {
    private static final int TYPE_FB_AD = 2000;
    private static final int NEXT_FB_AD = 4;
    private NativeAdsManager mAdManager;

    protected Context mContext;
    protected final List<T> mData;

    public FbAdRecyclerAdapter(Context context, List<T> data, String unitId) {
        mContext = context;
        mData = data;
        mAdManager = new NativeAdsManager(mContext, unitId, 4);
        mAdManager.setListener(this);
    }

    public FbAdRecyclerAdapter(Context context, String unitId) {
        mContext = context;
        mData = new ArrayList<>();
        mAdManager = new NativeAdsManager(mContext, unitId, 4);
        mAdManager.setListener(this);
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FB_AD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fb_native_ad, parent, false);
            return new FbAdRecyclerViewHolder(view);
        }
        return onCreateItemViewHolder(parent, viewType);
    }

    protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        notifyDataSetChanged();
    }

    @Override
    public void onAdError(AdError adError) {

    }

    public T getItem(int position) {
        if (position >= getItemCount()) {
            return null;
        }
        if (mData == null || mData.isEmpty() || isAdIndex(position)) {
            return null;
        }
        int index = position - (position / NEXT_FB_AD);
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
        if (mData == null || mData.isEmpty() || mData.size() < NEXT_FB_AD) {
            return false;
        } else if (position % NEXT_FB_AD == NEXT_FB_AD - 1 && mData.size() >= NEXT_FB_AD) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.isEmpty()) {
            return 0;
        } else if (mData.size() < NEXT_FB_AD) {
            return mData.size();
        } else {
            return mData.size() + mData.size() / NEXT_FB_AD;
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
        private ImageView ImvNativeAdIcon;
        private TextView TvNativeAdTitle;
        private TextView TvNativeAdSponsored;
        private MediaView MvNativeAdMedia;
        private TextView TvNativeAdSocialContext;
        private TextView TvNativeAdBody;
        private TextView TvNativeAdBodySub;
        private View ViewNativeAdActionContainer;
        private Button BtnNativeAdCallToAction;
        private ViewGroup ViewAdChoicesContainer;

        FbAdRecyclerViewHolder(View view) {
            super(view);
            ImvNativeAdIcon = (ImageView) itemView.findViewById(R.id.native_ad_icon);
            TvNativeAdTitle = (TextView) itemView.findViewById(R.id.native_ad_title);
            MvNativeAdMedia = (MediaView) itemView.findViewById(R.id.native_ad_media);
            TvNativeAdSocialContext = (TextView) itemView.findViewById(R.id.native_ad_social_context);
            TvNativeAdBody = (TextView) itemView.findViewById(R.id.native_ad_body);
            TvNativeAdBodySub = (TextView) itemView.findViewById(R.id.native_ad_body_sub);
            BtnNativeAdCallToAction = (Button) itemView.findViewById(R.id.native_ad_call_to_action);
            ViewNativeAdActionContainer = itemView.findViewById(R.id.native_ad_action_container);
            TvNativeAdSponsored = (TextView) itemView.findViewById(R.id.sponsored_label);
            ViewAdChoicesContainer = (ViewGroup) itemView.findViewById(R.id.ad_choices_container);
        }

        private void bindView(Context context, NativeAd ad) {
            if (ad != null) {
                setVisible(true);
                ad.unregisterView();
                TvNativeAdTitle.setText(ad.getAdTitle());
                //  if (mIsActionContainer) {
                TvNativeAdSocialContext.setText(ad.getAdSocialContext());
                TvNativeAdBody.setText(ad.getAdBody());
                BtnNativeAdCallToAction.setText(ad.getAdCallToAction());
                //  } else {
                //      mViewHolder.TvNativeAdBodySub.setText(ad.getAdBody());
                //  }

                // Download and display the ad icon.
                NativeAd.Image adIcon = ad.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, ImvNativeAdIcon);

                // Download and display the cover image.
                // if (mIsMedia) {
                MvNativeAdMedia.setNativeAd(ad);
                // }

                // Add the AdChoices icon
                ViewAdChoicesContainer.removeAllViews();
                AdChoicesView adChoicesView = new AdChoicesView(context, ad, true);
                ViewAdChoicesContainer.addView(adChoicesView);
                ad.registerViewForInteraction(itemView);
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