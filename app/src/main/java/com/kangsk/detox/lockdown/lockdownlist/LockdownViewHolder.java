package com.kangsk.detox.lockdown.lockdownlist;

import android.graphics.Typeface;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.Lockdown;
import com.kangsk.detox.utility.LockdownManager;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class LockdownViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    /*
     * PRIVATE FIELDS
     */
    private final LockdownManager mLockdownManager;
    private Lockdown mLockdown;

    private final MaterialCardView mContainerCard;

    // LOCKDOWN START/END TEXT
    private final TextView mLockdownStartTimeText;
    private final TextView mLockdownStartAMPMText;
    private final TextView mLockdownEndTimeText;
    private final TextView mLockdownEndAMPMText;

    // LOCKDOWN NAME TEXT
    private final TextView mLockdownNameText;

    // LOCKDOWN REPEATING DAY TEXT
    private final TextView mLockdownRepeatSundayText;
    private final TextView mLockdownRepeatMondayText;
    private final TextView mLockdownRepeatTuesdayText;
    private final TextView mLockdownRepeatWednesdayText;
    private final TextView mLockdownRepeatThursdayText;
    private final TextView mLockdownRepeatFridayText;
    private final TextView mLockdownRepeatSaturdayText;

    // LOCKDOWN REPEATING DAY INDICATORS
    private final TextView mLockdownRepeatSundayIndicator;
    private final TextView mLockdownRepeatMondayIndicator;
    private final TextView mLockdownRepeatTuesdayIndicator;
    private final TextView mLockdownRepeatWednesdayIndicator;
    private final TextView mLockdownRepeatThursdayIndicator;
    private final TextView mLockdownRepeatFridayIndicator;
    private final TextView mLockdownRepeatSaturdayIndicator;

    // LOCKDOWN ENABLE/DISABLE SWITCH
    private final MaterialSwitch mLockdownToggleSwitch;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownViewHolder(View itemView, LockdownManager lockdownManager) {
        super(itemView);

        mLockdownManager = lockdownManager;

        mContainerCard = itemView.findViewById(R.id.card_lockdown_item);
        mContainerCard.setOnClickListener(this);

        mLockdownStartTimeText = itemView.findViewById(R.id.text_lockdown_item_start_time);
        mLockdownEndTimeText = itemView.findViewById(R.id.text_lockdown_item_end_time);
        mLockdownStartAMPMText = itemView.findViewById(R.id.text_lockdown_item_start_ampm);
        mLockdownEndAMPMText = itemView.findViewById(R.id.text_lockdown_item_end_ampm);

        mLockdownNameText = itemView.findViewById(R.id.text_lockdown_item_name);

        mLockdownRepeatSundayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_sunday);
        mLockdownRepeatMondayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_monday);
        mLockdownRepeatTuesdayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_tuesday);
        mLockdownRepeatWednesdayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_wednesday);
        mLockdownRepeatThursdayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_thursday);
        mLockdownRepeatFridayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_friday);
        mLockdownRepeatSaturdayText = itemView.findViewById(R.id.text_lockdown_item_repeat_day_saturday);

        mLockdownRepeatSundayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_sunday);
        mLockdownRepeatMondayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_monday);
        mLockdownRepeatTuesdayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_tuesday);
        mLockdownRepeatWednesdayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_wednesday);
        mLockdownRepeatThursdayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_thursday);
        mLockdownRepeatFridayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_friday);
        mLockdownRepeatSaturdayIndicator = itemView.findViewById(R.id.indicator_lockdown_item_repeat_day_saturday);

        mLockdownToggleSwitch = itemView.findViewById(R.id.switch_lockdown_item);
        mLockdownToggleSwitch.setOnCheckedChangeListener(this);
    }

    /*
     * onClick: listener called when the '' button is clicked.
     */
    @Override
    public void onClick(View view) {
        // TODO: add logic for when the container MaterialCardView is clicked
    }

    /*
     * onCheckChanged: listener called when the 'toggle lockdown' switch is clicked.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mLockdown.setEnabled(true);
        } else {
            mLockdown.setEnabled(false);
        }
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel(int position) {
        mLockdown = mLockdownManager.getLockdownList().get(position);

        setLockdownStartTimeText(mLockdown, mLockdownStartTimeText, mLockdownStartAMPMText);
        setLockdownEndTimeText(mLockdown, mLockdownEndTimeText, mLockdownEndAMPMText);
        setLockdownNameText(mLockdown, mLockdownNameText);
        setLockdownRepeatDaysText(mLockdown, mLockdownRepeatSundayText, mLockdownRepeatSundayIndicator,
                mLockdownRepeatMondayText, mLockdownRepeatMondayIndicator,
                mLockdownRepeatTuesdayText, mLockdownRepeatTuesdayIndicator,
                mLockdownRepeatWednesdayText, mLockdownRepeatWednesdayIndicator,
                mLockdownRepeatThursdayText, mLockdownRepeatThursdayIndicator,
                mLockdownRepeatFridayText, mLockdownRepeatFridayIndicator,
                mLockdownRepeatSaturdayText, mLockdownRepeatSaturdayIndicator);
        initializeLockdownToggleSwitch(mLockdown, mLockdownToggleSwitch);
    }

    /*
     * setLockdownStartTimeText: retrieve the hour/minute/AMPM of the lockdown's scheduled start time, create a
     * formatted string, and set the text of the lockdown start time TextViews.
     */
    private void setLockdownStartTimeText(Lockdown lockdown, TextView lockdownStartTimeText, TextView lockdownStartAMPMText) {
        LocalTime lockdownStartTime = lockdown.getStartTime();

        int lockdownStartHour = lockdownStartTime.get(ChronoField.CLOCK_HOUR_OF_AMPM);
        int lockdownStartMinute = lockdownStartTime.get(ChronoField.MINUTE_OF_HOUR);
        String lockdownStartAMPM;

        if (lockdownStartTime.get(ChronoField.AMPM_OF_DAY) == Calendar.AM) {
            lockdownStartAMPM = "AM";
        } else {
            lockdownStartAMPM = "PM";
        }

        lockdownStartTimeText.setText(String.format("%2d", lockdownStartHour) + ":" + String.format("%02d", lockdownStartMinute));
        lockdownStartAMPMText.setText(lockdownStartAMPM);
    }

    /*
     * setLockdownEndTimeText: retrieve the hour/minute/AMPM of the lockdown's scheduled end time, create a
     * formatted string, and set the text of the lockdown end time TextViews.
     */
    private void setLockdownEndTimeText(Lockdown lockdown, TextView lockdownEndTimeText, TextView lockdownEndAMPMText) {
        LocalTime lockdownEndTime = lockdown.getEndTime();

        int lockdownEndHour = lockdownEndTime.get(ChronoField.CLOCK_HOUR_OF_AMPM);
        int lockdownEndMinute = lockdownEndTime.get(ChronoField.MINUTE_OF_HOUR);
        String lockdownEndAMPM;

        if (lockdownEndTime.get(ChronoField.AMPM_OF_DAY) == Calendar.AM) {
            lockdownEndAMPM = "AM";
        } else {
            lockdownEndAMPM = "PM";
        }

        lockdownEndTimeText.setText(String.format("%2d", lockdownEndHour) + ":" + String.format("%02d", lockdownEndMinute));
        lockdownEndAMPMText.setText(lockdownEndAMPM);
    }

    /*
     * setLockdownNameText: retrieve the lockdown name and set the text of the lockdown name TextView.
     */
    private void setLockdownNameText(Lockdown lockdown, TextView lockdownNameText) {
        String lockdownName = lockdown.getName();
        lockdownNameText.setText(lockdownName);
    }

    /*
     * setLockdownRepeatDaysText: retrieve the repeating days of the lockdown, apply a bold style to the
     * appropriate day of the week text, and make the appropriate indicators visible.
     */
    private void setLockdownRepeatDaysText(Lockdown lockdown, TextView lockdownRepeatSundayText, TextView lockdownRepeatSundayIndicator,
                                           TextView lockdownRepeatMondayText, TextView lockdownRepeatMondayIndicator,
                                           TextView lockdownRepeatTuesdayText, TextView lockdownRepeatTuesdayIndicator,
                                           TextView lockdownRepeatWednesdayText, TextView lockdownRepeatWednesdayIndicator,
                                           TextView lockdownRepeatThursdayText, TextView lockdownRepeatThursdayIndicator,
                                           TextView lockdownRepeatFridayText, TextView lockdownRepeatFridayIndicator,
                                           TextView lockdownRepeatSaturdayText, TextView lockdownRepeatSaturdayIndicator) {
        int[] repeatDays = lockdown.getRepeatDays();

        for (int repeatDay : repeatDays) {
            switch (repeatDay) {
                case Calendar.SUNDAY:
                    lockdownRepeatSundayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatSundayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.MONDAY:
                    lockdownRepeatMondayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatMondayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.TUESDAY:
                    lockdownRepeatTuesdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatTuesdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.WEDNESDAY:
                    lockdownRepeatWednesdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatWednesdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.THURSDAY:
                    lockdownRepeatThursdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatThursdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.FRIDAY:
                    lockdownRepeatFridayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatFridayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.SATURDAY:
                    lockdownRepeatSaturdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatSaturdayIndicator.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /*
     * initializeLockdownToggleSwitch: retrieve the lockdown's enabled/disabled state, and set the
     * starting position of the toggle switch accordingly.
     */
    private void initializeLockdownToggleSwitch(Lockdown lockdown, MaterialSwitch lockdownToggleSwitch) {
        boolean lockdownEnabled = lockdown.getEnabled();

        if (lockdownEnabled) {
            lockdownToggleSwitch.setChecked(true);
        } else {
            lockdownToggleSwitch.setChecked(false);
        }
    }
}
