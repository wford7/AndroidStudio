package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class AddWorkoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        String[] workout_names = getAllWorkouts();
        displayAllWorkouts(workout_names);

        TextView title = (TextView) findViewById(R.id.strength_title);
;
    }

    private void displayAllWorkouts(String[] s) {

        setContentView(R.layout.activity_add_workout);
        GridView gridView = (GridView) findViewById(R.id.workouts_grid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create strength workout item and set reps, sets, and weight

                String s = ((TextView) view).getText().toString();
                switch (ExerciseName.fromString(s)) {
                    case WALK:
                        displayCardioDetails();
                        break;
                    case JOG:
                        displayCardioDetails();
                        break;
                    case RUN:
                        displayCardioDetails();
                        break;
                    default:
                        displayStrengthDetails(s);
                }
            }
        });
    }

    private void displayCardioDetails() {
        Intent intent = new Intent(AddWorkoutActivity.this, AddCardioWorkoutActivity.class);
        startActivity(intent);
    }

    private void displayStrengthDetails(String s) {
        Intent intent = new Intent(AddWorkoutActivity.this, AddStrengthWorkoutActivity.class);
        ExerciseName details = ExerciseName.fromString(s);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_strength_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public String[] getAllWorkouts() {
        return ExerciseName.getAllExerciseNames();
    }
}
