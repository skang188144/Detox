package com.kangsk.detox;

import android.graphics.drawable.Drawable;

public class AppUsageInfo {
    Drawable appIcon;
    String appName, packageName;
    long appUsageTime = 0;
    int launchCount;

    AppUsageInfo(String packageName) {
        this.packageName = packageName;
    }
}
