package com.kangsk.detox;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:

 * 1. implement two separate getBarData methods -- one for hour, one for today
 *
 * 2. create a new method for retrieving a list of AppUsageInfo objects, with the package
 *    name, app name, launch count, and screen on time fields populated.
 */

public class UsageDataManager {

    /*
     * get the total screen-on time for every app in the specified hour
     */
    public float getScreenOnTime(Date date, int hour, int convertToTimeUnit, Context context) {
        long totalScreenOnTime = 0L;

        // instantiate a UsageStatsManager object
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        // query UsageStatsManager to get a UsageEvents object, which holds all usage data in the specified range
        UsageEvents usageEventsCollection = mUsageStatsManager.queryEvents(getFirstMilliOfHour(date, hour), getLastMilliOfHour(date, hour));

        // custom data type, used to classify total screen on time for a single app
        HashMap<String, AppUsageInfo> appUsageInfoMap = new HashMap<>();

        /*
         * this HashMap will have a string key containing a single app's package name.
         * The values will be a list of all UsageEvent.Event objects for that single app.
         * Note that UsageEvents in Android works in a way where a single event can happen
         * multiple times, so you need a List<> to hold all the events triggered by a single app.
         * We will pull the data from our UsageEvents object, and make it easier to parse through
         * by putting the data in a HashMap, instead of the custom UsageEvents object.
         */
        HashMap<String, List<UsageEvents.Event>> usageEventsMap = new HashMap<>();

        /*
         * current UsageEvents.Event that is being sorted from the collection UsageEvents,
         * to the HashMap we will use for processing the actual event data.
         */
        UsageEvents.Event currentUsageEvent;

        /*
         * iterate through the custom collection UsageEvents and re-encapsulate them in
         * the Map containing UsageEvents.Event's, as well a HashMap of our custom
         * AppUsageInfo data type.
         */
        while (usageEventsCollection.hasNextEvent()) {
            // get a reference of the current iteration of UsageEvents.Event
            currentUsageEvent = new UsageEvents.Event();
            usageEventsCollection.getNextEvent(currentUsageEvent);

            // only put the data in the HashMap if it is a ACTIVITY_RESUMED or ACTIVITY_PAUSED event
            if (currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                    || currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                String appPackageName = currentUsageEvent.getPackageName();

                if (appUsageInfoMap.get(appPackageName) == null) {
                    // create new entries for app not previously logged
                    appUsageInfoMap.put(appPackageName, new AppUsageInfo(appPackageName));
                    usageEventsMap.put(appPackageName, new ArrayList<UsageEvents.Event>());

                    // add the UsageEvents.Event entry
                    usageEventsMap.get(appPackageName).add(currentUsageEvent);
                } else {
                    // add the UsageEvents.Event entry
                    usageEventsMap.get(appPackageName).add(currentUsageEvent);
                }
            }
        }

        /*
         * iterate through each app's events list, and add up the total screen-on time
         */
        for (Map.Entry<String, List<UsageEvents.Event>> usageEventMapEntry : usageEventsMap.entrySet()) {
            List<UsageEvents.Event> appUsageEventsList = usageEventMapEntry.getValue();
            // number of Events that a Map entry (app) has
            int totalEvents = usageEventMapEntry.getValue().size();

            /*
             * in order to calculate the total screen time of an app, we need to have
             * a ACTIVITY_RESUMED event first, and then a ACTIVITY_PAUSED event next.
             * With those two events in that order, we can grab the timestamp of each
             * event and calculate the difference to get the screen time. if there are
             * 1 or less events, then we can't calculate screen time.
             * (note that the appUsageEventsList is already sorted chronologically.)
             */
            if (totalEvents <= 1) {
                continue;
            }

            for (int i = 0; i < totalEvents; i++) {
                // if the first event is not ACTIVITY_RESUMED, skip to the next iteration
                if (appUsageEventsList.get(i).getEventType() != UsageEvents.Event.ACTIVITY_RESUMED) {
                    continue;
                }

                /*
                 * check to see if our ACTIVITY_RESUMED event is our last event in the list. If so,
                 * skip this iteration and end this loop because we do not have a ACTIVITY_PAUSED
                 * event following if this is the last item in the list.
                 */
                if (i == totalEvents - 1) {
                    continue;
                }

                UsageEvents.Event resumeEvent = appUsageEventsList.get(i);
                UsageEvents.Event pauseEvent = appUsageEventsList.get(i + 1);
                long eventResumedTimeStamp = resumeEvent.getTimeStamp();
                long eventPausedTimeStamp = pauseEvent.getTimeStamp();

                appUsageInfoMap.get(resumeEvent.getPackageName()).screenOnTime += (eventPausedTimeStamp - eventResumedTimeStamp);
            }
        }

        for (Map.Entry<String, AppUsageInfo> appUsageInfoEntry : appUsageInfoMap.entrySet()) {
            totalScreenOnTime += appUsageInfoEntry.getValue().screenOnTime;
        }

        return roundToNearestTenth(convertMillisTo(totalScreenOnTime, convertToTimeUnit));
    }

