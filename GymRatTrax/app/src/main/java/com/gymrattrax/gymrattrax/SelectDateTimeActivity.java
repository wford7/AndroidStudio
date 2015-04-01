package com.gymrattrax.gymrattrax;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.getActivity;


public class SelectDateTimeActivity extends ActionBarActivity {
    private CalendarView calendar;
    private String dateSelected, date;
    private int monthSelected, daySelected, yearSelected;
    private TextView timeText, dateText;
    private TimePicker timePicker;
    private Button doneButton, setTimeButton;


    private int selectedHour = 0, selectedMinutes = 0;

    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            selectedHour = hourOfDay;
            selectedMinutes = minute;
            updateTimeUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);
        calendar = (CalendarView) findViewById(R.id.calendar);
        Calendar cal = Calendar.getInstance();

        this.monthSelected = cal.get(Calendar.MONTH);
        this.daySelected = cal.get(Calendar.DAY_OF_MONTH);
        this.yearSelected = cal.get(Calendar.YEAR);
        this.selectedHour = cal.get(Calendar.HOUR_OF_DAY);
        this.selectedMinutes = cal.get(Calendar.MINUTE);

        int newMonth = monthSelected + 1;
        String dateString = ("" + newMonth + "/" + daySelected + "/" + yearSelected);

        dateText = (TextView) findViewById(R.id.date_text);
        timeText = (TextView) findViewById(R.id.TimeSelected);

        updateDateUI(dateString);

        initializeCalendar(calendar);

        updateTimeUI();

        setTimeButton = (Button) findViewById(R.id.setTimeButton);
        setTimeButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {

                showTimepickerDialog(selectedHour, selectedMinutes, false, mTimeListener);
            }
        });
        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context ctx = getApplicationContext();
//                CalendarService.addEvent(ctx, "GymRatTrax", "Cardio Workout", "");

                loadHomeScreen();
            }
        });
    }

    private void updateTimeUI() {
        String am = "AM";
        String pm = "PM";

        if (selectedHour > 12) {

            selectedHour -= 12;
            String hour = (selectedHour > 9) ? "" + selectedHour : "" + selectedHour;
            String minutes = (selectedMinutes > 9) ? "" + selectedMinutes : "" + selectedMinutes;
            timeText.setText(hour + ":" + minutes + " " + pm);
        }
        else {
            String hour = (selectedHour > 9) ? "" + selectedHour : "0" + selectedHour;
            String minutes = (selectedMinutes > 9) ? "" + selectedMinutes : "0" + selectedMinutes;
            timeText.setText(hour + ":" + minutes + " " + am);
        }
    }

    //  sets calendar details and returns date selected from calendar in String
    private void initializeCalendar(CalendarView calendar) {
        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(1);
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.primary200));
        calendar.setSelectedDateVerticalBar(R.color.primary700);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month += 1;
                dateSelected = "" + month + "/" + day + "/" + year;
                updateDateUI(dateSelected);
            }
        });
    }

    private void updateDateUI(String d) {
        dateText = (TextView) findViewById(R.id.date_text);
        dateText.setText(d);
    }

    private TimePickerDialog showTimepickerDialog(int initHour, int initMinutes, boolean is24Hour, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog dialog = new TimePickerDialog(this, listener, initHour, initMinutes, is24Hour);
        dialog.show();
        return dialog;
    }

    private void loadHomeScreen() {
        Intent intent = new Intent(SelectDateTimeActivity.this, HomeScreen.class);
        startActivity(intent);
    }

//    public void addThisWorkout(WorkoutItem w) {
//        DBHelper dbh = new DBHelper(SelectDateTimeActivity.this);
//        dbh.addWorkout(w);
//        dbh.close();
//        Calendar time = Calendar.getInstance();
//        String s = "" + date + " " + time;
//        time.setTime();
//        time.add(Calendar.SECOND, 10);
//    }
}
