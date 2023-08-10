package com.kangsk.detox;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

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
 * 1. Add comments everywhere that need comments
 */

public class UsageDataManager {
    /*
     * getDailyUsageTime: get the total device usage time for every app in the specified day
     */
    public float getDailyUsageTime(Date date, int convertToTimeUnit, Context context) {
        // instantiate a UsageStatsManager object from our Context object
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfDay(date);
        long endRange = getLastMilliOfDay(date);

        // instantiate a UsageEvents collection by querying our UsageStatsManager
        UsageEvents usageEventsCollection = usageStatsManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEvents = new HashMap<>();
        HashMap<String, AppUsageInfo> appsUsageInfo = new HashMap<>();

        // sort UsageEvents data into our new collections
        sortUsageEventsCollection(usageEventsCollection, usageEvents, appsUsageInfo, context);

        // calculate the total usage time
        long totalUsageTime = calculateDailyUsageData(usageEvents, appsUsageInfo, date);

        return roundToNearestTenth(convertMillisTo(totalUsageTime, convertToTimeUnit));
    }

    /*
     * getDailyUsageTime: get the total device usage time for every app in the specified hour
     */
    public float getHourlyUsageTime(Date date, int hour, int convertToTimeUnit, Context context) {
        // instantiate a UsageStatsManager object from our Context object
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfHour(date, hour);
        long endRange = getLastMilliOfHour(date, hour);

        // instantiate a UsageEvents collection by querying our UsageStatsManager
        UsageEvents usageEventsCollection = usageStatsManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEvents = new HashMap<>();
        HashMap<String, AppUsageInfo> appsUsageInfo = new HashMap<>();

        // sort UsageEvents data into our new collections
        sortUsageEventsCollection(usageEventsCollection, usageEvents, appsUsageInfo, context);

        // calculate the total usage time
        long totalUsageTime = calculateHourlyUsageData(usageEvents, appsUsageInfo, date, hour);

        return roundToNearestTenth(convertMillisTo(totalUsageTime, convertToTimeUnit));
    }

    /*
     * getHourlyUsageBarData: get the screen on time for the 24 hours in a specified date, and
     * encapsulate that data in a BarData object.
     */
    public BarData getHourlyUsageBarData(Date date, Context context) {
        // create an ArrayList of barEntries, and populate positions (hours) 0 - 23 with the screen-on time
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int hour = 0;

        for (int i = 0; i < 24; i++) {
            float screenOnTime = getHourlyUsageTime(date, i, Calendar.HOUR, context);
            BarEntry barEntry = new BarEntry((float) (i), screenOnTime);
            barEntries.add(i, barEntry);
        }

        // use barEntries to instantiate barDataSet, and use barDataSet to instantiate barData
        BarDataSet barDataSet = new BarDataSet(barEntries, null);

        // barDataSet configuration
        barDataSet.setDrawValues(false);
        barDataSet.setColor(context.getResources().getColor(R.color.dark_green, context.getTheme()));

        return new BarData(barDataSet);
    }

    /*
     * getDailyAppsUsageInfo:
     */
    public HashMap<String, AppUsageInfo> getDailyAppsUsageInfo(Date date, int convertToTimeUnit, Context context) {

        // instantiate a UsageStatsManager object from our Context object
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfDay(date);
        long endRange = getLastMilliOfDay(date);

        // instantiate a UsageEvents collection by querying our UsageStatsManager
        UsageEvents usageEventsCollection = usageStatsManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEvents = new HashMap<>();
        HashMap<String, AppUsageInfo> appsUsageInfo = new HashMap<>();

        // sort UsageEvents data into our new collections
        sortUsageEventsCollection(usageEventsCollection, usageEvents, appsUsageInfo, context);

        // calculate the total usage time
        calculateDailyUsageData(usageEvents, appsUsageInfo, date);

        return appsUsageInfo;
    }

