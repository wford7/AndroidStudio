package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.jjoe64.graphview.*;

import java.util.Timer;
import java.util.TimerTask;

public class HomeScreen extends ActionBarActivity {

    private Button BeginWorkoutButton, ViewScheduleButton, ViewProfileButton, ViewProgressButton, CalorieNegationButton, EditSettingsButton;
    private ImageView GymRat;
    private ScrollView updateLayoutScroll;
    private GraphView graph, graph2;
    private int debugCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initiate tutorial/profile creation if there is no Profile ID in database
        Profile create = new Profile();
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

        displayCurrentWorkouts();
        graph = (GraphView)findViewById(R.id.graph);
        graph2 = (GraphView)findViewById(R.id.graph2);

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
            public void onClick(View view) {
                loadDailyWorkout(view);
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


        ViewProgressButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadProgress(view);
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
        Intent intent = new Intent (HomeScreen.this, ViewScheduleActivity.class);
        startActivity(intent);
    }

    /**
     * pull current workouts from database and then populate scrollview child
     */
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
            updateLayoutScroll.addView(dailyLayout);
            // " display(workoutItem[i]) "
        }
        dbh.close();
    }
}
