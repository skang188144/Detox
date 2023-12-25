package com.kangsk.detox.lockdownfragment.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class Lockdown implements Serializable {

    private String name;
    private long startTime;
    private long endTime;
    private int[] repeatDays;
    private ArrayList<String> blacklistedApps;
    private int pendingIntentRequestCode;

    public Lockdown(String name, long startTime, long endTime, int[] repeatDays, ArrayList<String> blacklistedApps, int pendingIntentRequestCode) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatDays = repeatDays;
        this.blacklistedApps = blacklistedApps;
        this.pendingIntentRequestCode = pendingIntentRequestCode;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
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
