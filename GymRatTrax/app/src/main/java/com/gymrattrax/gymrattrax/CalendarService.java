package com.gymrattrax.gymrattrax;


import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.Display;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarService extends ScheduleActivity {
    private int calId;
    private long eventID;
    private Date currentDay;
    private double time;

    public CalendarService() {
    }

    public int getID() {
        return calId;
    }

    public void setID(int ID) { this.calId = ID; }

    public Date getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Date currentDay) {
        this.currentDay = currentDay;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

////    public Date getStartTime() {
////        return startTime;
////    }
////
////    public void setStartTime(Date startDay) {
////        this.startTime = startDay;
////    }
////
////    public Date getEndTime() {
////        return endTime;
////    }
////
////    public void setEndTime(Date endDay) {
////        this.endTime = endDay;
////    }
//
//
//    public String name = "" + getStartTime().toString() + " - " + getEndTime().toString();

    public String name = "TEST";

    public String accountName = Account.class.getName();

    String[] projection = new String[] { CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.ALL_DAY, CalendarContract.Events.EVENT_LOCATION };

    public static Uri createNewCalendar(Context ctx, String name, String accountName) {
        Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
        target = target.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "ACCOUNT_TYPE_LOCAL").build();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, "ACCOUNT_TYPE_LOCAL");
        values.put(CalendarContract.Calendars.NAME, name);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x00FF00);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_ROOT);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().toString());
        values.put(CalendarContract.Calendars.CAN_PARTIALLY_UPDATE, 1);
        values.put(CalendarContract.Calendars.ALLOWED_REMINDERS, CalendarContract.Reminders.METHOD_DEFAULT);
        values.put(CalendarContract.Calendars.CAL_SYNC8, System.currentTimeMillis());

        Uri newCalendar = ctx.getContentResolver().insert(target, values);

        return newCalendar;
    }

    public long id = getID();

//    public String data = "" + startTime + "/" + endTime;

    public static Uri addEvent(Context ctx, String accountName, String name, String data) {
        Uri target = Uri.parse(CalendarContract.Calendars.CONTENT_URI.toString());
        target = target.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "ACCOUNT_TYPE_LOCAL").build();

        long startMillis = 0;
        long endMillis = 0;
        String[] div = data.split("/");
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, Integer.parseInt(div[0]) - 1, Integer.parseInt(div[1]));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, Integer.parseInt(div[0]) - 1, Integer.parseInt(div[1]));
        endMillis = endTime.getTimeInMillis();

        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.TITLE, name);
        cv.put(CalendarContract.Events.DTSTART, startMillis);
        cv.put(CalendarContract.Events.DTEND, endMillis);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());

        Uri newEvent = ctx.getContentResolver().insert(target, cv);
        long eventID = Long.parseLong(newEvent.getLastPathSegment());


        return newEvent;
    }


    public static boolean deleteEvent(Context ctx, String accountName, String name, long eventID) {



        return true;
    }

    public Uri.Builder getAllEvents() {
        Uri.Builder builder = Uri.parse(getCalendarUriBase() + "/instances/when").buildUpon();
        long now = new Date().getTime();
        ContentUris.appendId(builder, now - DateUtils.DAY_IN_MILLIS);
        ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS);
        return builder;
    }

    private String getCalendarUriBase() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id",  "displayname" }, null, null, null);
        cursor.moveToFirst();
        String[] CalNames = new String[cursor.getCount()];
        int[] CalIds = new int[cursor.getCount()];
        for (int i = 0; i < CalNames.length; i++) {
            CalIds[i] = cursor.getInt(0);
            CalNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }
        cursor.close();

        return null;
    }

    public Intent viewEvent() {
        long eventID = 208;
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri);
        return intent;
    }
}