    /*
     * sortUsageEventsCollection: sort a UsageEvents collection type into a pair of HashMaps,
     * one containing another list of UsageEvents.Event objects, and another containing AppUsageInfo
     * objects
     */
    private static void sortUsageEventsCollection(UsageEvents sourceCollection, HashMap<String, List<UsageEvents.Event>> usageEvents, HashMap<String, AppUsageInfo> appsUsageInfo, Context context) {
        UsageEvents.Event currentEvent;

        int currentEventType = 0;
        String currentEventPackage = null;

        while (sourceCollection.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            sourceCollection.getNextEvent(currentEvent);

            currentEventType = currentEvent.getEventType();
            currentEventPackage = currentEvent.getPackageName();

            // if this iteration's event is not ACTIVITY_RESUMED or ACTIVITY_PAUSED, skip to then next iteration.
            if (currentEventType != UsageEvents.Event.ACTIVITY_RESUMED
                    && currentEventType != UsageEvents.Event.ACTIVITY_PAUSED) {
                continue;
            }

            // if there isn't a Map.Entry<> with this app's name (key), then add a new Map.Entry<>
            if (appsUsageInfo.get(currentEventPackage) == null) {
                appsUsageInfo.put(currentEventPackage, new AppUsageInfo(currentEventPackage));
                appsUsageInfo.get(currentEventPackage).appName = getAppName(currentEventPackage, context);
                appsUsageInfo.get(currentEventPackage).appIcon = getAppIcon(currentEventPackage, context);

                usageEvents.put(currentEventPackage, new ArrayList<UsageEvents.Event>());
                usageEvents.get(currentEventPackage).add(currentEvent);

            // if there is a Map.Entry<> with this app's name (key), put the value in the existing Map.Entry<>
            } else {
                usageEvents.get(currentEventPackage).add(currentEvent);
            }
        }
    }

    /*
     * calculateDailyUsageData: calculate the total usage time in a day using the hashmaps with
     * the sorted events data
     */
    private long calculateDailyUsageData(HashMap<String, List<UsageEvents.Event>> usageEvents, HashMap<String, AppUsageInfo> appsUsageInfo, Date date) {
        List<UsageEvents.Event> appUsageEvents;
        int appUsageEventsSize;

        UsageEvents.Event currentEvent;
        int currentEventType;
        String currentEventPackage;

        long resumedEventTimeStamp;
        long pausedEventTimeStamp;
        long sessionUsageTime;

        long totalUsageTime = 0;

        for (Map.Entry<String, List<UsageEvents.Event>> entry : usageEvents.entrySet()) {
            appUsageEvents = entry.getValue();
            appUsageEventsSize = appUsageEvents.size();


            for (int i = 0; i < appUsageEventsSize; i++) {
                // check if the events list size is 0
                if (appUsageEventsSize == 0) {
                    continue;
                }

                currentEvent = appUsageEvents.get(i);
                currentEventType = currentEvent.getEventType();
                currentEventPackage = currentEvent.getPackageName();

                /*
                 * SPECIAL CASE:
                 * when the first event in the list is ACTIVITY_PAUSED, this means that the
                 * activity started before the time range, and extended into our time range.
                 */
                if (i == 0 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    resumedEventTimeStamp = getFirstMilliOfDay(date);
                    pausedEventTimeStamp = currentEvent.getTimeStamp();

                /*
                 * SPECIAL CASE:
                 * we ignore the last event if it's ACTIVITY_PAUSED, because this will already be
                 * calculated on the second to last event.
                 */
                } else if (i == appUsageEventsSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    continue;

                /*
                 * SPECIAL CASE:
                 * when the last event is ACTIVITY_RESUMED, this means that the activity is still
                 * ongoing. The pausedEventTimeStamp should be the current time now.
                 */
                } else if (i == appUsageEventsSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = date.getTime();

                /*
                 * REGULAR CASE:
                 * assume that the succeeding event is ACTIVITY_PAUSED. Get the difference
                 * between the current ACTIVITY_RESUMED event, and the next ACTIVITY_PAUSED event.
                 */
                } else if (currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = appUsageEvents.get(i + 1).getTimeStamp();

                /*
                 * DEFAULT CASE:
                 */
                } else {
                    resumedEventTimeStamp = 0;
                    pausedEventTimeStamp = 0;
                }

                //
                sessionUsageTime = pausedEventTimeStamp - resumedEventTimeStamp;
                totalUsageTime += sessionUsageTime;
                appsUsageInfo.get(currentEventPackage).appUsageTime += sessionUsageTime;
            }
        }

        // if the total usage time for this day is greater than 1 day, then round down
        if (convertMillisTo(totalUsageTime, Calendar.HOUR) > 24) {
            totalUsageTime = 86400000;
        }

        return totalUsageTime;
    }

