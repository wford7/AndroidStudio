package com.gymrattrax.gymrattrax;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

@Deprecated
public class NotifyScheduler {
    private static final String TAG = "NotifyScheduler";

    // The hook into our service
    private NotifyService mNotifyService;
    // The context to start the service in
    private Context mContext;
    // A flag if we are connected to the service or not
    private boolean mIsBound;

    public NotifyScheduler(Context context) {
        mContext = context;
    }

    /**
     * Call this to connect your activity to your service.
     */
    public void doBindService() {
        // Establish a connection with our service
        mContext.bindService(new Intent(mContext, NotifyService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * When you attempt to connect to the service, this connection will be called with the result.
     * If we have successfully connected we instantiate our service object so that we can call methods on it.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with our service has been established,
            // giving us the service object we can use to interact with our service.
//            mNotifyService = ((NotifyService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mNotifyService = null;
        }
    };

    /**
     * Add workout to the database file and check for notifications.
     * @param workoutItem WorkoutItem object that contains a date that will control a notification.
     */
    public void setAlarmForNotification(WorkoutItem workoutItem){
        Log.d(TAG, "Setting workout event (ID: " + workoutItem.getID() + ") alarm.");
        mNotifyService = new NotifyService();
        mNotifyService.setAlarm(workoutItem);
    }

    /**
     * When you have finished with the service call this method to stop it
     * releasing your connection and resources
     */
    public void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}