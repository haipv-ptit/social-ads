package com.appnet.android.ads.fb;

import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;

public final class FbAdHelper {
    public static void initialize(Context context) {
        AudienceNetworkAds.initialize(context);
    }
}
