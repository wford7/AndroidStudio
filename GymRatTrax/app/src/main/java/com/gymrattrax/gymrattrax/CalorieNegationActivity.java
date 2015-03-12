package com.gymrattrax.gymrattrax;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalorieNegationActivity extends ActionBarActivity {

    Button SuggestWorkoutButton;
    EditText NegateEditText;
    LinearLayout linearContainer;
    Button AddLightStrength;
    Button AddVigorousStrength;
    Button AddWalking;
    Button AddJogging;
    Button AddRunning;
    double[] times;

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
        times = new double[5];

        SuggestWorkoutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                /** grab random workout item ID, calculate how long it will take to burn
                 *  x amount of calories, return workout.
                 *  update
                 **/

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Remove button views if they have already been used
                if((AddLightStrength.getParent() != null)){
                    ((LinearLayout)AddLightStrength.getParent()).removeView(AddLightStrength);
                }

                if((AddVigorousStrength.getParent() != null)){
                    ((LinearLayout)AddVigorousStrength.getParent()).removeView(AddVigorousStrength);
                }

                if((AddWalking.getParent() != null)){
                    ((LinearLayout)AddWalking.getParent()).removeView(AddWalking);
                }

                if((AddJogging.getParent() != null)){
                    ((LinearLayout)AddJogging.getParent()).removeView(AddJogging);
                }

                if((AddRunning.getParent() != null)){
                    ((LinearLayout)AddRunning.getParent()).removeView(AddRunning);
                }

                linearContainer.removeAllViewsInLayout();
                TableLayout a = new TableLayout(CalorieNegationActivity.this);
                a.removeAllViews();

                int caloriesToNegate;
                try {
                    caloriesToNegate = Integer.parseInt(NegateEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast t = Toast.makeText(getApplicationContext(), "Invalid input.",
                            Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (caloriesToNegate > 400) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Do not use this feature to 'work off' an entire meal. " +
                                    "Try a smaller number.",
                            Toast.LENGTH_SHORT);
                    t.show();
                    return;
                } else if (caloriesToNegate < 0) {
                    Toast t = Toast.makeText(getApplicationContext(), "Calories must be positive.",
                            Toast.LENGTH_SHORT);
                    t.show();
                    return;
                } else if (caloriesToNegate == 0) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Zero calories? No workout needed!",
                            Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                Profile p = new Profile(CalorieNegationActivity.this);

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
                    times[i] = minutesDbl;
                    TableRow row = new TableRow(CalorieNegationActivity.this);
                    LinearLayout main = new LinearLayout(CalorieNegationActivity.this);
                    LinearLayout stack = new LinearLayout(CalorieNegationActivity.this);
                    TextView viewTitle = new TextView(CalorieNegationActivity.this);
                    TextView viewTime = new TextView(CalorieNegationActivity.this);
                    row.setId(1000 + i);
                    main.setId(2000 + i);
                    stack.setId(3000 + i);
                    viewTitle.setId(4000 + i);
                    viewTime.setId(5000 + i);
                    row.removeAllViews();
                    row.setBackgroundColor(getResources().getColor(R.color.primary200));
                    row.setPadding(5,10,5,10);
                    TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    trParams.setMargins(0,5,0,5);
                    row.setLayoutParams(trParams);

                    main.setOrientation(LinearLayout.HORIZONTAL);
                    stack.setOrientation(LinearLayout.VERTICAL);

                    String text = "";
                    if (i == 0) {
                        text = "Light strength exercise";
                    } else if (i == 1) {
                        text = "Vigorous strength exercise";
                    } else if (i == 2) {
                        text = "Walking";
                    } else if (i == 3) {
                        text = "Jogging";
                    } else if (i == 4) {
                        text = "Running";
                    }

                    viewTitle.setText(text);
                    viewTitle.setTextSize(20);
                    String time = minutes + " minutes, " + seconds + " seconds";
                    viewTime.setText(time);

//                    LayoutParams mainParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                            LayoutParams.WRAP_CONTENT);
//                    main.setLayoutParams(mainParams);
                    LayoutParams stackParams = new LinearLayout.LayoutParams(600,
                            LayoutParams.WRAP_CONTENT);
                    stack.setLayoutParams(stackParams);
                    stack.addView(viewTitle);
                    stack.addView(viewTime);
                    main.addView(stack);

                    if (i == 0) {
                        AddLightStrength.setHeight(20);
                        AddLightStrength.setWidth(20);
                        AddLightStrength.setId(6000+i);
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            AddLightStrength.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_press));
                        else
                            AddLightStrength.setBackground(getResources().getDrawable(R.drawable.add_button_press));
                        main.addView(AddLightStrength);
                    } else if (i == 1) {
                        AddVigorousStrength.setHeight(20);
                        AddVigorousStrength.setWidth(20);
                        AddVigorousStrength.setId(6000 + i);
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            AddVigorousStrength.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_press));
                        else
                            AddVigorousStrength.setBackground(getResources().getDrawable(R.drawable.add_button_press));
                        main.addView(AddVigorousStrength);
                    } else if (i == 2) {
                        AddWalking.setHeight(20);
                        AddWalking.setWidth(20);
                        AddWalking.setId(6000 + i);
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            AddWalking.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_press));
                        else
                            AddWalking.setBackground(getResources().getDrawable(R.drawable.add_button_press));
                        main.addView(AddWalking);
                    } else if (i == 3) {
                        AddJogging.setHeight(20);
                        AddJogging.setWidth(20);
                        AddJogging.setId(6000 + i);
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            AddJogging.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_press));
                        else
                            AddJogging.setBackground(getResources().getDrawable(R.drawable.add_button_press));
                        main.addView(AddJogging);
                    } else if (i == 4) {
                        AddRunning.setHeight(20);
                        AddRunning.setWidth(20);
                        AddRunning.setId(6000 + i);
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            AddRunning.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_press));
                        else
                            AddRunning.setBackground(getResources().getDrawable(R.drawable.add_button_press));
                        main.addView(AddRunning);
                    }

                    row.addView(main);
                    a.addView(row);
                }
            }
        });

        AddLightStrength.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create light strength workout item and store it in today's schedule

                StrengthWorkoutItem item = new StrengthWorkoutItem();
                item.setNumberOfReps(12);
                item.setNumberOfSets(4);
                item.setWeightUsed(10);
                item.setMETSVal(3);
                item.setName("lift_light");
                item.setTime(times[0]);
                Calendar cal = Calendar.getInstance();
                Date dat = cal.getTime();
                item.setDate(dat);

                DBHelper dbh = new DBHelper(CalorieNegationActivity.this);
                Schedule s = dbh.getCurrentSchedule();
                dbh.addWorkoutToSchedule(s,item, new Profile(CalorieNegationActivity.this));
                dbh.close();
                BackToHomeScreen(view);
            }
        });

        AddVigorousStrength.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create vigorous strength workout item and store it in today's schedule
                StrengthWorkoutItem item = new StrengthWorkoutItem();
                item.setNumberOfReps(20);
                item.setNumberOfSets(6);
                item.setWeightUsed(20);
                item.setMETSVal(7);
                item.setName("lift_vigor");
                item.setTime(times[1]);
                Calendar cal = Calendar.getInstance();
                Date dat = cal.getTime();
                item.setDate(dat);

                DBHelper dbh = new DBHelper(CalorieNegationActivity.this);
                Schedule s = dbh.getCurrentSchedule();
                dbh.addWorkoutToSchedule(s, item, new Profile(CalorieNegationActivity.this));
                dbh.close();
                BackToHomeScreen(view);
            }
        });

        AddWalking.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create walking workout item and store it in today's schedule
                CardioWorkoutItem item = new CardioWorkoutItem();
                item.setDistance(2);
                item.setMETSVal(11);
                item.setName("walk");
                item.setTime(times[2]);
                Calendar cal = Calendar.getInstance();
                Date dat = cal.getTime();
                item.setDate(dat);

                DBHelper dbh = new DBHelper(CalorieNegationActivity.this);
                Schedule s = dbh.getCurrentSchedule();
                dbh.addWorkoutToSchedule(s,item, new Profile(CalorieNegationActivity.this));
                dbh.close();
                BackToHomeScreen(view);
            }
        });

        AddJogging.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create jogging workout item and store it in today's schedule
                CardioWorkoutItem item = new CardioWorkoutItem();
                item.setDistance(2);
                item.setMETSVal(3.5);
                item.setName("jog");
                item.setTime(times[3]);
                Calendar cal = Calendar.getInstance();
                Date dat = cal.getTime();
                item.setDate(dat);

                DBHelper dbh = new DBHelper(CalorieNegationActivity.this);
                Schedule s = dbh.getCurrentSchedule();
                dbh.addWorkoutToSchedule(s,item, new Profile(CalorieNegationActivity.this));
                dbh.close();
                BackToHomeScreen(view);
            }
        });

        AddRunning.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create running workout item and store it in today's schedule
                CardioWorkoutItem item = new CardioWorkoutItem();
                item.setDistance(2);
                item.setMETSVal(6);
                item.setName("run");
                item.setTime(times[4]);
                Calendar cal = Calendar.getInstance();
                Date dat = cal.getTime();
                item.setDate(dat);

                DBHelper dbh = new DBHelper(CalorieNegationActivity.this);
                Schedule s = dbh.getCurrentSchedule();
                dbh.addWorkoutToSchedule(s,item, new Profile(CalorieNegationActivity.this));
                dbh.close();
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
