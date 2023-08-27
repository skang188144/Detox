package com.kangsk.detox.lockdownfragment.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class Lockdown implements Serializable {

    private String name;
    private long startTimeInMillis;
    private long endTimeInMillis;
    private int[] repeatDays;
    private ArrayList<String> blacklistedApps;
    private int pendingIntentRequestCode;

    public Lockdown(String name, long startTime, long endTime, int[] repeatDays, ArrayList<String> blacklistedApps, int pendingIntentRequestCode) {
        this.name = name;
        this.startTimeInMillis = startTime;
        this.endTimeInMillis = endTime;
        this.repeatDays = repeatDays;
        this.blacklistedApps = blacklistedApps;
        this.pendingIntentRequestCode = pendingIntentRequestCode;
    }

    public String getName() {
        return name;
    }

    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public long getEndTimeInMillis() {
        return endTimeInMillis;
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

    public void setStartTimeInMillis(long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
    }

    public void setEndTimeInMillis(long endTimeInMillis) {
        this.endTimeInMillis = endTimeInMillis;
    }

}
