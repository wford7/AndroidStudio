package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;


public class CalorieNegationActivity extends ActionBarActivity {

    Button SuggestWorkoutButton;
    EditText NegateEditText;
    LinearLayout linearContainer;
    Button AddLightStrength;
    Button AddVigorousStrength;
    Button AddWalking;
    Button AddJogging;
    Button AddRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calorie_negation);

        SuggestWorkoutButton = (Button) findViewById(R.id.negate_cal_button);
        NegateEditText = (EditText) findViewById(R.id.negate_calories);
        linearContainer = (LinearLayout) findViewById(R.id.suggestions_layout);
        AddLightStrength = new Button(CalorieNegationActivity.this);
        AddVigorousStrength = new Button(CalorieNegationActivity.this);
        AddWalking = new Button(CalorieNegationActivity.this);
        AddJogging = new Button(CalorieNegationActivity.this);
        AddRunning = new Button(CalorieNegationActivity.this);

        SuggestWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                /** grab random workout item ID, calculate how long it will take to burn
                 *  x amount of calories, return workout.
                 *  update
                 **/

                //Remove button views if they have already been used
                if((AddLightStrength.getParent() != null)){
                    ((TableRow)AddLightStrength.getParent()).removeView(AddLightStrength);
                }

                if((AddVigorousStrength.getParent() !=null)){
                    ((TableRow)AddVigorousStrength.getParent()).removeView(AddVigorousStrength);
                }

                if((AddWalking.getParent() != null)){
                    ((TableRow)AddWalking.getParent()).removeView(AddWalking);
                }

                if((AddJogging.getParent() !=null)){
                    ((TableRow)AddJogging.getParent()).removeView(AddJogging);
                }

                if((AddRunning.getParent() != null)){
                    ((TableRow)AddRunning.getParent()).removeView(AddRunning);
                }

                linearContainer.removeAllViewsInLayout();
                TableLayout a = new TableLayout(CalorieNegationActivity.this);
                a.removeAllViews();

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
                //Linear
                linearContainer.addView(a);

                for (int i = 0; i < METsValues.length; i++) {
                    double minutesDbl = ((60 * 24 * caloriesToNegate) / (METsValues[i] * BMR));
                    int secondsTotal = (int) (minutesDbl * 60);
                    int seconds = secondsTotal % 60;
                    int minutes = (secondsTotal - seconds) / 60;
                    TextView newView = new TextView(CalorieNegationActivity.this);
                    TableRow row = new TableRow(CalorieNegationActivity.this);
                    row.setId(1000 + i);
                    newView.setId(1000 + i);
                    row.removeAllViews();
                    String text = "Exercise <name of ";
                    if (i == 0) {
                        text += "light strength exercise> could be done for " + minutes + " minutes and " +
                                seconds + " seconds.";
                        newView.setText(text);
                        LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f);
                        newView.setLayoutParams(params);

                        row.addView(newView);

                        AddLightStrength.setWidth(10);
                        row.addView(AddLightStrength);
                        AddLightStrength.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                        a.addView(row);

                        //


                    } else if (i == 1) {
                        text += "vigorous exercise> could be done for " + minutes + " minutes and " +
                                seconds + " seconds.";

                        newView.setText(text);
                        LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f);
                        newView.setLayoutParams(params);

                        row.addView(newView);

                        AddVigorousStrength.setWidth(10);
                        row.addView(AddVigorousStrength);
                        AddVigorousStrength.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                        a.addView(row);
                        // linearContainer.addView(VigorousStrength);
                        // VigorousStrength.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                    } else if (i == 2) {
                        text += "vigorous exercise> could be done for " + minutes + " minutes and " +
                                seconds + " seconds.";
                        newView.setText(text);
                        LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f);
                        newView.setLayoutParams(params);

                        row.addView(newView);

                        AddWalking.setWidth(10);
                        row.addView(AddWalking);
                        AddWalking.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                        a.addView(row);

                    } else if (i == 3) {
                        text += "vigorous exercise> could be done for " + minutes + " minutes and " +
                                seconds + " seconds.";

                        newView.setText(text);
                        LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f);
                        newView.setLayoutParams(params);

                        row.addView(newView);

                        AddJogging.setWidth(10);
                        row.addView(AddJogging);
                        AddJogging.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                        a.addView(row);
                    } else if (i == 4) {
                        text += "vigorous exercise> could be done for " + minutes + " minutes and " +
                                seconds + " seconds.";

                        newView.setText(text);
                        LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f);
                        newView.setLayoutParams(params);

                        row.addView(newView);

                        AddRunning.setWidth(10);
                        row.addView(AddRunning);
                        AddRunning.setBackground(getResources().getDrawable(R.drawable.add_button_press));

                        a.addView(row);
                        // newView.setText(text);
//                    linearContainer.addView(newView);
                    }
                }
            }

        });

        AddLightStrength.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create light strength workout item and store it in today's schedule
                BackToHomeScreen(view);
            }
        });

        AddVigorousStrength.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create vigorous strength workout item and store it in today's schedule
                BackToHomeScreen(view);
            }
        });

        AddWalking.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create walking workout item and store it in today's schedule

                BackToHomeScreen(view);
            }
        });

        AddJogging.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create jogging workout item and store it in today's schedule

                BackToHomeScreen(view);
            }
        });

        AddRunning.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create running workout item and store it in today's schedule

                BackToHomeScreen(view);
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

    public void BackToHomeScreen(View view){
        Intent intent = new Intent (CalorieNegationActivity.this, HomeScreen.class);
        startActivity(intent);
    }

}
