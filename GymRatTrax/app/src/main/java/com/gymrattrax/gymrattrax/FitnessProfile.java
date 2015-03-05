package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;


public class FitnessProfile extends ActionBarActivity {

    private Button SaveProfileButton;
    private Button EditProfileButton;
    private EditText nameEditText;
    private EditText birthDateEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText fatPercentageEditText;
    //private Spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_profile);

        //database query and then set editTexts to display the appropriate data

        SaveProfileButton = (Button)findViewById(R.id.SaveProfileButton);
        EditProfileButton = (Button)findViewById(R.id.EditProfileButton);

        nameEditText = (EditText)findViewById(R.id.profile_name);
        birthDateEditText = (EditText)findViewById(R.id.birth_date);
        weightEditText = (EditText)findViewById(R.id.profile_weight);
        heightEditText = (EditText)findViewById(R.id.profile_height);
        fatPercentageEditText = (EditText)findViewById(R.id.fat_percentage);

        // make edit text unclickable until edit button is clicked
        nameEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        nameEditText.setEnabled(false);
        nameEditText.setClickable(false);

        birthDateEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        birthDateEditText.setEnabled(false);
        birthDateEditText.setClickable(false);

        weightEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        weightEditText.setEnabled(false);
        weightEditText.setClickable(false);

        heightEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        heightEditText.setEnabled(false);
        heightEditText.setClickable(false);

        fatPercentageEditText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        fatPercentageEditText.setEnabled(false);
        fatPercentageEditText.setClickable(false);




        SaveProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveChanges(view);
            }
        });

        EditProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                nameEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                nameEditText.setEnabled(true);
                nameEditText.setClickable(true);

                birthDateEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                birthDateEditText.setEnabled(true);
                birthDateEditText.setClickable(true);

                weightEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                weightEditText.setEnabled(true);
                weightEditText.setClickable(true);

                heightEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                heightEditText.setEnabled(true);
                heightEditText.setClickable(true);

                fatPercentageEditText.setBackgroundColor(getResources().getColor(android.R.color.white));
                fatPercentageEditText.setEnabled(true);
                fatPercentageEditText.setClickable(true);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fitness_profile, menu);
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

    public void saveChanges(View view){
        // update database profile
    }

    public void editProfile(View view){
        // unlock EditText fields and spinner
    }

    /**
     * Handle radio button clicks
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.little_exercise:
                if (checked)
                    // ActivityLevel = 1.2
                    break;
            case R.id.light_exercise:
                if (checked)
                    // ActivityLevel = 1.375
                    break;
            case R.id.mod_exercise:
                if (checked)
                    // ActivityLevel = 1.55
                    break;
            case R.id.heavy_exercise:
                if (checked)
                    // ActivityLevel = 1.725
                    break;
        }
    }
}