    /*
     * get the total screen-on time for every app in the specified day
     */
    public float getScreenOnTime(Date date, int convertToTimeUnit, Context context) {
        long totalScreenOnTime = 0L;

        // instantiate a UsageStatsManager object
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        // query UsageStatsManager to get a UsageEvents object, which holds all usage data in the specified range
        UsageEvents usageEventsCollection = mUsageStatsManager.queryEvents(getFirstMilliOfDay(date), getLastMilliOfDay(date));

        // custom data type, used to classify total screen on time for a single app
        HashMap<String, AppUsageInfo> appUsageInfoMap = new HashMap<>();

        /*
         * this HashMap will have a string key containing a single app's package name.
         * The values will be a list of all UsageEvent.Event objects for that single app.
         * Note that UsageEvents in Android works in a way where a single event can happen
         * multiple times, so you need a List<> to hold all the events triggered by a single app.
         * We will pull the data from our UsageEvents object, and make it easier to parse through
         * by putting the data in a HashMap, instead of the custom UsageEvents object.
         */
        HashMap<String, List<UsageEvents.Event>> usageEventsMap = new HashMap<>();

        /*
         * current UsageEvents.Event that is being sorted from the collection UsageEvents,
         * to the HashMap we will use for processing the actual event data.
         */
        UsageEvents.Event currentUsageEvent;

        /*
         * iterate through the custom collection UsageEvents and re-encapsulate them in
         * the Map containing UsageEvents.Event's, as well a HashMap of our custom
         * AppUsageInfo data type.
         */
        while (usageEventsCollection.hasNextEvent()) {
            // get a reference of the current iteration of UsageEvents.Event
            currentUsageEvent = new UsageEvents.Event();
            usageEventsCollection.getNextEvent(currentUsageEvent);

            // only put the data in the HashMap if it is a ACTIVITY_RESUMED or ACTIVITY_PAUSED event
            if (currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                    || currentUsageEvent.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                String appPackageName = currentUsageEvent.getPackageName();

                if (appUsageInfoMap.get(appPackageName) == null) {
                    // create new entries for app not previously logged
                    appUsageInfoMap.put(appPackageName, new AppUsageInfo(appPackageName));
                    usageEventsMap.put(appPackageName, new ArrayList<UsageEvents.Event>());

                    // add the UsageEvents.Event entry
                    usageEventsMap.get(appPackageName).add(currentUsageEvent);
                } else {
                    // add the UsageEvents.Event entry
                    usageEventsMap.get(appPackageName).add(currentUsageEvent);
                }
            }
        }

        /*
         * iterate through each app's events list, and add up the total screen-on time
         */
        for (Map.Entry<String, List<UsageEvents.Event>> usageEventMapEntry : usageEventsMap.entrySet()) {
            List<UsageEvents.Event> appUsageEventsList = usageEventMapEntry.getValue();
            // number of Events that a Map entry (app) has
            int totalEvents = usageEventMapEntry.getValue().size();

            /*
             * in order to calculate the total screen time of an app, we need to have
             * a ACTIVITY_RESUMED event first, and then a ACTIVITY_PAUSED event next.
             * With those two events in that order, we can grab the timestamp of each
             * event and calculate the difference to get the screen time. if there are
             * 1 or less events, then we can't calculate screen time.
             * (note that the appUsageEventsList is already sorted chronologically.)
             */
            if (totalEvents <= 1) {
                continue;
            }

            for (int i = 0; i < totalEvents; i++) {
                // if the first event is not ACTIVITY_RESUMED, skip to the next iteration
                if (appUsageEventsList.get(i).getEventType() != UsageEvents.Event.ACTIVITY_RESUMED) {
                    continue;
                }

                /*
                 * check to see if our ACTIVITY_RESUMED event is our last event in the list. If so,
                 * skip this iteration and end this loop because we do not have a ACTIVITY_PAUSED
                 * event following if this is the last item in the list.
                 */
                if (i == totalEvents - 1) {
                    continue;
                }

                UsageEvents.Event resumeEvent = appUsageEventsList.get(i);
                UsageEvents.Event pauseEvent = appUsageEventsList.get(i + 1);
                long eventResumedTimeStamp = resumeEvent.getTimeStamp();
                long eventPausedTimeStamp = pauseEvent.getTimeStamp();

                appUsageInfoMap.get(resumeEvent.getPackageName()).screenOnTime += (eventPausedTimeStamp - eventResumedTimeStamp);
            }
        }

        for (Map.Entry<String, AppUsageInfo> appUsageInfoEntry : appUsageInfoMap.entrySet()) {
            totalScreenOnTime += appUsageInfoEntry.getValue().screenOnTime;
        }

        return roundToNearestTenth(convertMillisTo(totalScreenOnTime, convertToTimeUnit));
    }

