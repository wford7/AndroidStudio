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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;



public class ScheduleActivity extends ActionBarActivity {
    private Button backScheduleButton;
    private Button addWorkoutButton;
    private Button editWorkoutButton;

    GridView gridView;

    static final String[] upcoming_workouts = new String[] {
            "Strength Workout","Wed. 4/1/2015", "5:15 pm",
            "Cardio Workout", "Thurs. 4/2/2015", "2:30 pm",
            "Cardio Workout", "Thurs. 4/2/2015", "5:15 pm",
            "Strength Workout", "Fri. 4/3/2015", "6:00 pm",
            "Strength Workout","Sat. 4/4/2015", "9:00 am",
            "Cardio Workout", "Sat. 4/4/2015", "2:30 pm",
            "Cardio Workout", "Sun. 4/5/2015", "12:15 pm",
            "Strength Workout", "Mon. 4/6/2015", "6:45 pm",
            "Strength Workout","Mon. 4/6/2015", "7:15 pm",
            "Cardio Workout", "Tues. 4/7/2015", "2:30 pm",
            "Cardio Workout", "Tues. 4/7/2015", "5:15 pm",
            "Strength Workout", "Tues. 4/7/2015", "6:00 pm"};

    public String accountName = Account.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        gridView = (GridView) findViewById(R.id.gridView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, upcoming_workouts);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == 3 || position == 6 || position == 9) {
                    String workoutId = "Strength#1";
                    Toast.makeText(getApplicationContext(), ((TextView) view).getText() + ": WORKOUT DETAILS", Toast.LENGTH_LONG).show();
                }
            }
        });

        addWorkoutButton = (Button) findViewById(R.id.addWorkoutButton);
        editWorkoutButton = (Button) findViewById(R.id.schedule_activity_edit_workout);
        backScheduleButton = (Button) findViewById(R.id.schedule_back_button);

        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addWorkoutButton.setOnClickListener(new Button.OnClickListener() {

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

    private String workoutDetails(String details) {
        details = "CURLS: 25 lbs SETS:2 REPS:20";
        return details;
    }

//    private void addWorkout(String exercise1, Calendar begin, Calendar end) {
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