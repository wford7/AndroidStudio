package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
// import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class HomeScreen extends ActionBarActivity {

    private Button BeginWorkoutButton, ViewScheduleButton, ViewProfileButton, ViewProgressButton, CalorieNegationButton, EditSettingsButton;
    private ImageView GymRat;
    private ScrollView updateLayoutScroll;
    private GraphView graph, graph2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initiate tutorial/profile creation if there is no Profile ID in database
        Profile create = new Profile();
        setContentView(R.layout.activity_home_screen);
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);


        GymRat = (ImageView)findViewById(R.id.home_rat);
        BeginWorkoutButton = (Button)findViewById(R.id.BeginWorkoutButton);
        ViewScheduleButton = (Button)findViewById(R.id.ViewScheduleButton);
        ViewProfileButton = (Button)findViewById(R.id.ViewProfileButton);
        ViewProgressButton = (Button)findViewById(R.id.ViewProgressButton);
        CalorieNegationButton = (Button)findViewById(R.id.CalorieNegationButton);
        EditSettingsButton = (Button)findViewById(R.id.EditSettingsButton);

        graph = (GraphView)findViewById(R.id.graph);
        graph2 = (GraphView)findViewById(R.id.graph2);

        GymRat.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
            }});

        BeginWorkoutButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
                graph.addSeries(series);

                LineGraphSeries<DataPoint> seriesUno = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 3),
                        new DataPoint(2, 2),
                        new DataPoint(5, 2)
                });
                graph2.addSeries(seriesUno);

//                loadCurrentSchedule(view);
//                LinearLayout newTest = (LinearLayout)findViewById(R.id.LayoutScroll);
//                TextView newText = new TextView(HomeScreen.this);
//                    newText.setText("Dynamic Add Text Test");
////                updateLayoutScroll = (ScrollView)findViewById(R.id.scrollView);
//
//                    newTest.addView(newText);


//                updateLayoutScroll.addView(newTest);
            }
        });

//        ViewScheduleButton.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//        public void onClick(View view){
//                view.startAnimation(animAlpha);
//                loadScheduleEdit(view);
//            }
//        });

        ViewProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadFitnessProfile(view);
            }
        });


        ViewProgressButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadProgress(view);
            }
        });

        CalorieNegationButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadCalorieNegation(view);
            }
        });

        EditSettingsButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
//                loadSettings(view);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }


    //the following method is triggered when user selects "Begin Workout" button from main page
    //if no workout is schedule, display message instructing user to "Create New Plan"
    public void loadCurrentSchedule(View view){

        //load current workout schedule for current date

        Intent intent = new Intent (HomeScreen.this, DailyWorkoutActivity.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Calorie Negation" button from the main page
    public void loadCalorieNegation(View view){

        Intent intent = new Intent (HomeScreen.this, CalorieNegationActivity.class);
        startActivity(intent);
    }

    //the following method is triggered when user selects "Fitness Profile" button from the main page
    final public void loadFitnessProfile(View view){

        Intent intent = new Intent (HomeScreen.this, ProfileActivity.class);
        startActivity(intent);
    }


    public void loadSettings(View view){

        Intent intent = new Intent (HomeScreen.this, SettingsActivity.class);
        startActivity(intent);
    }


    //this method is triggered when user selects "View Progress" button from the main page
    public void loadProgress(View view){

        Intent intent = new Intent (HomeScreen.this, ProgressActivity.class);
        startActivity(intent);
    }
}
