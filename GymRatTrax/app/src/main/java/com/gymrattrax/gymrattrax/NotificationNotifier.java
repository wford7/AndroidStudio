package com.gymrattrax.gymrattrax;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Receive Alarm from <...> and create the notification itself.
 */
public class NotificationNotifier extends Service {
    private static final String TAG ="NotificationNotifier";

    private WorkoutItem workoutItem;
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotificationNotifier getService() {
            return NotificationNotifier.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.gymrattrax.gymrattrax.INTENT_NOTIFY";
    public static final String WORKOUT_ID = "com.gymrattrax.gymrattrax.WORKOUT_ID";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out NotificationAlarm intent then we want to show our notification

        long wid = intent.getIntExtra(WORKOUT_ID, -1);
        if (wid > 0) {
            DBHelper dbh = new DBHelper(getApplicationContext());
            workoutItem = dbh.getWorkoutById(wid);
            dbh.close();
        }
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        // This is the 'title' of the notification
        CharSequence title = "Time to workout";
        if (workoutItem != null)
            title = workoutItem.getName().toString();
        // This is the icon to use on the notification
        int icon = R.drawable.ic_launcher;
        // This is the scrolling text of the notification
        CharSequence text = "Let's go!";
        if (workoutItem != null) {
//            switch (workoutItem.getType()) {
//                case CARDIO:
//                    break;
//                case STRENGTH:
//                    break;
//            }
            double minutesDbl = workoutItem.getTimeScheduled();
            int secondsTotal = (int) (minutesDbl * 60);
            int seconds = secondsTotal % 60;
            int minutes = (secondsTotal - seconds) / 60;
            text = minutes + " minutes, " + seconds + " seconds";
        }
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, CalorieNegationActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, title, text, contentIntent);

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.vibrate = new long[]{0, 300, 0};
        notification.priority = Notification.PRIORITY_HIGH;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Send the notification to the system.
        Log.d(TAG, "Displaying notification for workout item (ID: " + workoutItem.getID() + ").");
        mNM.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        stopSelf();
    }
}