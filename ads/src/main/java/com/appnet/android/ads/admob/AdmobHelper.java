package com.appnet.android.ads.admob;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;

public final class AdmobHelper {
    public static void initialize(Context context, String appId) {
        MobileAds.initialize(context, appId);
    }
}
