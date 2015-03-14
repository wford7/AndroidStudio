package com.gymrattrax.gymrattrax;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.ProfileTable.CREATE_TABLE);
        db.execSQL(DBContract.WeightTable.CREATE_TABLE);
        db.execSQL(DBContract.ScheduleTable.CREATE_TABLE);
        db.execSQL(DBContract.WorkoutTable.CREATE_TABLE);

        ContentValues values = new ContentValues();
        values.put(DBContract.ProfileTable.COLUMN_NAME_KEY,
                DBContract.ProfileTable.KEY_DATE_FORMAT);
        values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, "MM/dd/yyyy");
        db.insert(DBContract.ProfileTable.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL(DBContract.ProfileTable.DELETE_TABLE);
                    db.execSQL(DBContract.WeightTable.DELETE_TABLE);
                    db.execSQL(DBContract.ScheduleTable.DELETE_TABLE);
                    db.execSQL(DBContract.WorkoutTable.DELETE_TABLE);
                    onCreate(db);
            }
        }
    }

    public String getProfileInfo(String key) {
        String query = "SELECT * FROM " + DBContract.ProfileTable.TABLE_NAME + " WHERE " +
                DBContract.ProfileTable.COLUMN_NAME_KEY + " =  \"" + key + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String value = "";
        if (cursor.moveToFirst())
            value = cursor.getString(1);
        cursor.close();
        db.close();
        return value;
    }

    public void setProfileInfo(String key, String value) {
        /*
        To delete profile info, insert a blank string for the value.
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DBContract.ProfileTable.TABLE_NAME + " WHERE " +
                DBContract.ProfileTable.COLUMN_NAME_KEY + " =  \"" + key + "\"";
        Cursor cursor = db.rawQuery(query, null);
        //If the value is empty, we delete the whole record.
        if (value.trim().isEmpty()) {
            //Some settings values may be blocked from being deleted.
            if (!key.equals(DBContract.ProfileTable.KEY_DATE_FORMAT)) {
                if (cursor.moveToFirst()) {
                    String[] args = new String[1];
                    args[0] = key;
                    db.delete(DBContract.ProfileTable.TABLE_NAME,
                            DBContract.ProfileTable.COLUMN_NAME_KEY + " = ?", args);
                }
            }
        }
        else {
            //If we have both a key and a value, then we check to see if the key already exists.
            ContentValues values = new ContentValues();
            //If the key DOES already exist, then we update the existing record.
            if (cursor.moveToFirst()) {
                //If the key already contains the same value, skip the update operation.
                if (!value.equals(cursor.getString(1))) {
                    String[] args = new String[1];
                    args[0] = key;
                    values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, value);
                    db.update(DBContract.ProfileTable.TABLE_NAME, values,
                            DBContract.ProfileTable.COLUMN_NAME_KEY + "=?", args);
                }
            }
            //If the key does not exist, then an entirely new record must be created.
            else {
                values.put(DBContract.ProfileTable.COLUMN_NAME_KEY, key);
                values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, value);

                db.insert(DBContract.ProfileTable.TABLE_NAME, null, values);
            }
        }
        cursor.close();
        db.close();
    }

    public double[] getLatestWeight() {
        String query = "SELECT * FROM " + DBContract.WeightTable.TABLE_NAME + " ORDER BY " +
                DBContract.WeightTable.COLUMN_NAME_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        double[] values = new double[3];
        if (cursor.moveToFirst()) {
            values[0] = cursor.getDouble(1);
            if (cursor.isNull(2))
                values[1] = -1;
            else
                values[1] = cursor.getDouble(2);
            values[2] = cursor.getDouble(3);
        }
        cursor.close();
        db.close();
        return values;
    }

    public Map<Date,Double> getWeights(Date from,Date to) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String fromStr = dateFormat.format(from) + " 00:00:00.000";
        String toStr = dateFormat.format(to) + " 23:59:59.999";

        String query = "SELECT * FROM " + DBContract.WeightTable.TABLE_NAME + " WHERE " +
                DBContract.WeightTable.COLUMN_NAME_DATE + " >=  \"" + fromStr + "\" AND " +
                DBContract.WeightTable.COLUMN_NAME_DATE + " <=  \"" + toStr + "\" ORDER BY " +
                DBContract.WeightTable.COLUMN_NAME_DATE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Map<Date,Double> values = new HashMap<>();
        while (cursor.moveToNext()) {
            Date d1;
            try {
                d1 = convertDate(cursor.getString(0));
            } catch (ParseException e) {
                Calendar cal = new GregorianCalendar();
                d1 = cal.getTime();
            }
            values.put(d1, cursor.getDouble(1));
        }
        cursor.close();
        db.close();
        return values;
    }

    public void setWeight(double weight, double bodyFat, double activityLevel) {
        /*
        bodyFat is optional. To make bodyFat NULL, pass in a non-positive value.
         */
        double[] old = getLatestWeight();
        if (weight != old[0] || bodyFat != old[1] || activityLevel != old[2]) {
            String timestamp = now();
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBContract.WeightTable.COLUMN_NAME_DATE, timestamp);
            values.put(DBContract.WeightTable.COLUMN_NAME_WEIGHT, weight);
            if (bodyFat > 0)
                values.put(DBContract.WeightTable.COLUMN_NAME_BODY_FAT_PERCENTAGE, bodyFat);
            else
                values.putNull(DBContract.WeightTable.COLUMN_NAME_BODY_FAT_PERCENTAGE);
            values.put(DBContract.WeightTable.COLUMN_NAME_ACTIVITY_LEVEL, activityLevel);

            db.insert(DBContract.WeightTable.TABLE_NAME, null, values);

            db.close();
        }
    }

    public void addWorkoutToSchedule(Schedule s, WorkoutItem w, Profile p) {
        /*
        ID is a new autoincrement integer, SCHEDULE is s.getID(), and EXERCISE is w.getID(). All
        other values will come from other GET methods within variable w. Insert into table WORKOUT
        values with a RECORD_TYPE value of “P” for proposed.
        */
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, s.getID());
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE, w.getName().toString());
        values.put(DBContract.WorkoutTable.COLUMN_NAME_RECORD_NO, 0);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, convertDate(w.getDate()));
