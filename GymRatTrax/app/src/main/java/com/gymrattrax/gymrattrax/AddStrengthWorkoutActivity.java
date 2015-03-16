package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class AddStrengthWorkoutActivity extends ActionBarActivity {
    private Button addExerciseButton;
    private Button addCardioWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strength_workout);

        addExerciseButton = (Button) findViewById(R.id.addExerciseButton);

        addExerciseButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStrengthWorkoutActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });


    }
}