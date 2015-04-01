package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class AddCardioWorkoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cardio_details);

        TextView title = (TextView) findViewById(R.id.add_workout_cardio);
//        TextView strengthSets = (TextView) findViewById(R.id.strength_sets);
//        TextView strengthReps = (TextView) findViewById(R.id.strength_reps);
        final Button nextButton = (Button) findViewById(R.id.next);

        nextButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddCardioWorkoutActivity.this.loadDateTime();
            }
        });
    }

    private void loadDateTime() {
        Intent intent = new Intent(AddCardioWorkoutActivity.this, SelectDateTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
//package com.gymrattrax.gymrattrax;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.view.View;
//import android.widget.Button;
//
//public class AddStrengthWorkoutActivity extends ActionBarActivity {
//    private Button addExerciseButton;
//    private Button addCardioWorkout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_strength_workout);
//
//        addExerciseButton = (Button) findViewById(R.id.addExerciseButton);
//
//        addExerciseButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AddStrengthWorkoutActivity.this, ScheduleActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }
//}
