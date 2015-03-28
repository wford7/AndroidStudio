package com.gymrattrax.gymrattrax;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Set an alarm using Android's AlarmManager for the date passed into the constructor. When the
 * alarm is raised it will start NotificationNotifier. This runs in its own thread.
 */
public class NotificationAlarm implements Runnable {
    private static final String TAG = "NotificationAlarm";
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    private final WorkoutItem w;

    public NotificationAlarm(Context context, Calendar date, WorkoutItem w) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.w = w;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotificationNotifier.class);
        intent.putExtra(NotificationNotifier.INTENT_NOTIFY, true);
        intent.putExtra(NotificationNotifier.WORKOUT_ID, w.getID());
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        Log.d(TAG, "Running AlarmManager task for workout item (ID: " + w.getID() + ").");
        am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
    }
}