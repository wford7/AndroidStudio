package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class FitnessProfile extends ActionBarActivity {

    private Button SaveProfileButton;
    private Button EditProfileButton;
    private EditText NameEditText;
    private EditText BirthDateEditText;
    //private Spinner

    //database query and then set editTexts to display the appropriate data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_profile);

        SaveProfileButton = (Button)findViewById(R.id.SaveProfileButton);
        EditProfileButton = (Button)findViewById(R.id.EditProfileButton);

        NameEditText = (EditText)findViewById(R.id.NameEditText);

        SaveProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveChanges(view);
            }
        });

        EditProfileButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                editProfile(view);
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
}
