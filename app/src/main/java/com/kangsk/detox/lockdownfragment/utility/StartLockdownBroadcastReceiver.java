package com.kangsk.detox.lockdownfragment.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class StartLockdownBroadcastReceiver extends BroadcastReceiver {

    /*
     * PRIVATE FIELDS
     */
    private Lockdown mLockdown;
    private AlarmManager mAlarmManager;

    /*
     * onReceive: called when the Intent pointing to this BroadcastReceiver is sent.
     * Initializes the appropriate fields, and schedules the EndLockdownBroadcastReceiver
     * as well as the next lockdown occurrence's StartLockdownBroadcastReceiver.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LockdownManager", "Lockdown Received");
        Bundle bundle = intent.getBundleExtra("LockdownBundle");
        mLockdown = (Lockdown) bundle.get("Lockdown");
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Intent monitorServiceIntent = new Intent(context.getApplicationContext(), MonitorService.class);
        context.getApplicationContext().startService(monitorServiceIntent);
//        MonitorService.startLockdown();

        scheduleCurrentLockdownEnd(mLockdown, mAlarmManager, context);
        scheduleNextLockdownStart(mLockdown, mAlarmManager, context);
    }

    private void scheduleCurrentLockdownEnd(Lockdown lockdown, AlarmManager alarmManager, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Lockdown", lockdown);

        Intent intent = new Intent(context, EndLockdownBroadcastReceiver.class);
        intent.putExtra("LockdownBundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, lockdown.getPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, lockdown.getEndTime(), pendingIntent);
    }

    private void scheduleNextLockdownStart(Lockdown lockdown, AlarmManager alarmManager, Context context) {
        if (lockdown == null || lockdown.getRepeatDays() == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("Lockdown", lockdown);

        Intent intent = new Intent(context, StartLockdownBroadcastReceiver.class);
        intent.putExtra("LockdownBundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, lockdown.getPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, updateLockdownStartEndTime(lockdown).getStartTime(), pendingIntent);
    }

    private Lockdown updateLockdownStartEndTime(Lockdown lockdown) {
        // grab a Calendar instance and set its time to the original start time
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(lockdown.getStartTime());

        // grab a Calendar instance and set its time to the first repeatDay time
        Calendar newStartTime = Calendar.getInstance();
        newStartTime.setTimeInMillis(startTime.getTimeInMillis());
        newStartTime.set(Calendar.DAY_OF_WEEK, lockdown.getRepeatDays()[0]);

        /*
         * traverse through each repeatDay and find the closest to the original start time.
         * Once the closest repeatDay is found, update newStartTime accordingly.
         */
        for (int repeatDayOfWeek : lockdown.getRepeatDays()) {
            Calendar repeatTime = Calendar.getInstance();
            repeatTime.setTimeInMillis(startTime.getTimeInMillis());
            repeatTime.set(Calendar.DAY_OF_WEEK, repeatDayOfWeek);

            if (repeatTime.before(startTime)) {
                repeatTime.add(Calendar.WEEK_OF_YEAR, 1);
            }

            if (repeatTime.before(newStartTime)) {
                newStartTime = repeatTime;
            }
        }

        /*
         * calculate the difference between startTime and newStartTime, and add that
         * difference to the original end time to find the newEndTime.
         */
        long difference = newStartTime.getTimeInMillis() - startTime.getTimeInMillis();
        long newEndTimeInMillis = lockdown.getEndTime() + difference;

//        lockdown.setStartTime(newStartTime.getTimeInMillis());
//        lockdown.setEndTime(newEndTimeInMillis);

        return lockdown;
    }
}
