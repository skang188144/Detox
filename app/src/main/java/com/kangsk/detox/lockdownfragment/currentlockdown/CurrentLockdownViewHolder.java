package com.kangsk.detox.lockdownfragment.currentlockdown;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

import java.time.Duration;

public class CurrentLockdownViewHolder extends RecyclerView.ViewHolder {

    private final TextView timeRemainingText;
    private LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    public CurrentLockdownViewHolder(View itemView, LockdownManager lockdownManager, Context applicationContext) {
        super(itemView);
        timeRemainingText = itemView.findViewById(R.id.text_current_lockdown_time_remaining);
        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;
    }

    public void bindModel() {
        Lockdown currentLockdown = mLockdownManager.getActiveLockdown();

        if (currentLockdown == null) {
            timeRemainingText.setText("0h 0m");
        } else {
            Duration lockdownDuration = Duration.ofMillis(currentLockdown.getEndTime() - currentLockdown.getStartTime());
            String timeRemainingString = lockdownDuration.toHoursPart() + "h " + lockdownDuration.toMinutesPart() + "m";
            timeRemainingText.setText(timeRemainingString);
        }

    }
}
