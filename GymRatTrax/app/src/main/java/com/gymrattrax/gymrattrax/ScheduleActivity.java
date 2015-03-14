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
    private EditText nameEditText;

    public String accountName = Account.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        currentScheduleButton = (Button) findViewById(R.id.currentScheduleButton);
        editScheduleButton = (Button) findViewById(R.id.editScheduleButton);


        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        currentScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //===== This code was commented for the 3/12 demo
//                Calendar dateToShow = Calendar.getInstance();
//                dateToShow.set(2015, Calendar.MARCH, 12, 17, 00);
//
//                loadViewSchedule(dateToShow);
                //===== This code was commented for the 3/12 demo

                //===== This code was added for the 3/12 demo
                Intent intent = new Intent (ScheduleActivity.this, CurrentScheduleActivity.class);
                startActivity(intent);
                //===== This code was added for the 3/12 demo
            }
        });
    }

//    private void createNewEvent(String exercise1, Calendar begin, Calendar end) {
//        Calendar dateToShow = Calendar.getInstance();
//        dateToShow.set(2015, Calendar.MARCH, 12, 17, 00);
//        long epochMillis = dateToShow.getTimeInMillis();
//
//        String data = "" + begin.toString() + "" + end.toString();
//        Uri eventUri = CalendarService.addEvent(ctx, accountName, "EXERCISE 1", data);
//        Uri.Builder uriBuilder = eventUri.buildUpon();
//        uriBuilder.appendPath("time");
//        ContentUris.appendId(uriBuilder, epochMillis);
//        Uri uri = uriBuilder.build();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        startActivity(intent);
//    }

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