package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;


public class CardioWorkoutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_workout);

        TextView title = (TextView) findViewById(R.id.cardio_title);
        TextView goalTime = (TextView) findViewById(R.id.scheduled_time);

        Bundle b = getIntent().getExtras();
        String name = b.getString("name");
        double distance = b.getDouble("distance");
        double time = b.getDouble("time");

        title.setText(name);

        double minutesDbl = time;
        int secondsTotal = (int) (minutesDbl * 60);
        int seconds = secondsTotal % 60;
        int minutes = (secondsTotal - seconds) / 60;
        String fmtTime = minutes + ":" + seconds;
        goalTime.setText("Target Time: " + fmtTime);


        //implement chronometer and click listener


        //display scheduled/goal time

        //display radio buttons for "How You felt" after completing workout activity.  These radio buttons
        //will only be active after user clicks on COMPLETE
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardio_workout, menu);
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
