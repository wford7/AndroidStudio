package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CalorieNegationActivity extends ActionBarActivity {

    Button SuggestWorkoutButton;
    EditText NegateEditText;
    LinearLayout linearContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calorie_negation);

        SuggestWorkoutButton = (Button) findViewById(R.id.negate_cal_button);
        NegateEditText = (EditText) findViewById(R.id.negate_calories);
        linearContainer = (LinearLayout) findViewById(R.id.suggestions_layout);

        SuggestWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                /** grab random workout item ID, calculate how long it will take to burn
                 *  x amount of calories, return workout.
                 *  update
                 **/
                linearContainer.removeAllViewsInLayout();
                int caloriesToNegate = Integer.parseInt(NegateEditText.getText().toString());
                Profile p = new Profile();

                /*
                NOTE: I believe that, when a Profile Object is instantiated, it pulls in database
                information then, and it will calculate and store the BMR at that moment. Because
                this might take some time, I've manually stored an example BMR here. In the future,
                a user will not even be able to go through the main menu normally if the app detects
                that the Profile section is unable to be completely filled in. The user will be
                forced to enter their information in ahead of time. I wanted to get this part of the
                algorithm in soon, so I'll clean this back up when I'm ready. -CS
                 */
                p.setBMR(2300);

                double BMR = p.getBMR();

                /*
                NOTE: Also, now that I understand more of how we determine METs values, I feel like
                there is a more efficient and more accurate way to do it. Until I figure that out
                completely, I am just using some local variables here. -CS
                 */
                double cardio_walk = 3.0;
                double cardio_jog = 7.0;
                double cardio_run = 11.0;
                double strength_light = 3.5;
                double strength_vigorous = 6.0;

                double[] METsValues = new double[]{strength_light, strength_vigorous,
                        cardio_walk, cardio_jog, cardio_run};
                for (int i = 0; i < METsValues.length; i++) {
                    double minutesDbl = ((60 * 24 * caloriesToNegate) / (METsValues[i] * BMR));
                    int secondsTotal = (int)(minutesDbl * 60);
                    int seconds = secondsTotal % 60;
                    int minutes = (secondsTotal - seconds) / 60;
                    TextView newView = new TextView(CalorieNegationActivity.this);
                    newView.setId(1000 + i);
                    String text = "Exercise <name of ";
                    if (i == 0)
                        text += "light strength";
                    else if (i == 1)
                        text += "vigorous strength";
                    else if (i == 2)
                        text += "walking";
                    else if (i == 3)
                        text += "jogging";
                    else if (i == 4)
                        text += "running";
                    text += " exercise> could be done for " + minutes + " minutes and " + seconds +
                            " seconds.";
                    newView.setText(text);
                    linearContainer.addView(newView);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calorie_negation, menu);
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
