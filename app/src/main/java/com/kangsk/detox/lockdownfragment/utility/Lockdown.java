package com.kangsk.detox.lockdownfragment.utility;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Lockdown implements Serializable {

    private String name;
    private boolean enabled;
    private LocalTime startTime;
    private LocalTime endTime;
    private int[] repeatDays;
    private ArrayList<String> blacklistedApps;
    private int pendingIntentRequestCode;

    public Lockdown(String name, boolean enabled, LocalTime startTime, LocalTime endTime, int[] repeatDays, ArrayList<String> blacklistedApps, int pendingIntentRequestCode) {
        this.name = name;
        this.enabled = enabled;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatDays = repeatDays;
        this.blacklistedApps = blacklistedApps;
        this.pendingIntentRequestCode = pendingIntentRequestCode;
    }

    public String getName() {
        return name;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int[] getRepeatDays() {
        return repeatDays;
    }

    public ArrayList<String> getBlacklistedApps() {
        return blacklistedApps;
    }

    public int getPendingIntentRequestCode() {
        return pendingIntentRequestCode;
    }

}
