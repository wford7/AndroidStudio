package com.gymrattrax.gymrattrax;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Set an alarm using Android's AlarmManager for the date passed into the constructor. When the
 * alarm is raised it will start NotifyNotifier. This runs in its own thread.
 */
public class NotifyAlarm implements Runnable {
    private static final String TAG = "NotifyAlarm";

    private final Calendar dateForWhichAlarmIsRaised;
    // The android system alarm manager
    private final AlarmManager alarmManager;
    // Your context to retrieve the alarm manager from
    private final Context context;
    private final WorkoutItem workoutItem;

    public NotifyAlarm(Context context, Calendar dateForWhichAlarmIsRaised,
                       WorkoutItem workoutItem) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.dateForWhichAlarmIsRaised = dateForWhichAlarmIsRaised;
        this.workoutItem = workoutItem;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyNotifier.class);
        intent.putExtra(NotifyReceiver.INTENT_NOTIFY, true);
        intent.putExtra(NotifyReceiver.ID, workoutItem.getID());
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        Log.d(TAG, "Running AlarmManager task for workout item (ID: " + workoutItem.getID() + ").");
        alarmManager.set(
                AlarmManager.RTC, dateForWhichAlarmIsRaised.getTimeInMillis(), pendingIntent);
    }
}