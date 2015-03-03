package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
// import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;

//add button press listeners


public class HomeScreen extends ActionBarActivity {

    private Button BeginWorkoutButton, ViewScheduleButton, ViewProfileButton, ViewProgressButton, CalorieNegationButton, EditSettingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BeginWorkoutButton = (Button)findViewById(R.id.BeginWorkoutButton);
        ViewScheduleButton = (Button)findViewById(R.id.ViewScheduleButton);
        ViewProfileButton = (Button)findViewById(R.id.ViewProfileButton);
        ViewProgressButton = (Button)findViewById(R.id.ViewProgressButton);
        CalorieNegationButton = (Button)findViewById(R.id.CalorieNegationButton);
        EditSettingsButton = (Button)findViewById(R.id.EditSettingsButton);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        BeginWorkoutButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                loadCurrentSchedule(view);
            }
        });

//        ViewScheduleButton.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//        public void onClick(View view){
//                view.startAnimation(animAlpha);
//                loadScheduleEdit(view);
//            }
//        });

        ViewProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                loadFitnessProfile(view);
            }
        });


        ViewProgressButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                loadProgress(view);
            }
        });

        CalorieNegationButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                loadCalorieNegation(view);
            }
        });

        EditSettingsButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                loadSettings(view);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }


    //the following method is triggered when user selects "Begin Workout" button from main page
    //if no workout is schedule, display message instructing user to "Create New Plan"
    public void loadCurrentSchedule(View view){

        //load current workout schedule for current date

        Intent intent = new Intent (this, DailyWorkout.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Calorie Negation" button from the main page
    public void loadCalorieNegation(View view){

        Intent intent = new Intent (this, CalorieNegation.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Fitness Profile" button from the main page
    final public void loadFitnessProfile(View view){

        Intent intent = new Intent (this, FitnessProfile.class);
        startActivity(intent);
    }

<<<<<<< HEAD
    public void loadSettings(View view){
        //this method is triggered when user selects "Edit Settings" button from the main page
=======
    //this method is triggered when user selects "Edit Settings" button from the main page
    public void loadSettings(View view){
>>>>>>> draft_cjs

        Intent intent = new Intent (this, Settings.class);
        startActivity(intent);
    }

<<<<<<< HEAD
    public void loadProgress(View view){
        //this method is triggered when user selects "View Progress" button from the main page
=======
    //this method is triggered when user selects "View Progress" button from the main page
    public void loadProgress(View view){
>>>>>>> draft_cjs

        Intent intent = new Intent (this, ViewProgress.class);
        startActivity(intent);
    }
}
