package com.kangsk.detox.lockdownfragment.currentlockdown;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

public class CurrentLockdownViewHolder extends RecyclerView.ViewHolder {

    private final TextView timeRemainingText;
    private final CircularProgressIndicator circularProgressIndicator;
    private LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    public CurrentLockdownViewHolder(View itemView, LockdownManager lockdownManager, Context applicationContext) {
        super(itemView);
        timeRemainingText = itemView.findViewById(R.id.text_current_lockdown_time_remaining);
        circularProgressIndicator = itemView.findViewById(R.id.circular_progressbar_current_lockdown_current_lockdown);
        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;

        int[] repeatDays = new int[1];
        repeatDays[0] = Calendar.MONDAY;

        ArrayList<String> blacklistedApps = new ArrayList<>();
        blacklistedApps.add("com.chess");

        Lockdown lockdown = new Lockdown("Test", System.currentTimeMillis(), System.currentTimeMillis() + 9999999, repeatDays, blacklistedApps, 1123123);
        mLockdownManager.addLockdown(lockdown);
    }

    public void bindModel() {
        Lockdown currentLockdown = mLockdownManager.getActiveLockdown();

        if (currentLockdown == null) {
            timeRemainingText.setText("N/A");
        }

        Duration lockdownDuration = Duration.ofMillis(currentLockdown.getEndTime() - currentLockdown.getStartTime());
        Duration timeRemainingDuration = Duration.ofMillis(currentLockdown.getEndTime() - System.currentTimeMillis());
        String timeRemainingString = timeRemainingDuration.toHoursPart() + "h " + timeRemainingDuration.toMinutesPart() + "m";
        float progress = (float) timeRemainingDuration.toMillis() / (float) lockdownDuration.toMillis() * 100f;

        timeRemainingText.setText(timeRemainingString);
        circularProgressIndicator.setProgress((int) progress);

    }
}
