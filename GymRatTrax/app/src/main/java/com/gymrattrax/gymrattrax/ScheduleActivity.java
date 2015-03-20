package com.gymrattrax.gymrattrax;

import android.accounts.Account;
import android.content.ContentUris;
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

import static com.gymrattrax.gymrattrax.R.id.schedule_google_calendar_button;


public class ScheduleActivity extends ActionBarActivity {

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
                    Toast.makeText(getApplicationContext(), ((TextView) view).getText() + ": WORKOUT DETAILS", Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button addWorkoutButton = (Button) findViewById(R.id.addWorkoutButton);
        Button editWorkoutButton = (Button) findViewById(R.id.schedule_activity_edit_workout);
        Button backScheduleButton = (Button) findViewById(R.id.schedule_back_button);
        final Button openGoogleCalendarButton = (Button) findViewById(schedule_google_calendar_button);

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

            ;

        });
    }

//                openGoogleCalendarButton.setOnClickListener(new Button.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        Calendar dateToShow = Calendar.getInstance();
//                        dateToShow.set(2015, Calendar.MARCH, 25, 17, 0);
//                        loadViewSchedule(dateToShow);
//                    }
//                });
//            }


//        editWorkoutButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


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

