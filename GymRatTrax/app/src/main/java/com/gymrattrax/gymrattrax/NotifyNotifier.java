package com.gymrattrax.gymrattrax;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Receive Alarm and create the notification itself.
 */
public class NotifyNotifier extends Service {
    private static final String TAG ="NotifyNotifier";

    private WorkoutItem workoutItem;
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyNotifier getService() {
            return NotifyNotifier.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 7918;
    // Name of an intent extra we can use to identify if this service was started to create a notification

    @Override
    public void onCreate() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out NotifyAlarm intent then we want to show our notification

        long wid = intent.getIntExtra(NotifyReceiver.ID, -1);
        if (wid > 0) {
            DBHelper dbh = new DBHelper(getApplicationContext());
            workoutItem = dbh.getWorkoutById(wid);
            dbh.close();
        }
        if(intent.getBooleanExtra(NotifyReceiver.INTENT_NOTIFY, false))
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false);

        Intent intent = new Intent(this, DailyWorkoutActivity.class);
        if (workoutItem != null) {
            mBuilder.setContentTitle(workoutItem.getName().toString());

            switch (workoutItem.getType()) {
                case CARDIO:
                    mBuilder.setContentText(String.valueOf(((CardioWorkoutItem)workoutItem).
                            getDistance()) + " miles");
                    break;
                case STRENGTH:
                    mBuilder.setContentText(String.valueOf(((StrengthWorkoutItem)workoutItem).
                            getSetsScheduled()) + " sets of " +
                            String.valueOf(((StrengthWorkoutItem)workoutItem).getRepsScheduled()) +
                            " reps with " +
                            String.valueOf(((StrengthWorkoutItem)workoutItem).getWeightUsed()) +
                            " lb weights");
                    break;
                default:
                    mBuilder.setContentText(((int)(workoutItem.getTimeScheduled() * 60) -
                            ((int)(workoutItem.getTimeScheduled() * 60) % 60) / 60) + " minutes, " +
                            ((int)(workoutItem.getTimeScheduled() * 60) % 60) + " seconds");
                    break;
            }

            if (workoutItem.getNotificationTone() != null) {
                mBuilder.setSound(workoutItem.getNotificationTone());
            } else {
                //mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            }
            if (workoutItem.isNotificationVibrate()) {
                mBuilder.setVibrate(new long[]{0, 300, 0});
            } else {
                mBuilder.setVibrate(new long[]{0, 0, 0});
            }
            if (workoutItem.isNotificationOngoing()) {
                mBuilder.setOngoing(true);
                mBuilder.setAutoCancel(false);
            }
            intent = new Intent(this, DailyWorkoutActivity.class);
            //TODO: add workout ID to Bundle. for example...
            /**
             * Bundle b = new Bundle();
             * b.putInt("ID", ID);
             * intent.putExtras(b);
             * startActivity(intent);
             */


        } else {
            mBuilder.setContentTitle("Time to work out!");
            mBuilder.setContentText("Let's go!");
//            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mBuilder.setVibrate(new long[]{0, 300, 0});
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(contentIntent);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManager mNotificationManager =
                (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(TAG, "Displaying notification for workout item (ID: " + workoutItem.getID() + ").");
        mNotificationManager.notify(NOTIFICATION, mBuilder.build());

        // Stop the service when we are finished
        stopSelf();
    }
}