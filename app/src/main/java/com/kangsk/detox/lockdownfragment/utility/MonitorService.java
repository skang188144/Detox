package com.kangsk.detox.lockdownfragment.utility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kangsk.detox.R;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class MonitorService extends AccessibilityService {

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        accessibilityServiceInfo.packageNames = getInstalledPackages();
        setServiceInfo(accessibilityServiceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (LockdownManager.getInstance(this).getActiveLockdown() == null) {
            return;
        }

        String packageName = event.getPackageName().toString();

        if (isAppBlacklisted(packageName)) {
            blockBlacklistedApp();
        }

        Log.d("MonitorService", "EVENT: Window State Changed!");
    }

    @Override
    public void onInterrupt() {

    }

    private boolean isAppBlacklisted(String packageName) {
        if (LockdownManager.getInstance(this).getActiveLockdown().getBlacklistedApps().contains(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    private void blockBlacklistedApp() {
        Lockdown lockdown = LockdownManager.getInstance(this).getActiveLockdown();

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.Theme_Detox))
                .setTitle("Lockdown")
                .setMessage(lockdown.getName() + " has blocked access to this app. It will expire in " + lockdown.getTimeRemainingString())
                .setIcon(R.mipmap.ic_launcher_no_text_foreground)
                .setPositiveButton("Dismiss", (dialog, which) -> {})
                .create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
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
