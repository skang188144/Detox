package com.kangsk.detox.lockdown.activelockdown;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.Lockdown;
import com.kangsk.detox.utility.LockdownManager;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ActiveLockdownViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final LockdownManager mLockdownManager;

    private final TextView mLockdownTimeRemainingText;
    private final CircularProgressIndicator mLockdownTimeRemainingIndicator;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public ActiveLockdownViewHolder(View itemView, LockdownManager lockdownManager) {
        super(itemView);

        mLockdownManager = lockdownManager;

        mLockdownTimeRemainingText = itemView.findViewById(R.id.text_active_lockdown_item_time_remaining);
        mLockdownTimeRemainingIndicator = itemView.findViewById(R.id.progress_indicator_active_lockdown_item);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
        Lockdown activeLockdown = mLockdownManager.getActiveLockdown();
        setLockdownTimeRemainingText(activeLockdown, mLockdownTimeRemainingText);
        setLockdownTimeRemainingIndicatorProgress(activeLockdown, mLockdownTimeRemainingIndicator);
    }

    /*
     * setLockdownTimeRemainingText: takes a lockdown as a parameter and sets the time remaining text
     */
    private void setLockdownTimeRemainingText(Lockdown lockdown, TextView lockdownTimeRemainingText) {
        // if lockdown is null, there is no active lockdown
        if (lockdown == null) {
            lockdownTimeRemainingText.setText("N/A");
            return;
        }

        lockdownTimeRemainingText.setText(lockdown.getTimeRemainingString());
    }

    /*
     * setLockdownTimeRemainingIndicatorProgress: takes a lockdown as a parameter and sets the time remaining progress indicator
     */
    private void setLockdownTimeRemainingIndicatorProgress(Lockdown lockdown, CircularProgressIndicator lockdownTimeRemainingIndicator) {
        // if lockdown is null, there is no active lockdown
        if (lockdown == null) {
            lockdownTimeRemainingIndicator.setProgress(100);
            return;
        }

        // fetch current time, and lockdown start/end time
        LocalTime currentTime = LocalTime.now();
        LocalTime lockdownStartTime = lockdown.getStartTime();
        LocalTime lockdownEndTime = lockdown.getEndTime();

        // calculate lockdown duration and lockdown remaining time
        long lockdownDurationInSeconds = lockdownStartTime.until(lockdownEndTime, ChronoUnit.SECONDS);
        long lockdownRemainingTimeInSeconds = currentTime.until(lockdownEndTime, ChronoUnit.SECONDS);

        // calculate lockdown elapsed time as a percentage
        int lockdownProgressPercentage = (int) ((float) lockdownRemainingTimeInSeconds / lockdownDurationInSeconds * 100);

        lockdownTimeRemainingIndicator.setProgress(lockdownProgressPercentage);
    }
}
