package com.gymrattrax.gymrattrax;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);

        // We want this service to continue running until it is explicitly stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients. See
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Starts a new thread that will create an Alarm, which in turn will create a Notifier when the
     * time passes.
     */
    public void setAlarm(Calendar c, WorkoutItem w) {
        // This starts a new thread to set the alarm
        // You want to push off your tasks onto a new thread to free up the UI to carry on responding
        Log.d(TAG, "Running alarm for workout item (ID: " + w.getID() + ").");
        new NotificationAlarm(this, c, w).run();
    }
}