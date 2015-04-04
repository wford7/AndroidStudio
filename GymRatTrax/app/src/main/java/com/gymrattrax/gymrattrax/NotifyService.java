package com.gymrattrax.gymrattrax;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

@Deprecated
public class NotifyService extends Service {
    private static final String TAG = "NotifyService";

    /**
     * Class for clients to access.
     */
//    public class ServiceBinder extends Binder {
//        NotifyService getService() {
//            return NotifyService.this;
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "Received start id " + startId + ": " + intent);
//
//        // We want this service to continue running until it is explicitly stopped, so return sticky.
//        return START_STICKY;
//        Intent alarmIntent = new Intent(getBaseContext(), A)
//
//        new NotifyAlarm(this, time, workoutItem).run();
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        return mBinder;
        return null;
    }

    // This is the object that receives interactions from clients. See
//    private final IBinder mBinder = new ServiceBinder();

    /**
     * Starts a new thread that will create an Alarm, which in turn will create a Notifier when the
     * time passes.
     * @param workoutItem The WorkoutItem Object that will control the timing and content of the
     *                    notification itself.
     */
    public void setAlarm(WorkoutItem workoutItem) {
        Calendar time = Calendar.getInstance();
        if (workoutItem.getNotificationMinutesInAdvance() >= 0)
            time.add(Calendar.MINUTE, -workoutItem.getNotificationMinutesInAdvance());
        else {
            DBHelper dbh = new DBHelper(this);
            int advanceMinutes;
            try {
                advanceMinutes = Integer.parseInt(
                        dbh.getProfileInfo(DBContract.ProfileTable.KEY_NOTIFY_ADVANCE));
            } catch (NumberFormatException e) {
                advanceMinutes = 0;
            }
            time.add(Calendar.MINUTE, -advanceMinutes);
        }
        time.setTime(workoutItem.getDateScheduled());
        Log.d(TAG, "Running alarm for workout item (ID: " + workoutItem.getID() + ").");
        new NotifyAlarm(this, time, workoutItem).run();
    }

}