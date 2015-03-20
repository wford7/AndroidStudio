package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class StrengthWorkoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_workout);

        TextView title = (TextView)findViewById(R.id.strength_title);
        TextView strengthSets = (TextView)findViewById(R.id.strength_sets);
        TextView strengthReps = (TextView)findViewById(R.id.strength_reps);

        Bundle b = getIntent().getExtras();
        int ID = b.getInt("ID");

        DBHelper dbh = new DBHelper(this);
        WorkoutItem currentWorkout = dbh.getWorkoutById(ID);
        int sets = ((StrengthWorkoutItem)currentWorkout).getSetsScheduled();
        int reps = ((StrengthWorkoutItem)currentWorkout).getRepsScheduled();
        String name = currentWorkout.getName().toString();
        title.setText(name);

        //populate screen with
        for (int i = 0; i < sets; i++){

        }

//        strengthSets.setText("Reps: "+ Integer.toString(reps));
        strengthReps.setText("Sets: " + Integer.toString(sets));

        //radio buttons that user can select that describes difficulty of exercise.  this will be "easy" "moderate" "hard"
        //EditText that user may input amount of time taken to complete workout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_strength_workout, menu);
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
}
