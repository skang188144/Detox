package com.kangsk.detox;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * 0. Figure out if you need to specify "today", or maybe you can add capabilities to get a specific day?
 * 1. take out the getScreenOnTime into a separate method
 * 2. create two new methods for hour and for today
 * 3. implement two separate getBarData methods -- one for hour, one for today
 *
 * 4. Create a new method for getting a map of the app name, and the screen on time for that specific app. (for app list below the overview graph)
 *          (also consider making this method take the time range -- specific hour or specific day)
 *
 * 5. make comments for everything
 */


public class ScreenOnTimeManager {

    public float getScreenOnTimeOfToday(int convertToTimeUnit, Context context) {

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        /**
         * Maybe the reason why the this method is called multiple times is to refresh
         * the TextView. And when this method is called, we sort through each event and
         * somehow remove it from the heap.
         */
        UsageEvents totalListOfUsageEvents = mUsageStatsManager.queryEvents(getFirstMilliOfToday(), getLastMilliOfToday());
        HashMap<String, AppUsageInfo> appUsageInfoMap = new HashMap<>();
        HashMap<String, List<UsageEvents.Event>> appUsageEventMap = new HashMap<>();
        UsageEvents.Event currentUsageEvent;

        long finalScreenOnTime = 0L;
        long finalLaunchCount = 0L;


        while (totalListOfUsageEvents.hasNextEvent()) {
            currentUsageEvent = new UsageEvents.Event();
            totalListOfUsageEvents.getNextEvent(currentUsageEvent);

           if (currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                || currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
               String appName = currentUsageEvent.getPackageName();

               if (appUsageInfoMap.get(appName) == null) {
                   // create new entry for app not previously logged
                   appUsageInfoMap.put(appName, new AppUsageInfo(appName));
                   appUsageEventMap.put(appName, new ArrayList<UsageEvents.Event>());
                   // add app usageevent entry
                   appUsageEventMap.get(appName).add(currentUsageEvent);
               } else {
                   // add app usageevent entry
                   appUsageEventMap.get(appName).add(currentUsageEvent);
               }
           }
        }

        for (Map.Entry<String, List<UsageEvents.Event>> appUsageEventMapEntry : appUsageEventMap.entrySet()) {
            List<UsageEvents.Event> appUsageEventsList = appUsageEventMapEntry.getValue();
            int totalNumberOfEventsForApp = appUsageEventMapEntry.getValue().size();


            if (totalNumberOfEventsForApp <= 1) {
                continue;
            }

            for (int i = 0; i < totalNumberOfEventsForApp; i++) {

                if (appUsageEventsList.get(i).getEventType() != UsageEvents.Event.ACTIVITY_RESUMED) {
                    continue;
                }
                if (i == totalNumberOfEventsForApp - 1) {
                    continue;
                }
                UsageEvents.Event resumeEvent = appUsageEventsList.get(i);
                UsageEvents.Event pauseEvent = appUsageEventsList.get(i + 1);
                long eventResumedTimeStamp = resumeEvent.getTimeStamp();
                long eventPausedTimeStamp = pauseEvent.getTimeStamp();

                appUsageInfoMap.get(resumeEvent.getPackageName()).launchCount += 1;
                appUsageInfoMap.get(resumeEvent.getPackageName()).screenOnTime += convertMillisTo((eventPausedTimeStamp - eventResumedTimeStamp), Calendar.MINUTE);
            }
        }

        for (Map.Entry<String, AppUsageInfo> appUsageInfoEntry : appUsageInfoMap.entrySet()) {
            finalScreenOnTime += appUsageInfoEntry.getValue().screenOnTime;
            finalLaunchCount += appUsageInfoEntry.getValue().launchCount;
        }

        return roundToNearestTenth(finalScreenOnTime);
    }







    /*
     * getFirstMilliOfToday: calculates the first millisecond of today in Unix time
     */
    private long getFirstMilliOfToday() {
        // get calendar instance
        Calendar calendar = getCalendar();

        // 0 millisecond
        calendar.set(Calendar.MILLISECONDS_IN_DAY, 0);
        return calendar.getTimeInMillis();
    }

    /*
     * getLastMilliOfToday: calculates the last millisecond of today in Unix time
     */
    private long getLastMilliOfToday() {
        // get calendar instance
        Calendar calendar = getCalendar();

        // 86399999 millisecond (86400000 is exactly 24 hours)
        calendar.set(Calendar.MILLISECONDS_IN_DAY, 86399999);
        return calendar.getTimeInMillis();
    }

    /*
     * getFirstMilliOfHour: calculates the first millisecond of a specific hour
     * in Unix time
     */
    private long getFirstMilliOfHour(int hour) {
        // get calendar instance
        Calendar calendar = getCalendar();

        // hour argument exceeds 24 hours
        if (hour > 23) {
            throw new RuntimeException("ERROR: hour exceeds 0 - 23 range of a single day");
        }

        calendar.set(Calendar.MILLISECONDS_IN_DAY, (hour * 3600000));
        return calendar.getTimeInMillis();
    }

    /*
     * getLastMilliOfHour: calculates the last millisecond of a specific hour
     * in Unix time
     */
    private long getLastMilliOfHour(int hour) {
        // get calendar instance
        Calendar calendar = getCalendar();

        // hour argument exceeds 24 hours
        if (hour > 23) {
            throw new RuntimeException("ERROR: hour exceeds 0 - 23 range of a single day");
        }

        calendar.set(Calendar.MILLISECONDS_IN_DAY, ((hour + 1) * 3600000) - 1);
        return calendar.getTimeInMillis();
    }

    /*
     * getCalendar: returns a Calendar instance configured with the device's timezone
     */
    private static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar;
    }

    /*
     * sumScreenOnTime: takes a Map of strings representing an app's package name and
     * UsageStats objects, and calculates the sum of all the UsageStats' screen-on
     * times.
     */
    private static long sumScreenOnTime(Map<String, UsageStats> appUsageStats) {
        // iterate through the key-value pairs, and add up the total usage time
        long totalScreenOnMillis = 0;
        for (Map.Entry<String, UsageStats> entry : appUsageStats.entrySet()) {
            totalScreenOnMillis += entry.getValue().getTotalTimeVisible();
        }
        return totalScreenOnMillis;
    }

    private static float roundToNearestTenth(float rawNumber) {
        int scale = (int) Math.pow(10, 1);
        float roundedNumber = (float) Math.round(rawNumber * scale) / scale;
        return roundedNumber;
    }

    /*
     * convertMillisTo: takes milliseconds and converts it into a specific time unit
     */
    private static float convertMillisTo(long millis, int timeUnit) {
        switch (timeUnit) {
            case Calendar.MILLISECOND:
                return (float) millis;
            case Calendar.SECOND:
                return (millis/1000.0f);
            case Calendar.MINUTE:
                return (millis/60000.0f);
            case Calendar.HOUR:
                return (millis/3600000.0f);
            case Calendar.DATE:
                return (millis/86400000.0f);
            default:
                throw new RuntimeException("ERROR: time unit is invalid");
        }
    }

}
