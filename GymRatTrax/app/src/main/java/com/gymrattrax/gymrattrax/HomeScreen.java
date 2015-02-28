package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;



public class HomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void loadCurrentSchedule(View view){
        //this method is triggered when user selects "Begin Workout" button from main page
        //if no workout is schedule, display message instructing user to "Create New Plan"
        //load current workout schedule for current date

        Intent intent = new Intent (this, DailyWorkout.class);
        startActivity(intent);
    }

    public void loadCalorieNegation(View view){
        //this method is triggered when user selects "Calorie Negation" button from the main page

        Intent intent = new Intent (this, CalorieNegation.class);
        startActivity(intent);
    }

    public void loadFitnessProfile(View view){
        //this method is triggered when user selects "Fitness Profile" button from the main page

        Intent intent = new Intent (this, FitnessProfile.class);
        startActivity(intent);
    }
}
