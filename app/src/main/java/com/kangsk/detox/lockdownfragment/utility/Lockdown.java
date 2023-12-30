package com.kangsk.detox.lockdownfragment.utility;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Lockdown implements Serializable {

    private String name;
    private boolean enabled;
    private LocalTime startTime;
    private LocalTime endTime;
    private int[] repeatDays;
    private ArrayList<String> blacklistedApps;

    public Lockdown(String name, boolean enabled, LocalTime startTime, LocalTime endTime, int[] repeatDays, ArrayList<String> blacklistedApps) {
        this.name = name;
        this.enabled = enabled;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatDays = repeatDays;
        this.blacklistedApps = blacklistedApps;
    }

    public String getName() {
        return name;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getTimeRemainingString() {
        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = getEndTime();

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
            timeRemainingString += (remainingHours + " " + hourLabelString + " ");
        }
        timeRemainingString += (remainingMinutes + " min.");

        return timeRemainingString;
    }

    public int[] getRepeatDays() {
        return repeatDays;
    }

    public ArrayList<String> getBlacklistedApps() {
        return blacklistedApps;
    }
}
