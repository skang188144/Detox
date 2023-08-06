package com.kangsk.detox;

import android.graphics.drawable.Drawable;

public class AppUsageInfo {
    Drawable appIcon; // You may add get this usage data also, if you wish.
    String appName, packageName;
    long screenOnTime;
    int launchCount;

    AppUsageInfo(String packageName) {
        this.packageName = packageName;
    }
}
