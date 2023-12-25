package com.kangsk.detox.lockdownfragment.utility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kangsk.detox.R;

import java.util.List;


public class MonitorService extends AccessibilityService implements View.OnTouchListener {

    /*
     * PRIVATE FIELDS
     */
    private WindowManager mWindowManager;
    private ConstraintLayout mLockdownMessageLayout;
    private WindowManager.LayoutParams mLockdownMessageLayoutParameters;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        // configure AccessibilityService
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        accessibilityServiceInfo.packageNames = getInstalledPackages();
        setServiceInfo(accessibilityServiceInfo);


        // create WindowManager and blocked app warning View
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLockdownMessageLayout = new ConstraintLayout(this);

        // configure the blocked app warning View
        mLockdownMessageLayoutParameters = new WindowManager.LayoutParams();
        mLockdownMessageLayoutParameters.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        //mLockdownMessageLayoutParameters.format = PixelFormat.TRANSLUCENT;
        mLockdownMessageLayoutParameters.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLockdownMessageLayoutParameters.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLockdownMessageLayoutParameters.height = WindowManager.LayoutParams.MATCH_PARENT;


        // inflate the blocked app warning View
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.lockdown_message, mLockdownMessageLayout);
        mLockdownMessageLayout.setOnTouchListener(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (LockdownManager.getInstance(this).getActiveLockdown() == null) {
            return;
        }

        String packageName = event.getPackageName().toString();

        if (isAppBlacklisted(packageName)) {
            blockBlacklistedApp(packageName);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mLockdownMessageLayout.isShown()) {
            //hideLockdownMessage();
            return true;
        }
        return false;
    }

    private boolean isAppBlacklisted(String packageName) {
        if (LockdownManager.getInstance(this).getActiveLockdown().getBlacklistedApps().contains(packageName)) {
            return true;
        }

        return false;
    }

    private void blockBlacklistedApp(String packageName) {
        if (mLockdownMessageLayout.isShown()) {
            return;
        }

        showLockdownMessage();
    }

    private void showLockdownMessage() {
        mWindowManager.addView(mLockdownMessageLayout, mLockdownMessageLayoutParameters);
    }

    private void hideLockdownMessage() {
        mWindowManager.removeView(mLockdownMessageLayout);
    }

    private String[] getInstalledPackages() {
        PackageManager packageManager = this.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String[] installedPackages = new String[installedApplications.size()];

        for (int i = 0; i < installedApplications.size(); i++) {
            installedPackages[i] = installedApplications.get(i).packageName;
        }

        return installedPackages;
    }
}
