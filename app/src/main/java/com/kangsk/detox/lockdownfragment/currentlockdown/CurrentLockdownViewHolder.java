package com.kangsk.detox.lockdownfragment.currentlockdown;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

import java.time.Duration;
import java.util.ArrayList;

public class CurrentLockdownViewHolder extends RecyclerView.ViewHolder {

    private final TextView timeRemainingText;
    private final Context mApplicationContext;

    public CurrentLockdownViewHolder(View itemView, Context applicationContext) {
        super(itemView);
        timeRemainingText = itemView.findViewById(R.id.text_current_lockdown_time_remaining);
        mApplicationContext = applicationContext;
    }

    public void bindModel() {
        LockdownManager lockdownManager = LockdownManager.getInstance(mApplicationContext);
        String name = "Test Lockdown";
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 6400000;
        ArrayList<String> blacklistedApps = new ArrayList<>();
        blacklistedApps.add("notion.id");
        blacklistedApps.add("com.chess");
        int pendingRequestCode = 0;
        Lockdown currentLockdown = new Lockdown(name, startTime, endTime, null, blacklistedApps, pendingRequestCode);

        lockdownManager.addLockdown(currentLockdown);

        if (currentLockdown == null) {
            timeRemainingText.setText("0h 0m");
        } else {
            Duration lockdownDuration = Duration.ofMillis(currentLockdown.getEndTimeInMillis() - currentLockdown.getStartTimeInMillis());
            String timeRemainingString = lockdownDuration.toHoursPart() + "h " + lockdownDuration.toMinutesPart() + "m";
            timeRemainingText.setText(timeRemainingString);
        }

    }
}
