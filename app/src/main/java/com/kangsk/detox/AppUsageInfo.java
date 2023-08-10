package com.kangsk.detox;

import android.graphics.drawable.Drawable;

public class AppUsageInfo {
    /**
     * TODO:
     * - Properly encapsulate fields and implement getters/setters
     */
    public Drawable appIcon;
    public String appName, packageName;
    public long appUsageTime = 0;
    int launchCount;

    AppUsageInfo(String packageName) {
        this.packageName = packageName;
    }
}
