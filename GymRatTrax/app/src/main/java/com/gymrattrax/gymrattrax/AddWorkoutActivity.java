package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class AddWorkoutActivity extends ActionBarActivity {
    private Button addStrengthWorkoutButton;
    private Button addCardioWorkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        addStrengthWorkoutButton = (Button) findViewById(R.id.add_workout_strength);
        addCardioWorkoutButton = (Button) findViewById(R.id.add_workout_cardio);

        addStrengthWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWorkoutActivity.this, StrengthWorkoutActivity.class);
                startActivity(intent);
            }
        });

    }
}
