package com.kangsk.detox.lockdownfragment.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LockdownManager {
    /*
     * PRIVATE FIELDS
     */
    private static Context mApplicationContext;
    private static AlarmManager mAlarmManager;
    private static ArrayList<Lockdown> mLockdownList;
    private static File mLockdownListFile;
    private static LockdownManager mInstance = new LockdownManager();
    private static boolean isInitialized = false;

    private LockdownManager() {
    }

    public static LockdownManager getInstance(Context applicationContext) {
        if (isInitialized) {
            return mInstance;
        }

        mApplicationContext = applicationContext;
        mAlarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        mLockdownListFile = new File(applicationContext.getFilesDir(), "lockdown_list.detox");

        // if the lockdown list file does not exist in the device's file system, create a new lockdown list
        if (readLockdownListFromStorage(mLockdownListFile, mApplicationContext) == null) {
            mLockdownList = new ArrayList<>();
            // if the lockdown list file exists, then read the lockdown ArrayList saved on the file
        } else {
            mLockdownList = readLockdownListFromStorage(mLockdownListFile, mApplicationContext);
        }

        isInitialized = true;

        return mInstance;
    }

    public static void endInstance() {
        writeLockdownListToStorage(mLockdownList, mApplicationContext);
    }

    public ArrayList<Lockdown> getLockdownList() {
        return mLockdownList;
    }

    public Lockdown getCurrentLockdown() {
        // traverse through mLockdownList and find a lockdown with a time parameter that encompasses the current system time
        for (Lockdown lockdown : mLockdownList) {
            if (System.currentTimeMillis() >= lockdown.getStartTimeInMillis() && System.currentTimeMillis() <= lockdown.getEndTimeInMillis()) {
                return lockdown;
            }
        }
        // if no lockdown is ongoing, return null
        return null;
    }

    public void addLockdown(Lockdown lockdown) {
        mLockdownList.add(lockdown);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Lockdown", lockdown);

        Intent intent = new Intent(mApplicationContext, StartLockdownBroadcastReceiver.class);
        intent.putExtra("LockdownBundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mApplicationContext, lockdown.getPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, lockdown.getStartTimeInMillis(), pendingIntent);
    }

    public void removeLockdown(Lockdown lockdown) {
        mLockdownList.remove(lockdown);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Lockdown", lockdown);

        Intent intent = new Intent(mApplicationContext, StartLockdownBroadcastReceiver.class);
        intent.putExtra("LockdownBundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mApplicationContext, lockdown.getPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.cancel(pendingIntent);
    }

    /*
     * readLockdownListFromStorage: method responsible for reading the saved lockdownList file from
     * the device's file system. If the lockdownList file does not exist, this method will return null.
     */
    private static ArrayList<Lockdown> readLockdownListFromStorage(File lockdownListFile, Context applicationContext) {
        ArrayList<Lockdown> lockdownList = null;

        if (lockdownListFile.exists()) {
            try {
                FileInputStream fileInputStream  = applicationContext.openFileInput("lockdown_list.detox");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                lockdownList = (ArrayList<Lockdown>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        return lockdownList;
    }

    /*
     * writeLockdownListToStorage: method responsible for writing the current lockdownList to the lockdownList
     * file in the device's file system.
     */
    private static void writeLockdownListToStorage(ArrayList<Lockdown> lockdownList, Context applicationContext) {
        try {
            FileOutputStream fileOutputStream = applicationContext.openFileOutput("lockdown_list.detox", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(lockdownList);
            objectOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}