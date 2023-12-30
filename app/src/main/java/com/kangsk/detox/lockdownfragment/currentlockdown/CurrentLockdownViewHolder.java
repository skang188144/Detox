package com.kangsk.detox.lockdownfragment.currentlockdown;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class CurrentLockdownViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final TextView mTimeRemainingText;
    private final CircularProgressIndicator mCircularProgressIndicator;
    private LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public CurrentLockdownViewHolder(View itemView, LockdownManager lockdownManager, Context applicationContext) {
        super(itemView);
        mTimeRemainingText = itemView.findViewById(R.id.text_current_lockdown_time_remaining);
        mCircularProgressIndicator = itemView.findViewById(R.id.circular_progressbar_current_lockdown_current_lockdown);

        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
        Lockdown activeLockdown = mLockdownManager.getActiveLockdown();

        setTimeRemainingText(mTimeRemainingText, activeLockdown);
        setCircularProgressIndicatorProgress(mCircularProgressIndicator, activeLockdown);
    }

    private void setTimeRemainingText(TextView timeRemainingText, Lockdown lockdown) {
        if (lockdown == null) {
            timeRemainingText.setText("N/A");
            return;
        }

        timeRemainingText.setText(lockdown.getTimeRemainingString());
    }

    private void setCircularProgressIndicatorProgress(CircularProgressIndicator circularProgressIndicator, Lockdown lockdown) {
        if (lockdown == null) {
            circularProgressIndicator.setProgress(100);
            return;
        }

        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = lockdown.getStartTime();
        LocalTime endTime = lockdown.getEndTime();

        long lockdownDuration = startTime.until(endTime, ChronoUnit.SECONDS);
        long remainingTime = currentTime.until(endTime, ChronoUnit.SECONDS);
        int progress = (int) ((float) remainingTime / lockdownDuration * 100);

        circularProgressIndicator.setProgress(progress);
    }
}
