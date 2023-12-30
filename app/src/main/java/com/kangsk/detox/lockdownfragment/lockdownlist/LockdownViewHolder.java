package com.kangsk.detox.lockdownfragment.lockdownlist;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class LockdownViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final LockdownManager mLockdownManager;

    private final TextView mLockdownStartTimeText;
    private final TextView mLockdownStartTimeAMPMText;
    private final TextView mLockdownEndTimeText;
    private final TextView mLockdownEndTimeAMPMText;

    private final TextView mLockdownNameText;

    private final TextView mLockdownRepeatDaySundayText;
    private final TextView mLockdownRepeatDayMondayText;
    private final TextView mLockdownRepeatDayTuesdayText;
    private final TextView mLockdownRepeatDayWednesdayText;
    private final TextView mLockdownRepeatDayThursdayText;
    private final TextView mLockdownRepeatDayFridayText;
    private final TextView mLockdownRepeatDaySaturdayText;

    private final TextView mLockdownRepeatDaySundayIndicator;
    private final TextView mLockdownRepeatDayMondayIndicator;
    private final TextView mLockdownRepeatDayTuesdayIndicator;
    private final TextView mLockdownRepeatDayWednesdayIndicator;
    private final TextView mLockdownRepeatDayThursdayIndicator;
    private final TextView mLockdownRepeatDayFridayIndicator;
    private final TextView mLockdownRepeatDaySaturdayIndicator;

    private final MaterialSwitch mLockdownEnabledSwitch;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownViewHolder(View itemView, LockdownManager lockdownManager) {
        super(itemView);

        mLockdownManager = lockdownManager;

        mLockdownStartTimeText = itemView.findViewById(R.id.text_lockdown_list_lockdown_start_time);
        mLockdownEndTimeText = itemView.findViewById(R.id.text_lockdown_list_lockdown_end_time);
        mLockdownStartTimeAMPMText = itemView.findViewById(R.id.text_lockdown_list_lockdown_start_time_ampm);
        mLockdownEndTimeAMPMText = itemView.findViewById(R.id.text_lockdown_list_lockdown_end_time_ampm);

        mLockdownNameText = itemView.findViewById(R.id.text_lockdown_list_lockdown_name);

        mLockdownRepeatDaySundayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_sunday);
        mLockdownRepeatDayMondayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_monday);
        mLockdownRepeatDayTuesdayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_tuesday);
        mLockdownRepeatDayWednesdayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_wednesday);
        mLockdownRepeatDayThursdayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_thursday);
        mLockdownRepeatDayFridayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_friday);
        mLockdownRepeatDaySaturdayText = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_saturday);

        mLockdownRepeatDaySundayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_sunday_dot);
        mLockdownRepeatDayMondayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_monday_dot);
        mLockdownRepeatDayTuesdayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_tuesday_dot);
        mLockdownRepeatDayWednesdayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_wednesday_dot);
        mLockdownRepeatDayThursdayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_thursday_dot);
        mLockdownRepeatDayFridayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_friday_dot);
        mLockdownRepeatDaySaturdayIndicator = itemView.findViewById(R.id.text_lockdown_list_lockdown_repeat_day_saturday_dot);

        mLockdownEnabledSwitch = itemView.findViewById(R.id.switch_lockdown_list_lockdown);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel(int position) {
        Lockdown currentLockdown = mLockdownManager.getLockdownList().get(position);

        setLockdownStartTimeText(mLockdownStartTimeText, mLockdownStartTimeAMPMText, currentLockdown);
        setLockdownEndTimeText(mLockdownEndTimeText, mLockdownEndTimeAMPMText, currentLockdown);
        setLockdownNameText(mLockdownNameText, currentLockdown);
        setRepeatDays(mLockdownRepeatDaySundayText, mLockdownRepeatDaySundayIndicator,
                mLockdownRepeatDayMondayText, mLockdownRepeatDayMondayIndicator,
                mLockdownRepeatDayTuesdayText, mLockdownRepeatDayTuesdayIndicator,
                mLockdownRepeatDayWednesdayText, mLockdownRepeatDayWednesdayIndicator,
                mLockdownRepeatDayThursdayText, mLockdownRepeatDayThursdayIndicator,
                mLockdownRepeatDayFridayText, mLockdownRepeatDayFridayIndicator,
                mLockdownRepeatDaySaturdayText, mLockdownRepeatDaySaturdayIndicator,
                currentLockdown);
        setSwitchEnabled(mLockdownEnabledSwitch, currentLockdown);
    }

    private void setLockdownStartTimeText(TextView lockdownStartTimeText, TextView lockdownStartTimeAMPMText, Lockdown lockdown) {
        LocalTime startTime = lockdown.getStartTime();

        int startHour = startTime.get(ChronoField.CLOCK_HOUR_OF_AMPM);
        int startMinute = startTime.get(ChronoField.MINUTE_OF_HOUR);
        String startAMPM;

        if (startTime.get(ChronoField.AMPM_OF_DAY) == 0) {
            startAMPM = "AM";
        } else {
            startAMPM = "PM";
        }

        lockdownStartTimeText.setText(String.format("%2d", startHour) + ":" + String.format("%02d", startMinute));
        lockdownStartTimeAMPMText.setText(startAMPM);
    }

    private void setLockdownEndTimeText(TextView lockdownEndTimeText, TextView lockdownEndTimeAMPMText, Lockdown lockdown) {
        LocalTime endTime = lockdown.getEndTime();

        int endHour = endTime.get(ChronoField.CLOCK_HOUR_OF_AMPM);
        int endMinute = endTime.get(ChronoField.MINUTE_OF_HOUR);
        String endAMPM;

        if (endTime.get(ChronoField.AMPM_OF_DAY) == 0) {
            endAMPM = "AM";
        } else {
            endAMPM = "PM";
        }

        lockdownEndTimeText.setText(String.format("%2d", endHour) + ":" + String.format("%02d", endMinute));
        lockdownEndTimeAMPMText.setText(endAMPM);
    }

    private void setLockdownNameText(TextView lockdownNameText, Lockdown lockdown) {
        String name = lockdown.getName();
        lockdownNameText.setText(name);
    }

    private void setRepeatDays(TextView lockdownRepeatDaySundayText, TextView lockdownRepeatDaySundayIndicator,
                               TextView lockdownRepeatDayMondayText, TextView lockdownRepeatDayMondayIndicator,
                               TextView lockdownRepeatDayTuesdayText, TextView lockdownRepeatDayTuesdayIndicator,
                               TextView lockdownRepeatDayWednesdayText, TextView lockdownRepeatDayWednesdayIndicator,
                               TextView lockdownRepeatDayThursdayText, TextView lockdownRepeatDayThursdayIndicator,
                               TextView lockdownRepeatDayFridayText, TextView lockdownRepeatDayFridayIndicator,
                               TextView lockdownRepeatDaySaturdayText, TextView lockdownRepeatDaySaturdayIndicator,
                               Lockdown lockdown) {
        int[] repeatDays = lockdown.getRepeatDays();

        for (int repeatDay : repeatDays) {
            switch (repeatDay) {
                case Calendar.SUNDAY:
                    lockdownRepeatDaySundayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDaySundayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.MONDAY:
                    lockdownRepeatDayMondayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDayMondayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.TUESDAY:
                    lockdownRepeatDayTuesdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDayTuesdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.WEDNESDAY:
                    lockdownRepeatDayWednesdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDayWednesdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.THURSDAY:
                    lockdownRepeatDayThursdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDayThursdayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.FRIDAY:
                    lockdownRepeatDayFridayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDayFridayIndicator.setVisibility(View.VISIBLE);
                    break;
                case Calendar.SATURDAY:
                    lockdownRepeatDaySaturdayText.setTypeface(null, Typeface.BOLD);
                    lockdownRepeatDaySaturdayIndicator.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void setSwitchEnabled(MaterialSwitch lockdownSwitch, Lockdown lockdown) {
        boolean enabled = lockdown.getEnabled();

        if (enabled) {
            lockdownSwitch.setChecked(true);
        } else {
            lockdownSwitch.setChecked(false);
        }
    }
}