//        double BMR = p.getBMR();
//        if (BMR <= 0)
//            values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES, 0);
//        else {
//            values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES,
//                    (int)(w.getTime() * w.getMETSVal() * BMR) / (60*24));
//        }
        switch (w.getType()) {
            case CARDIO:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_CAR_DISTANCE,
                        ((CardioWorkoutItem)w).getDistance());
                break;
            case STRENGTH:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_REPS,
                        ((StrengthWorkoutItem)w).getCompletedReps());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_SETS,
                        ((StrengthWorkoutItem)w).getCompletedSets());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_WEIGHT,
                        ((StrengthWorkoutItem)w).getWeightUsed());
                break;
        }
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_DURATION, w.getTime());

        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        db.close();
    }

    public void removeWorkoutItemFromSchedule(WorkoutItem w) {
        /*
        Select all WORKOUT records with SCHEDULE equal to s.getID() and EXERCISE equal to w.getID().
        If one exists (and no more than one should exist), change record type to R for Removed.
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable._ID + " =  \"" + String.valueOf(w.getID()) +
                "\" AND " + DBContract.WorkoutTable.COLUMN_NAME_RECORD_NO + " =  \"0\"";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String[] args = new String[1];
            args[0] = cursor.getString(0);

            db.delete(DBContract.WorkoutTable.TABLE_NAME,
                    DBContract.WorkoutTable._ID + "=?", args);
        }
        cursor.close();
        db.close();
    }

    public WorkoutItem[] getWorkoutsForToday() {
        Calendar cal = new GregorianCalendar();
        return getWorkoutsInRange(cal.getTime(),cal.getTime());
    }

    public WorkoutItem[] getWorkoutsInRange(Date start, Date end) {
        /*
        Convert the Date values into string matching the format, “yyyy-MM-dd HH:mm:ss.SSS,” but set
        “HH:mm:ss.SSS” to “00:00:00.000” for variable fromStr and “11:59:59.999” for variable
        endStr. Perform a database query operation, “select * from WORKOUT where DATE >= fromStr
        and DATE <= endStr.” Take the output values and assign them to WorkoutItem values.
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startStr = dateFormat.format(start) + " 00:00:00.000";
        String endStr = dateFormat.format(end) + " 23:59:59.999";

        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable.COLUMN_NAME_RECORD_NO + " =  \"0\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME + " >=  \"" + startStr + "\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME + " <=  \"" + endStr + "\"";

        return storeWorkouts(query);
    }

    public WorkoutItem[] getWorkoutsFromSchedule(Schedule s) {
        /*
        Convert the Date values into string matching the format, “yyyy-MM-dd HH:mm:ss.SSS,” but set
        “HH:mm:ss.SSS” to “00:00:00.000” for variable fromStr and “11:59:59.999” for variable
        endStr. Perform a database query operation, “select * from WORKOUT where DATE >= fromStr
        and DATE <= endStr.” Take the output values and assign them to WorkoutItem values.
         */
        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable.COLUMN_NAME_RECORD_NO + " =  \"0\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE + " >=  \"" + s.getID() + "\"";

        return storeWorkouts(query);
    }

    public void completeWorkout(Schedule s, WorkoutItem w) {
        /*
        Send a database update operation, “insert into WORKOUT values (w.getId(), ...)” for all
        necessary fields and with RECORD_TYPE value “C” for completed.
         */
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.WorkoutTable._ID, String.valueOf(w.getID()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_RECORD_NO, 1); //needs to increase if additional workouts complete
        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, String.valueOf(s.getID()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE,
                String.valueOf(w.getName()));

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String dateStr = convertDate(date);

        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, dateStr);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES,
                String.valueOf(w.getCaloriesBurned()));

        switch (w.getType()) {
            case CARDIO:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_CAR_DISTANCE,
                        ((CardioWorkoutItem)w).getDistance());
                break;
            case STRENGTH:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_REPS,
                        ((StrengthWorkoutItem)w).getCompletedReps());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_SETS,
                        ((StrengthWorkoutItem)w).getCompletedSets());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STR_WEIGHT,
                        ((StrengthWorkoutItem)w).getWeightUsed());
                break;
        }
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_DURATION, String.valueOf(w.getTime()));

        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        db.close();
    }

    public Schedule getCurrentSchedule() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String dateStr = convertDate(date);

        String query = "SELECT * FROM " + DBContract.ScheduleTable.TABLE_NAME + " WHERE " +
                DBContract.ScheduleTable.COLUMN_NAME_START_DATE + " <=  \"" + dateStr + "\"" +
                " ORDER BY " + DBContract.ScheduleTable.COLUMN_NAME_START_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Schedule s = new Schedule();
        if (cursor.moveToFirst()) {
            s.setID(cursor.getInt(cursor.getColumnIndex(
                    DBContract.ScheduleTable._ID)));
            s.setLengthInDays(cursor.getInt(cursor.getColumnIndex(
                    DBContract.ScheduleTable.COLUMN_NAME_LENGTH_IN_DAYS)));
            try {
                s.setStartDay(convertDate(cursor.getString(cursor.getColumnIndex(
                        DBContract.ScheduleTable.COLUMN_NAME_START_DATE))));
            } catch (ParseException e) {
                s.setStartDay(date);
            }
        }
        cursor.close();
        db.close();
        return s;
    }

    public Date convertDate(String d) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        return sdf.parse(d);
    }
    public String convertDate(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        return sdf.format(d);
    }
    public String displayDate(Date d) {
        String dateFormat = getProfileInfo(DBContract.ProfileTable.KEY_DATE_FORMAT);
        if (dateFormat.trim().isEmpty())
            dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        return sdf.format(d);
    }
    public String displayDateTime(Date d) {
        String dateFormat = getProfileInfo(DBContract.ProfileTable.KEY_DATE_FORMAT);
        if (dateFormat.trim().isEmpty())
            dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " hh:mm a", Locale.US);
        return sdf.format(d);
    }

    public String now() {
        Calendar cal = new GregorianCalendar();
        Date dat = cal.getTime();
        return convertDate(dat);
    }

    String[][] secretDebugDeleteFromFinalReleaseRawQuery(String table) {
        switch (table) {
            case "Profile":
                table = DBContract.ProfileTable.TABLE_NAME;
                break;
            case "Weight":
                table = DBContract.WeightTable.TABLE_NAME;
                break;
            case "Schedule":
                table = DBContract.ScheduleTable.TABLE_NAME;
                break;
            case "Workout":
                table = DBContract.WorkoutTable.TABLE_NAME;
                break;
        }

        String query = "SELECT * FROM " + table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String[][] value = new String[cursor.getCount() + 1][cursor.getColumnCount()];
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            value[0][i] = "[ " + cursor.getColumnName(i) + " ]";
        }
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++)
                value[cursor.getPosition() + 1][i] = cursor.getString(i);
        }
        cursor.close();
        db.close();
        return value;
    }

    private WorkoutItem[] storeWorkouts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        WorkoutItem[] workouts = new WorkoutItem[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            //Pull in values from exercise table.
//            String subQuery = "SELECT * FROM " + DBContract.ExerciseTable.TABLE_NAME + " WHERE " +
//                    DBContract.ExerciseTable.COLUMN_NAME_NAME + " = \"" + cursor.getString(3) +
//                    "\"";
//            Cursor subCursor = db.rawQuery(subQuery, null);
//            if (subCursor.moveToFirst()) {
//            }
//            subCursor.close();

            if (cursor.isNull(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_CAR_DISTANCE))) { //strength
                workouts[i] = new StrengthWorkoutItem();
                ((StrengthWorkoutItem) workouts[i]).setNumberOfReps(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STR_REPS)));
                ((StrengthWorkoutItem) workouts[i]).setNumberOfSets(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STR_SETS)));
                ((StrengthWorkoutItem) workouts[i]).setWeightUsed(
                        cursor.getDouble(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STR_WEIGHT)));
            }
            else { //cardio
                workouts[i] = new CardioWorkoutItem();
                ((CardioWorkoutItem) workouts[i]).setDistance(
                        cursor.getDouble(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_CAR_DISTANCE)));
            }

            //id
            workouts[i].setID(cursor.getInt(cursor.getColumnIndex(DBContract.WorkoutTable._ID)));

            //schedule (not applicable)
            //exercise
            workouts[i].setName(ExerciseName.fromString(cursor.getString(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_EXERCISE))));

            //date
            try {
                workouts[i].setDate(convertDate(cursor.getString(cursor.getColumnIndex(
                        DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME))));
            } catch (ParseException e) {
                Calendar cal = Calendar.getInstance();
                workouts[i].setDate(cal.getTime());
            }

            //calories "burned"
//            workouts[i].setCaloriesBurned(Double.parseDouble(cursor.getString(cursor.getColumnIndex(
//                    DBContract.WorkoutTable.COLUMN_NAME_CALORIES))));

            //time
            workouts[i].setTime(cursor.getDouble(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_TIME_DURATION)));

            i++;
        }
        cursor.close();
        db.close();

        return workouts;
    }
}