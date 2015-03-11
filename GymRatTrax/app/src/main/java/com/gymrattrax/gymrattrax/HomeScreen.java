package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.jjoe64.graphview.*;

import java.util.Timer;
import java.util.TimerTask;

public class HomeScreen extends ActionBarActivity {

    private Button BeginWorkoutButton, ViewScheduleButton, ViewProfileButton, ViewProgressButton,
            CalorieNegationButton, EditSettingsButton;
    private ImageView GymRat;
    private ScrollView updateLayoutScroll;
    private int debugCheck;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initiate tutorial/profile creation if there is no Profile ID in database
        Profile create = new Profile(this);
        if (!create.isComplete() && DBContract.ALLOW_DEBUG) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Profile is not adequately complete. " +
                            "In the future, launch Create Profile activity here. " +
                            "DO NOT ATTEMPT CALORIE NEGATION.", Toast.LENGTH_LONG);
            toast.show();
        }
        setContentView(R.layout.activity_home_screen);
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);


        /**populate ScrollView's child LinearLayout with workout items that are schedule for the current date
         * display - workout item, time
         * if there are no scheduled workout items, display "Nothing scheduled for today"
         **/


        GymRat = (ImageView)findViewById(R.id.home_rat);
        BeginWorkoutButton = (Button)findViewById(R.id.BeginWorkoutButton);
        ViewScheduleButton = (Button)findViewById(R.id.ViewScheduleButton);
        ViewProfileButton = (Button)findViewById(R.id.ViewProfileButton);
        ViewProgressButton = (Button)findViewById(R.id.ViewProgressButton);
        CalorieNegationButton = (Button)findViewById(R.id.CalorieNegationButton);
        EditSettingsButton = (Button)findViewById(R.id.EditSettingsButton);

        addButton = (ImageButton)findViewById(R.id.add_button);


        displayCurrentWorkouts();

        debugCheck = 0;

        GymRat.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
            }});

        if (DBContract.ALLOW_DEBUG) {
            GymRat.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    debugCheck++;
                    if (debugCheck == 3) {
                        debugCheck = 0;
                        Intent intent = new Intent(HomeScreen.this, DBDebug.class);
                        startActivity(intent);
                    }
                    startTimer();
                    return true;
                }
            });
        }

        BeginWorkoutButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        ViewProgressButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadProgress(view);
           }
        });

        ViewScheduleButton.setOnClickListener(new Button.OnClickListener(){

            @Override
        public void onClick(View view){
                loadSchedules(view);
            }
        });

        ViewProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadFitnessProfile(view);
            }
        });

        BeginWorkoutButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadDailyWorkout(view);
            }
        });

        CalorieNegationButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadCalorieNegation(view);
            }
        });

        EditSettingsButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {

                loadSettings(view);
            }
        });

    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                debugCheck = 0;
            }
        }, 7000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }


    //the following method is triggered when user selects "Begin Workout" button from main page
    //if no workout is schedule, display message instructing user to "Create New Plan"
    public void loadDailyWorkout(View view){

        //load current workout schedule for current date

        Intent intent = new Intent (HomeScreen.this, DailyWorkoutActivity.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Calorie Negation" button from the main page
    public void loadCalorieNegation(View view){

        Intent intent = new Intent (HomeScreen.this, CalorieNegationActivity.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Fitness Profile" button from the main page
    final public void loadFitnessProfile(View view){

        Intent intent = new Intent (HomeScreen.this, ProfileActivity.class);
        startActivity(intent);
    }


    public void loadSettings(View view){

        Intent intent = new Intent (HomeScreen.this, SettingsActivity.class);
        startActivity(intent);
    }


    //this method is triggered when user selects "View Progress" button from the main page
    public void loadProgress(View view){

        Intent intent = new Intent (HomeScreen.this, ProgressActivity.class);
        startActivity(intent);
    }

    public void loadSchedules(View view){
        Intent intent = new Intent (HomeScreen.this, ScheduleActivity.class);
        startActivity(intent);
    }

    /**
     * pull workouts (current day) from database and then populate ScrollView child
     */
//    private void displayCurrentWorkouts() {
//        DBHelper dbh = new DBHelper(this);
////        WorkoutItem[] workoutItems = dbh.getWorkoutsForToday();
//        WorkoutItem[] workoutItems = new WorkoutItem[0];
//        for (WorkoutItem workoutItem : workoutItems) {
//            LinearLayout dailyLayout = (LinearLayout) findViewById(R.id.LayoutScroll);
//            TextView dailyWorkout = new TextView(HomeScreen.this);
//            dailyWorkout.setText(workoutItem.getName());
//            updateLayoutScroll = (ScrollView) findViewById(R.id.scrollView);
//            dailyLayout.addView(dailyWorkout);
//            updateLayoutScroll.addView(dailyLayout);
//            // " display(workoutItem[i]) "
//        }
//        dbh.close();
//    }
    private void displayCurrentWorkouts() {
        DBHelper dbh = new DBHelper(this);
//        WorkoutItem[] workoutItems = dbh.getWorkoutsForToday();
        WorkoutItem[] workoutItems = new WorkoutItem[0];
        for (WorkoutItem workoutItem : workoutItems) {
            LinearLayout dailyLayout = (LinearLayout) findViewById(R.id.LayoutScroll);
            TextView dailyWorkout = new TextView(HomeScreen.this);
            dailyWorkout.setText(workoutItem.getName());
            updateLayoutScroll = (ScrollView) findViewById(R.id.scrollView);
            dailyLayout.addView(dailyWorkout);
            // " display(workoutItem[i]) "
        }
        dbh.close();
    }
}
