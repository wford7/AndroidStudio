package com.gymrattrax.gymrattrax;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;



public class ScheduleActivity extends ActionBarActivity {
    private Button backScheduleButton;
    private Button currentScheduleButton;
    private Button editScheduleButton;
    private Button viewScheduleButton;
//    private Button removeWorkoutButton;
    private EditText nameEditText;

    public String accountName = Account.class.getName();
    public Context ctx = new Context() {
        @Override
        public AssetManager getAssets() {
            return null;
        }

        @Override
        public Resources getResources() {
            return null;
        }

        @Override
        public PackageManager getPackageManager() {
            return null;
        }

        @Override
        public ContentResolver getContentResolver() {
            return null;
        }

        @Override
        public Looper getMainLooper() {
            return null;
        }

        @Override
        public Context getApplicationContext() {
            return null;
        }

        @Override
        public void setTheme(int resid) {

        }

        @Override
        public Resources.Theme getTheme() {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }

        @Override
        public String getPackageName() {
            return null;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return null;
        }

        @Override
        public String getPackageResourcePath() {
            return null;
        }

        @Override
        public String getPackageCodePath() {
            return null;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return null;
        }

        @Override
        public FileInputStream openFileInput(String name) throws FileNotFoundException {
            return null;
        }

        @Override
        public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
            return null;
        }

        @Override
        public boolean deleteFile(String name) {
            return false;
        }

        @Override
        public File getFileStreamPath(String name) {
            return null;
        }

        @Override
        public File getFilesDir() {
            return null;
        }

        @Override
        public File getNoBackupFilesDir() {
            return null;
        }

        @Nullable
        @Override
        public File getExternalFilesDir(String type) {
            return null;
        }

        @Override
        public File[] getExternalFilesDirs(String type) {
            return new File[0];
        }

        @Override
        public File getObbDir() {
            return null;
        }

        @Override
        public File[] getObbDirs() {
            return new File[0];
        }

        @Override
        public File getCacheDir() {
            return null;
        }

        @Override
        public File getCodeCacheDir() {
            return null;
        }

        @Nullable
        @Override
        public File getExternalCacheDir() {
            return null;
        }

        @Override
        public File[] getExternalCacheDirs() {
            return new File[0];
        }

        @Override
        public File[] getExternalMediaDirs() {
            return new File[0];
        }

        @Override
        public String[] fileList() {
            return new String[0];
        }

        @Override
        public File getDir(String name, int mode) {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return null;
        }

        @Override
        public boolean deleteDatabase(String name) {
            return false;
        }

        @Override
        public File getDatabasePath(String name) {
            return null;
        }

        @Override
        public String[] databaseList() {
            return new String[0];
        }

        @Override
        public Drawable getWallpaper() {
            return null;
        }

        @Override
        public Drawable peekWallpaper() {
            return null;
        }

        @Override
        public int getWallpaperDesiredMinimumWidth() {
            return 0;
        }

        @Override
        public int getWallpaperDesiredMinimumHeight() {
            return 0;
        }

        @Override
        public void setWallpaper(Bitmap bitmap) throws IOException {

        }

        @Override
        public void setWallpaper(InputStream data) throws IOException {

        }

        @Override
        public void clearWallpaper() throws IOException {

        }

        @Override
        public void startActivity(Intent intent) {

        }

        @Override
        public void startActivity(Intent intent, Bundle options) {

        }

        @Override
        public void startActivities(Intent[] intents) {

        }

        @Override
        public void startActivities(Intent[] intents, Bundle options) {

        }

        @Override
        public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

        }

        @Override
        public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {

        }

        @Override
        public void sendBroadcast(Intent intent) {

        }

        @Override
        public void sendBroadcast(Intent intent, String receiverPermission) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle user) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {

        }

        @Override
        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

        }

        @Override
        public void sendStickyBroadcast(Intent intent) {

        }

        @Override
        public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

        }

        @Override
        public void removeStickyBroadcast(Intent intent) {

        }

        @Override
        public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

        }

        @Override
        public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

        }

        @Override
        public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

        }

        @Nullable
        @Override
        public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
            return null;
        }

        @Nullable
        @Override
        public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
            return null;
        }

        @Override
        public void unregisterReceiver(BroadcastReceiver receiver) {

        }

        @Nullable
        @Override
        public ComponentName startService(Intent service) {
            return null;
        }

        @Override
        public boolean stopService(Intent service) {
            return false;
        }

        @Override
        public boolean bindService(Intent service, ServiceConnection conn, int flags) {
            return false;
        }

        @Override
        public void unbindService(ServiceConnection conn) {

        }

        @Override
        public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
            return false;
        }

        @Override
        public Object getSystemService(String name) {
            return null;
        }

        @Override
        public int checkPermission(String permission, int pid, int uid) {


            return PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public int checkCallingPermission(String permission) {
            return PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public int checkCallingOrSelfPermission(String permission) {
            return PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public void enforcePermission(String permission, int pid, int uid, String message) {

        }

        @Override
        public void enforceCallingPermission(String permission, String message) {

        }

        @Override
        public void enforceCallingOrSelfPermission(String permission, String message) {

        }

        @Override
        public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

        }

        @Override
        public void revokeUriPermission(Uri uri, int modeFlags) {

        }

        @Override
        public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
            return 0;
        }

        @Override
        public int checkCallingUriPermission(Uri uri, int modeFlags) {
            return 0;
        }

        @Override
        public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
            return 0;
        }

        @Override
        public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
            return 0;
        }

        @Override
        public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

        }

        @Override
        public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

        }

        @Override
        public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

        }

        @Override
        public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {

        }

        @Override
        public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public Context createConfigurationContext(Configuration overrideConfiguration) {
            return null;
        }

        @Override
        public Context createDisplayContext(Display display) {
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);



        backScheduleButton = (Button) findViewById(R.id.backScheduleButton);
        currentScheduleButton = (Button) findViewById(R.id.currentScheduleButton);
        editScheduleButton = (Button) findViewById(R.id.editScheduleButton);

        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                    onBackPressed();
            }
    });

        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        currentScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Calendar begin = Calendar.getInstance();
//                begin.set(2015, Calendar.MARCH, 12, 17, 00);
//                Calendar end = Calendar.getInstance();
//                end.set(2015, Calendar.MARCH, 12, 19, 45);
//                createNewEvent("Exercise1", begin, end);
            }
        });

        currentScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar dateToShow = Calendar.getInstance();
                dateToShow.set(2015, Calendar.MARCH, 12, 17, 00);

                loadViewSchedule(dateToShow);
            }
        });
    }

    private void createNewEvent(String exercise1, Calendar begin, Calendar end) {
        Calendar dateToShow = Calendar.getInstance();
        dateToShow.set(2015, Calendar.MARCH, 12, 17, 00);
        long epochMillis = dateToShow.getTimeInMillis();

        String data = "" + begin.toString() + "" + end.toString();
        Uri eventUri = CalendarService.addEvent(ctx, accountName, "EXERCISE 1", data);
        Uri.Builder uriBuilder = eventUri.buildUpon();
        uriBuilder.appendPath("time");
        ContentUris.appendId(uriBuilder, epochMillis);
        Uri uri = uriBuilder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadViewSchedule(Calendar dateToShow){
        long epochMillis = dateToShow.getTimeInMillis();

        Uri.Builder uriBuilder = CalendarContract.CONTENT_URI.buildUpon();
        uriBuilder.appendPath("time");
        ContentUris.appendId(uriBuilder, epochMillis);
        Uri uri = uriBuilder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }
}