    /*
     * calculateHourlyUsageData: calculate the total usage time in an hour using the hashmaps with
     * the sorted events data
     */
    private long calculateHourlyUsageData(HashMap<String, List<UsageEvents.Event>> usageEvents, HashMap<String, AppUsageInfo> appsUsageInfo, Date date, int hour) {
        List<UsageEvents.Event> appUsageEvents;
        int appUsageEventsSize;

        UsageEvents.Event currentEvent;
        int currentEventType;
        String currentEventPackage;

        long resumedEventTimeStamp;
        long pausedEventTimeStamp;
        long sessionUsageTime;

        long totalUsageTime = 0;

        for (Map.Entry<String, List<UsageEvents.Event>> entry : usageEvents.entrySet()) {
            appUsageEvents = entry.getValue();
            appUsageEventsSize = appUsageEvents.size();

            for (int i = 0; i < appUsageEventsSize; i++) {
                // check if the events list size is 0
                if (appUsageEventsSize == 0) {
                    continue;
                }

                currentEvent = appUsageEvents.get(i);
                currentEventType = currentEvent.getEventType();
                currentEventPackage = currentEvent.getPackageName();

                /*
                 * SPECIAL CASE:
                 * when the first event in the list is ACTIVITY_PAUSED, this means that the
                 * activity started before the time range, and extended into our time range.
                 */
                if (i == 0 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    resumedEventTimeStamp = getFirstMilliOfHour(date, hour);
                    pausedEventTimeStamp = currentEvent.getTimeStamp();

                /*
                 * SPECIAL CASE:
                 * we ignore the last event if it's ACTIVITY_PAUSED, because this will already be
                 * calculated on the second to last event.
                 */
                } else if (i == appUsageEventsSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    continue;

                /*
                 * SPECIAL CASE:
                 * when the last event is ACTIVITY_RESUMED, this means that the
                 * activity started in the time range, and extended outside of our time range.
                 */
                } else if (i == appUsageEventsSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = date.getTime();

                /*
                 * REGULAR CASE:
                 * assume that the succeeding event is ACTIVITY_PAUSED. Get the difference
                 * between the current ACTIVITY_RESUMED event, and the next ACTIVITY_PAUSED event.
                 */
                } else if (currentEventType == UsageEvents.Event.ACTIVITY_RESUMED){
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = appUsageEvents.get(i + 1).getTimeStamp();

                /*
                 * DEFAULT CASE:
                 */
                } else {
                    resumedEventTimeStamp = 0;
                    pausedEventTimeStamp = 0;
                }

                //
                sessionUsageTime = pausedEventTimeStamp - resumedEventTimeStamp;
                totalUsageTime += sessionUsageTime;
                appsUsageInfo.get(currentEventPackage).appUsageTime += sessionUsageTime;
            }
        }

        // if the total usage time for this hour is greater than 1 hour, then round down
        if (convertMillisTo(totalUsageTime, Calendar.MINUTE) > 60) {
            totalUsageTime = 3600000;
        }

        return totalUsageTime;
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
    private long getFirstMilliOfDay(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);

        calendar.set(Calendar.MILLISECONDS_IN_DAY, 0);
        return calendar.getTimeInMillis();
    }

    /*
     * getLastMilliOfToday: calculates the last millisecond of the specified date in Unix time
     */
    private long getLastMilliOfDay(Date date) {
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
     * roundToNearestTenth: round a float down to its nearest tenth
     */
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

    private static String getAppName(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.CERT_INPUT_RAW_X509);
            return packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception exception) {
            return null;
        }

    }

    private static Drawable getAppIcon(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        Drawable icon;

        try {
            icon = packageManager.getApplicationIcon(packageName);
            return icon;
        }
        catch (PackageManager.NameNotFoundException exception) {
            return null;
        }
    }
}
