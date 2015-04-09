package com.gymrattrax.gymrattrax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

//TODO: Rename to DatabaseDebug and put in com.gymrattrax.scheduler.activities
public class DBDebug extends ActionBarActivity {
    private Spinner tableSpinner;
    private TableLayout tableTable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        tableSpinner = (Spinner)findViewById(R.id.debug_spinner);
        tableTable = (TableLayout)findViewById(R.id.table_scroll);

        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input = tableSpinner.getItemAtPosition(position).toString();
                tableTable.removeAllViewsInLayout();

                DBHelper dbh = new DBHelper(DBDebug.this);
                String[][] dbValues = dbh.debugRawQuery(input);
                dbh.close();

                for (int i = 0; i < dbValues.length; i++) {
                    TableRow tr = new TableRow(DBDebug.this);
                    tr.setId(1000 + i);
                    for (int j = 0; j < dbValues[0].length; j++) {
                        TextView tv = new TextView(DBDebug.this);
                        tv.setId(((j+2) * 1000) + i);
                        tv.setText(dbValues[i][j]);
                        tv.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tr.addView(tv);
                    }
                    tableTable.addView(tr);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    public void onNotificationButtonClick(View v){
        NotifyReceiver.cancelNotifications(this);
        DBHelper dbh = new DBHelper(this);
        WorkoutItem w = dbh.getWorkoutById(1);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 2);
        w.setDateScheduled(c.getTime());
        dbh.addWorkout(w);
        NotifyReceiver.setNotifications(this);
    }
}