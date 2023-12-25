package com.kangsk.detox.lockdownfragment.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class EndLockdownBroadcastReceiver extends BroadcastReceiver {

    /*
     * PRIVATE FIELDS
     */
    private Lockdown mLockdown;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("LockdownBundle");
        mLockdown = (Lockdown) bundle.get("Lockdown");
//        MonitorService.endLockdown();
    }
}