    /*
     * get the screen on time for the 24 hours in a specified date, and encapsulate
     * that data in a BarData object.
     */
    public BarData getBarDataForDay(Date date, Context context) {
        // create an ArrayList of barEntries, and populate positions (hours) 0 - 23 with the screen-on time
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int hour = 0;

        for (int i = 0; i < 24; i++) {
            BarEntry barEntry;
            float screenOnTime = getScreenOnTime(date, i, Calendar.HOUR, context);

            // if the screen on time is imprecise and greater than an hour, round down to 1.0 hours of usage
            if (screenOnTime > 1.0f) {
                screenOnTime = 1.0f;
            }

            barEntry = new BarEntry((float) (i), screenOnTime);
            barEntries.add(i, barEntry);
        }

        // use barEntries to instantiate barDataSet, and use barDataSet to instantiate barData
        BarDataSet barDataSet = new BarDataSet(barEntries, null);
        barDataSet.setDrawValues(false);
        barDataSet.setColor(context.getResources().getColor(R.color.green, context.getTheme()));
        return new BarData(barDataSet);
    }

    /*
     * getFirstMilliOfHour: calculates the first millisecond of a specific hour
     * in Unix time
     */
    private long getFirstMilliOfHour(Date date, int hour) {
        // get calendar instance
        Calendar calendar = getCalendar();
        calendar.setTime(date);

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
    private long getLastMilliOfHour(Date date, int hour) {
        // get calendar instance
        Calendar calendar = getCalendar();
        calendar.setTime(date);

        // hour argument exceeds 24 hours
        if (hour > 23) {
            throw new RuntimeException("ERROR: hour exceeds 0 - 23 range of a single day");
        }

        calendar.set(Calendar.MILLISECONDS_IN_DAY, ((hour + 1) * 3600000) - 1);
        return calendar.getTimeInMillis();
    }

    /*
     * getFirstMilliOfDay: calculates the first millisecond of the specified date in Unix time
     */
    public long getFirstMilliOfDay(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);

        calendar.set(Calendar.MILLISECONDS_IN_DAY, 0);
        return calendar.getTimeInMillis();
    }

    /*
     * getLastMilliOfToday: calculates the last millisecond of the specified date in Unix time
     */
    public long getLastMilliOfDay(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);

        calendar.set(Calendar.MILLISECONDS_IN_DAY, 86399999);
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
