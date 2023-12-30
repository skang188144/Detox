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
import java.util.ArrayList;
import java.util.Calendar;

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
    }

    private void setTimeRemainingText(TextView timeRemainingText, Lockdown lockdown) {
        if (lockdown == null) {
            timeRemainingText.setText("N/A");
            return;
        }

        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = lockdown.getEndTime();

        long remainingHours = currentTime.until(endTime, ChronoUnit.HOURS);
        long remainingMinutes = currentTime.until(endTime, ChronoUnit.MINUTES) % 60;

        String hourLabelString;
        String timeRemainingString = "";

        if (remainingHours > 1) {
            hourLabelString = "hrs.";
        } else {
            hourLabelString = "hr.";
        }

        if (remainingHours > 0) {
            timeRemainingString += (remainingHours + " " + hourLabelString);
        }
        timeRemainingString += (" " + remainingMinutes + " min.");

        timeRemainingText.setText(timeRemainingString);
    }
}
