package com.gymrattrax.gymrattrax;

import android.accounts.Account;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;



public class ScheduleActivity extends ActionBarActivity {
    private Button backScheduleButton;
    private Button addWorkoutButton;
    private Button editWorkoutButton;
    private Button openGoogleCalendarButton;

    GridView gridView;

    static final String[] upcoming_workouts = new String[]{
            "Strength Workout", "Wed. 4/1/2015", "5:15 pm",
            "Cardio Workout", "Thurs. 4/2/2015", "2:30 pm",
            "Cardio Workout", "Thurs. 4/2/2015", "5:15 pm",
            "Strength Workout", "Fri. 4/3/2015", "6:00 pm",
            "Strength Workout", "Sat. 4/4/2015", "9:00 am",
            "Cardio Workout", "Sat. 4/4/2015", "2:30 pm",
            "Cardio Workout", "Sun. 4/5/2015", "12:15 pm",
            "Strength Workout", "Mon. 4/6/2015", "6:45 pm",
            "Strength Workout", "Mon. 4/6/2015", "7:15 pm",
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
        openGoogleCalendarButton = (Button) findViewById(R.id.schedule_google_calendar_button);

        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                loadAddWorkout(view);
            }
        });

//        openGoogleCalendarButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Calendar dateToShow = Calendar.getInstance();
//                dateToShow.set(2015, Calendar.MARCH, 25, 17, 0);
//                loadViewSchedule(dateToShow);
//            }
//        });
    }

//        editWorkoutButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


    private void loadAddWorkout(View view) {
        Intent intent = new Intent(ScheduleActivity.this, AddWorkoutActivity.class);
        startActivity(intent);
    }

//CALENDAR SYNCING
// MOVE THIS TO CALENDAR SERVICE
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

    public void loadViewSchedule(Calendar dateToShow) {
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
