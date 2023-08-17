package com.kangsk.detox.utility;

import android.graphics.drawable.Drawable;

public class AppUsageData {
    public Drawable appIcon;
    public String appName, packageName;
    public long appUsageTime = 0;

    public AppUsageData(String packageName) {
        this.packageName = packageName;
    }
}
