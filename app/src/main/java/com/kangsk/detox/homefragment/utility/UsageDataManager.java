package com.kangsk.detox.homefragment.utility;

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
import com.kangsk.detox.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsageDataManager {

    private final Context mApplicationContext;

    public UsageDataManager(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    /*
     * getUsageTimeForDay: get the total device usage time for every app in the specified day
     */
    public long getUsageTimeForDay(Date date) {
        // instantiate a UsageStatManager object from our Context object
        UsageStatsManager usageStatManager = (UsageStatsManager) mApplicationContext.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfDay(date);
        long endRange = getLastMilliOfDay(date);

        // instantiate a UsageEvent collection by querying our UsageStatManager
        UsageEvents usageEventCollection = usageStatManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEventMap = new HashMap<>();
        HashMap<String, AppUsageData> appUsageDataMap = new HashMap<>();

        // sort UsageEvent data into our new collections
        sortUsageEventCollection(usageEventCollection, usageEventMap, appUsageDataMap);

        // calculate the total usage time
        long usageTimeForDay = calculateUsageTimeForDay(usageEventMap, appUsageDataMap, date);

        return usageTimeForDay;
    }

    /*
     * getUsageTimeForDay: get the total device usage time for every app in the specified hour
     */
    public long getUsageTimeForHour(Date date, int hour) {
        // instantiate a UsageStatManager object from our Context object
        UsageStatsManager usageStatManager = (UsageStatsManager) mApplicationContext.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfHour(date, hour);
        long endRange = getLastMilliOfHour(date, hour);

        // instantiate a UsageEvent collection by querying our UsageStatManager
        UsageEvents usageEventCollection = usageStatManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEventMap = new HashMap<>();
        HashMap<String, AppUsageData> appUsageDataMap = new HashMap<>();

        // sort UsageEvent data into our new collections
        sortUsageEventCollection(usageEventCollection, usageEventMap, appUsageDataMap);

        // calculate the total usage time
        long usageTimeForHour = calculateUsageTimeForHour(usageEventMap, appUsageDataMap, date, hour);

        return usageTimeForHour;
    }

    /*
     * getUsageTimePerApp:get a list of apps and their individual usage time in the specified day
     */
    public ArrayList<AppUsageData> getUsageTimePerApp(Date date) {
        // instantiate a UsageStatManager object from our Context object
        UsageStatsManager usageStatManager = (UsageStatsManager) mApplicationContext.getSystemService(Context.USAGE_STATS_SERVICE);

        long startRange = getFirstMilliOfDay(date);
        long endRange = getLastMilliOfDay(date);

        // instantiate a UsageEvent collection by querying our UsageStatManager
        UsageEvents usageEventCollection = usageStatManager.queryEvents(startRange, endRange);

        // grabbing our individual collections
        HashMap<String, List<UsageEvents.Event>> usageEventMap = new HashMap<>();
        HashMap<String, AppUsageData> appUsageDataMap = new HashMap<>();
        ArrayList<AppUsageData> appUsageDataList = new ArrayList<>();

        // sort UsageEvent data into our new collections
        sortUsageEventCollection(usageEventCollection, usageEventMap, appUsageDataMap);

        // calculate the total usage time
        calculateUsageTimeForDay(usageEventMap, appUsageDataMap, date);

        for (Map.Entry<String, AppUsageData> appUsageDataEntry : appUsageDataMap.entrySet()) {
            if (appUsageDataEntry.getValue().appUsageTime >= 60000) {
                appUsageDataList.add(appUsageDataEntry.getValue());
            }
        }

        appUsageDataList.sort(Comparator.comparing(AppUsageData::getAppUsageTime));
        Collections.reverse(appUsageDataList);

        return appUsageDataList;
    }

    /*
     * getUsageBarData: get the screen on time for the 24 hours in a specified date, and
     * encapsulate that data in a BarData object.
     */
    public BarData getUsageBarData(Date date) {
        // create an ArrayList of barEntries, and populate positions (hours) 0 - 23 with the screen-on time
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        // calculate usage time for each hour and encapsulate it in a BarEntry object
        for (int hour = 0; hour < 24; hour++) {
            float usageTimeForHour = getUsageTimeForHour(date, hour) / 60000.0f;
            BarEntry barEntry = new BarEntry((float) (hour), usageTimeForHour);
            barEntries.add(hour, barEntry);
        }

        // use barEntries to instantiate barDataSet, and use barDataSet to instantiate barData
        BarDataSet barDataSet = new BarDataSet(barEntries, null);

        // barDataSet configuration
        barDataSet.setDrawValues(false);
        barDataSet.setColor(mApplicationContext.getResources().getColor(R.color.dark_complimentary_color, mApplicationContext.getTheme()));
        barDataSet.setHighlightEnabled(false);

        return new BarData(barDataSet);
    }

    /*
     * sortUsageEventsCollection: sort a UsageEvents collection type into a pair of HashMaps,
     * one containing another list of UsageEvents.Event objects, and another containing AppUsageData
     * objects
     */
    private void sortUsageEventCollection(UsageEvents sourceCollection, HashMap<String, List<UsageEvents.Event>> usageEventsMap, HashMap<String, AppUsageData> appsUsageDataMap) {
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
            if (appsUsageDataMap.get(currentEventPackage) == null) {
                appsUsageDataMap.put(currentEventPackage, new AppUsageData(currentEventPackage));
                appsUsageDataMap.get(currentEventPackage).appName = getAppName(currentEventPackage);
                appsUsageDataMap.get(currentEventPackage).appIcon = getAppIcon(currentEventPackage);

                usageEventsMap.put(currentEventPackage, new ArrayList<UsageEvents.Event>());
                usageEventsMap.get(currentEventPackage).add(currentEvent);

            // if there is a Map.Entry<> with this app's name (key), put the value in the existing Map.Entry<>
            } else {
                usageEventsMap.get(currentEventPackage).add(currentEvent);
            }
        }
    }

    /*
     * calculateDailyUsageData: calculate the total usage time in a day using the hashmaps with
     * the sorted events data
     */
    private long calculateUsageTimeForDay(HashMap<String, List<UsageEvents.Event>> usageEvents, HashMap<String, AppUsageData> appsUsageInfo, Date date) {
        List<UsageEvents.Event> appUsageEventList;
        int appUsageEventListSize;

        UsageEvents.Event currentEvent;
        int currentEventType;
        String currentEventPackage;

        long resumedEventTimeStamp;
        long pausedEventTimeStamp;
        long sessionUsageTime;

        long totalUsageTime = 0;

        for (Map.Entry<String, List<UsageEvents.Event>> entry : usageEvents.entrySet()) {
            appUsageEventList = entry.getValue();
            appUsageEventListSize = appUsageEventList.size();


            for (int i = 0; i < appUsageEventListSize; i++) {
                // check if the events list size is 0
                if (appUsageEventListSize == 0) {
                    continue;
                }

                currentEvent = appUsageEventList.get(i);
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
                } else if (i == appUsageEventListSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    continue;

                /*
                 * SPECIAL CASE:
                 * when the last event is ACTIVITY_RESUMED, this means that the activity is still
                 * ongoing. The pausedEventTimeStamp should be the current time now.
                 */
                } else if (i == appUsageEventListSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = date.getTime();

                /*
                 * REGULAR CASE:
                 * assume that the succeeding event is ACTIVITY_PAUSED. Get the difference
                 * between the current ACTIVITY_RESUMED event, and the next ACTIVITY_PAUSED event.
                 */
                } else if (currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = appUsageEventList.get(i + 1).getTimeStamp();

                /*
                 * DEFAULT CASE:
                 */
                } else {
                    resumedEventTimeStamp = 0;
                    pausedEventTimeStamp = 0;
                }

                sessionUsageTime = pausedEventTimeStamp - resumedEventTimeStamp;
                totalUsageTime += sessionUsageTime;
                appsUsageInfo.get(currentEventPackage).appUsageTime += (sessionUsageTime);
            }
        }

        // if the total usage time for this day is greater than 1 day, then round down
        if (totalUsageTime > 86400000) {
            totalUsageTime = 86400000;
        }

        return totalUsageTime;
    }

    /*
     * calculateHourlyUsageData: calculate the total usage time in an hour using the hashmaps with
     * the sorted events data
     */
    private long calculateUsageTimeForHour(HashMap<String, List<UsageEvents.Event>> usageEvents, HashMap<String, AppUsageData> appsUsageInfo, Date date, int hour) {
        List<UsageEvents.Event> appUsageEventList;
        int appUsageEventListSize;

        UsageEvents.Event currentEvent;
        int currentEventType;
        String currentEventPackage;

        long resumedEventTimeStamp;
        long pausedEventTimeStamp;
        long sessionUsageTime;

        long totalUsageTime = 0;

        for (Map.Entry<String, List<UsageEvents.Event>> entry : usageEvents.entrySet()) {
            appUsageEventList = entry.getValue();
            appUsageEventListSize = appUsageEventList.size();

            for (int i = 0; i < appUsageEventListSize; i++) {
                // check if the events list size is 0
                if (appUsageEventListSize == 0) {
                    continue;
                }

                currentEvent = appUsageEventList.get(i);
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
                } else if (i == appUsageEventListSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    continue;

                /*
                 * SPECIAL CASE:
                 * when the last event is ACTIVITY_RESUMED, this means that the
                 * activity started in the time range, and extended outside of our time range.
                 */
                } else if (i == appUsageEventListSize - 1 && currentEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = date.getTime();

                /*
                 * REGULAR CASE:
                 * assume that the succeeding event is ACTIVITY_PAUSED. Get the difference
                 * between the current ACTIVITY_RESUMED event, and the next ACTIVITY_PAUSED event.
                 */
                } else if (currentEventType == UsageEvents.Event.ACTIVITY_RESUMED){
                    resumedEventTimeStamp = currentEvent.getTimeStamp();
                    pausedEventTimeStamp = appUsageEventList.get(i + 1).getTimeStamp();

                /*
                 * DEFAULT CASE:
                 */
                } else {
                    resumedEventTimeStamp = 0;
                    pausedEventTimeStamp = 0;
                }

                sessionUsageTime = pausedEventTimeStamp - resumedEventTimeStamp;
                totalUsageTime += sessionUsageTime;
                appsUsageInfo.get(currentEventPackage).appUsageTime += (sessionUsageTime);
            }
        }

        // if the total usage time for this hour is greater than 1 hour, then round down
        if (totalUsageTime > 3600000) {
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
    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar;
    }

    /*
     * convertMillisTo: takes milliseconds and converts it into a specific time unit
     */
    @Deprecated
    private float convertMillisTo(long millis, int timeUnit) {
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
                throw new RuntimeException("An exceptio time unit is invalid");
        }
    }

    /*
     * getAppName: accesses the device's package manager, looks for an app that matches the
     * specified package name, and returns the display name of the app.
     */
    private String getAppName(String packageName) {
        PackageManager packageManager = mApplicationContext.getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.CERT_INPUT_RAW_X509);
            return packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception exception) {
            return null;
        }

    }

    /*
     * getAppName: accesses the device's package manager, looks for an app that matches the
     * specified package name, and returns the icon of the app.
     */
    private Drawable getAppIcon(String packageName) {
        PackageManager packageManager = mApplicationContext.getPackageManager();
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
