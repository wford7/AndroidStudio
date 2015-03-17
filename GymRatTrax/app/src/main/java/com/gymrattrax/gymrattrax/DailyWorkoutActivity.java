package com.gymrattrax.gymrattrax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class DailyWorkoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_workout);


        displayCurrentWorkouts();
    }

    //add scrollview to xml file
    //activity will be dynamically built based on database query that will return all

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void displayCurrentWorkouts() {
        LinearLayout linearContainer = (LinearLayout) findViewById(R.id.populate_scroll);
        TextView title = (TextView) findViewById(R.id.title_main);

        linearContainer.removeAllViewsInLayout();
        TableLayout a = new TableLayout(DailyWorkoutActivity.this);
        a.removeAllViews();

        DBHelper dbh = new DBHelper(this);
        WorkoutItem[] workouts = dbh.getWorkoutsForToday();
        //Linear
        linearContainer.addView(a);

        int i = 0;
        for (final WorkoutItem w : workouts) {
            TableRow row = new TableRow(DailyWorkoutActivity.this);
            LinearLayout main = new LinearLayout(DailyWorkoutActivity.this);
            LinearLayout stack = new LinearLayout(DailyWorkoutActivity.this);
            TextView viewTitle = new TextView(DailyWorkoutActivity.this);
            TextView viewTime = new TextView(DailyWorkoutActivity.this);
            row.setId(1000 + i);
            main.setId(2000 + i);
            stack.setId(3000 + i);
            viewTitle.setId(4000 + i);
            viewTime.setId(5000 + i);
            row.removeAllViews();
            row.setBackgroundColor(getResources().getColor(R.color.primary200));
            row.setPadding(5,10,5,10);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(0,5,0,5);
            row.setLayoutParams(trParams);

            main.setOrientation(LinearLayout.HORIZONTAL);
            stack.setOrientation(LinearLayout.VERTICAL);

            viewTitle.setText(w.getName().toString());
            viewTitle.setTextSize(20);

            double minutesDbl = w.getTimeScheduled();
            int secondsTotal = (int) (minutesDbl * 60);
            int seconds = secondsTotal % 60;
            int minutes = (secondsTotal - seconds) / 60;
            String time = minutes + " minutes, " + seconds + " seconds";
            time = dbh.displayDateTime(w.getDateScheduled()) + ": " + time;
            viewTime.setText(time);

            ViewGroup.LayoutParams stackParams = new LinearLayout.LayoutParams(600,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            stack.setLayoutParams(stackParams);
            stack.addView(viewTitle);
            stack.addView(viewTime);
            main.addView(stack);

            row.addView(main);
            a.addView(row);


            //only clickable if it has not been completed
            row.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v){

                    if (w.getType().toString() == "STRENGTH"){
                        //get parameters for strength workout
                        startStrengthWorkoutActivity();
                    }
                    else if (w.getType().toString() == "CARDIO"){
                        startCardioWorkoutActivity();
                    }
                }
            });

            title.setText("Click A Workout Item To Begin!");
            i++;
        }
        dbh.close();
    }

    private void startCardioWorkoutActivity() {
        Intent intent = new Intent(DailyWorkoutActivity.this, CardioWorkoutActivity.class);
        // create bundle from parameters
        startActivity(intent);
    }

    private void startStrengthWorkoutActivity(){
        Intent intent = new Intent(DailyWorkoutActivity.this, StrengthWorkoutActivity.class);
        // create bundle from parameters
        startActivity(intent);
    }
}
