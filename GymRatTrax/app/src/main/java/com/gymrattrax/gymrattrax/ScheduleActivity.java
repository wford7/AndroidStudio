package com.gymrattrax.gymrattrax;

import android.accounts.Account;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class ScheduleActivity extends ActionBarActivity {

    GridView gridView;

    final DBHelper dbh = new DBHelper(this);
    WorkoutItem[] workouts = new WorkoutItem[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        displayUpcomingWorkouts();

        final Button addWorkoutButton = (Button) findViewById(R.id.addWorkoutButton);
        Button editWorkoutButton = (Button) findViewById(R.id.schedule_activity_edit_workout);
        Button backScheduleButton = (Button) findViewById(R.id.schedule_back_button);
        final Button openGoogleCalendarButton = (Button) findViewById(R.id.openGoogleCalendarButton);

        backScheduleButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ScheduleActivity.this, v);
                popup.inflate(R.menu.menu_add_workout);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_workout_strength:
                                ScheduleActivity.this.loadStrengthWorkout();
                                return true;
                            case R.id.add_workout_cardio:
                                ScheduleActivity.this.loadCardioWorkout();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        openGoogleCalendarButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar dateToShow = Calendar.getInstance();
                dateToShow.set(2015, Calendar.MARCH, 25, 17, 0);
                loadViewSchedule(dateToShow);
            }
        });

        editWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context ctx = getApplicationContext();
//                CalendarService.addEvent(ctx, "GymRatTrax", "Cardio Workout", "Running/ 3 miles/ CaloriesToBurn 495");
                loadViewWorkoutEvent();
            }
        });
    }

    private void displayUpcomingWorkouts() {
        int i = 0;
        workouts = dbh.getWorkoutsForToday();
        String[] upcoming_workouts = new String[workouts.length];

        for (final WorkoutItem w : workouts) {
            upcoming_workouts[i] = w.getName().toString();
            double minutesDbl = w.getTimeScheduled();
            int secondsTotal = (int) (minutesDbl * 60);
            int seconds = secondsTotal % 60;
            int minutes = (secondsTotal - seconds) / 60;
            String time = minutes + " minutes, " + seconds + " seconds";
            time = dbh.displayDateTime(w.getDateScheduled()) + ": " + time;
            String infoString = "" + w.getName().toString() + ": " + time;
            upcoming_workouts[i] = infoString;
            i++;
        }

        gridView = (GridView) findViewById(R.id.gridView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, upcoming_workouts);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), ((TextView) view).getText() + ": WORKOUT DETAILS", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadViewWorkoutEvent() {
        Intent intent = new Intent(CalendarService.viewEvent());
        startActivity(intent);
    }

    private void loadStrengthWorkout() {
        Intent intent = new Intent(ScheduleActivity.this, StrengthWorkoutActivity.class);
        startActivity(intent);
    }

    private void loadCardioWorkout() {
        Intent intent = new Intent(ScheduleActivity.this, CardioWorkoutActivity.class);
        startActivity(intent);
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