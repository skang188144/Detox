package com.kangsk.detox.lockdownfragment.utility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kangsk.detox.R;

import java.util.ArrayList;
import java.util.List;


public class MonitorService extends AccessibilityService {

    /*
     * PRIVATE FIELDS
     */
    private static boolean isLockdownOngoing = false;
    private WindowManager mWindowManager;
    private ConstraintLayout mLockdownMessageLayout;
    private WindowManager.LayoutParams mLockdownMessageLayoutParameters;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        // configure AccessibilityService
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        configureAccessibilityServiceInfo(accessibilityServiceInfo);
        setServiceInfo(accessibilityServiceInfo);

        // create WindowManager and blocked app warning View
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLockdownMessageLayout = new ConstraintLayout(this);

        // configure the blocked app warning View
        mLockdownMessageLayoutParameters = new WindowManager.LayoutParams();
        configureLockdownMessageParameters(mLockdownMessageLayoutParameters);

        // inflate the blocked app warning View
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.lockdown_message, mLockdownMessageLayout);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (isLockdownOngoing) {
            blockBlacklistedApps(event);
        }
        Log.d("MonitorService", event.getPackageName() + " " + event.toString());
    }

    @Override
    public void onInterrupt() {

    }

    public static void startLockdown() {
        isLockdownOngoing = true;
    }

    public static void endLockdown() {
        isLockdownOngoing = false;
    }

    private void blockBlacklistedApps(AccessibilityEvent event) {
        LockdownManager lockdownManager = LockdownManager.getInstance(this);
        Lockdown lockdown = lockdownManager.getCurrentLockdown();
        ArrayList<String> blacklistedApps = lockdown.getBlacklistedApps();

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (mLockdownMessageLayout.isShown()) {
                return;
            }

            for (String blacklistedApp : blacklistedApps) {
                if (event.getPackageName().equals((blacklistedApp))) {
                    showLockdownMessage();
                }
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            if (mLockdownMessageLayout.isShown()) {
                hideLockdownMessage();
            }
        }
    }

    private void showLockdownMessage() {
        mWindowManager.addView(mLockdownMessageLayout, mLockdownMessageLayoutParameters);
    }

    private void hideLockdownMessage() {
        mWindowManager.removeView(mLockdownMessageLayout);
    }






    private void configureAccessibilityServiceInfo(AccessibilityServiceInfo accessibilityServiceInfo) {
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_SELECTED;
        accessibilityServiceInfo.packageNames = getInstalledPackages();
    }

    private void configureLockdownMessageParameters(WindowManager.LayoutParams lockdownMessageLayoutParameters) {
        lockdownMessageLayoutParameters.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lockdownMessageLayoutParameters.format = PixelFormat.TRANSLUCENT;
        lockdownMessageLayoutParameters.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lockdownMessageLayoutParameters.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lockdownMessageLayoutParameters.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lockdownMessageLayoutParameters.gravity = Gravity.CENTER;
    }

    private String[] getInstalledPackages() {
        PackageManager packageManager = getApplication().getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String[] installedPackageStringArray = new String[installedApplications.size()];

        for (int i = 0; i < installedApplications.size(); i++) {
            installedPackageStringArray[i] = installedApplications.get(i).packageName;
            Log.d("TestMonitorService ", installedApplications.get(i).packageName);
        }

        return installedPackageStringArray;
    }
}
