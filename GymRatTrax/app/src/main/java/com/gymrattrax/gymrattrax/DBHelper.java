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
        db.execSQL(DBContract.ExerciseTable.CREATE_TABLE);
        db.execSQL(DBContract.WorkoutTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.ProfileTable.DELETE_TABLE);
        db.execSQL(DBContract.WeightTable.DELETE_TABLE);
        db.execSQL(DBContract.ScheduleTable.DELETE_TABLE);
        db.execSQL(DBContract.ExerciseTable.DELETE_TABLE);
        db.execSQL(DBContract.WorkoutTable.DELETE_TABLE);
        onCreate(db);
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
        if (value.trim().isEmpty()) { //delete
            if (cursor.moveToFirst()) {
                String[] args = new String[1];
                args[0] = key;
                db.delete(DBContract.ProfileTable.TABLE_NAME,
                        DBContract.ProfileTable.COLUMN_NAME_KEY + " = ?", args);
            }
        }
        else {
            ContentValues values = new ContentValues();
            if (cursor.moveToFirst()) { //update
                String[] args = new String[1];
                args[0] = key;
                values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, value);
                db.update(DBContract.ProfileTable.TABLE_NAME, values,
                        DBContract.ProfileTable.COLUMN_NAME_KEY + "=?", args);
            }
            else { //insert
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

        Map<Date,Double> values = new HashMap<Date,Double>();
        while (cursor.moveToNext()) {
            Date d1 = new Date();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                        Locale.US);
                d1 = format.parse(cursor.getString(0));
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

    public void addWorkoutToSchedule(Schedule s, WorkoutItem w) {
        /*
        ID is a new autoincrement integer, SCHEDULE is s.getID(), and EXERCISE is w.getID(). All
        other values will come from other GET methods within variable w. Insert into table WORKOUT
        values with a RECORD_TYPE value of “P” for proposed.
         */
    }

    public boolean removeWorkoutItemFromSchedule(Schedule s, WorkoutItem w) {
        /*
        Select all WORKOUT records with SCHEDULE equal to s.getID() and EXERCISE equal to w.getID().
        If one exists (and no more than one should exist), change record type to R for Removed.
         */
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable.COLUMN_NAME_ID + " =  \"" + String.valueOf(w.getID()) +
                "\" AND " + DBContract.WorkoutTable.COLUMN_NAME_RECORD_TYPE + " =  \"" +
                DBContract.WorkoutTable.VAL_PROPOSED + "\"";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String[] args = new String[1];
            args[0] = cursor.getString(0);

            db.delete(DBContract.WorkoutTable.TABLE_NAME,
                    DBContract.WorkoutTable.COLUMN_NAME_ID + "=?", args);
        }
        cursor.close();
        db.close();
        return result;
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
        WorkoutItem[] workouts;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startStr = dateFormat.format(start) + " 00:00:00.000";
        String endStr = dateFormat.format(end) + " 23:59:59.999";

        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable.COLUMN_NAME_RECORD_TYPE + " =  \"" +
                DBContract.WorkoutTable.VAL_PROPOSED + "\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME + " >=  \"" + startStr + "\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME + " <=  \"" + endStr + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        workouts = new WorkoutItem[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {

            //Pull in values from exercise table.
            String subQuery = "SELECT * FROM " + DBContract.ExerciseTable.TABLE_NAME + " WHERE " +
                    DBContract.ExerciseTable.COLUMN_NAME_NAME + " = \"" + cursor.getString(3) +
                    "\"";
            Cursor subCursor = db.rawQuery(subQuery, null);
            String value = null;
            if (subCursor.moveToFirst()) {
                switch (subCursor.getString(1)) {
                    case "C":
                        workouts[i] = new CardioWorkoutItem();
                        workouts[i].setMETSVal(subCursor.getDouble(2));
                        break;
                    case "S":
                        workouts[i] = new StrengthWorkoutItem();
                        workouts[i].setType(WorkoutType.STRENGTH);
                        break;
                }
                workouts[i].setMETSVal(subCursor.getDouble(2));
            }
            subCursor.close();

            //set rest of values
            workouts[i].setID(Integer.parseInt(cursor.getString(0)));
            //workouts[i].setSchedule(cursor.getInteger(2));
            workouts[i].setName(cursor.getString(3));
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
                Date date = format.parse(cursor.getString(4));
                workouts[i].setDate(date);
            } catch (ParseException e) {
                Calendar cal = new GregorianCalendar();
                workouts[i].setDate(cal.getTime());
            }
            workouts[i].setCaloriesBurned(Double.parseDouble(cursor.getString(5)));
            i++;
        }
        cursor.close();
        db.close();

        return workouts;
    }

    public void completeWorkout(Schedule s, WorkoutItem w) {
        /*
        Send a database update operation, “insert into WORKOUT values (w.getId(), ...)” for all
        necessary fields and with RECORD_TYPE value “C” for completed.
         */
        SQLiteDatabase db = this.getWritableDatabase();
//        if (value.trim().isEmpty()) {
//            String query = "SELECT * FROM " + DBContract.ProfileTable.TABLE_NAME + " WHERE " +
//                    DBContract.ProfileTable.COLUMN_NAME_KEY + " =  \"" + key + "\"";
//
//            Cursor cursor = db.rawQuery(query, null);
//
//            if (cursor.moveToFirst()) {
//                String[] args = new String[1];
//                args[0] = key;
//                db.delete(DBContract.ProfileTable.TABLE_NAME,
//                        DBContract.ProfileTable.COLUMN_NAME_KEY + " = ?", args);
//            }
//            cursor.close();
//        }
//        else {
        ContentValues values = new ContentValues();
        values.put(DBContract.WorkoutTable.COLUMN_NAME_ID, String.valueOf(w.getID()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_RECORD_TYPE,
                DBContract.WorkoutTable.VAL_COMPLETE);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, String.valueOf(s.getID()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE,
                String.valueOf(w.getName()));

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String dateStr = dateFormat.format(date);

        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, dateStr);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES,
                String.valueOf(w.getCaloriesBurned()));

        String parameters = null;
        switch (w.getType()) {
            case CARDIO:
                parameters = String.valueOf(((CardioWorkoutItem)w).getDistance());
                break;
            case STRENGTH:
                parameters = String.valueOf(((StrengthWorkoutItem)w).getCompletedReps()) + ":" +
                        String.valueOf(((StrengthWorkoutItem)w).getCompletedSets()) + ":" +
                        String.valueOf(((StrengthWorkoutItem)w).getWeightUsed());
                break;
        }
        values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS, parameters);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_SPENT, String.valueOf(w.getTime()));

        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);
//        }
        db.close();
    }

    public double getMETSVal(String name) {
        String query = "SELECT " + DBContract.ExerciseTable.COLUMN_NAME_METS + " FROM " +
                DBContract.ExerciseTable.TABLE_NAME + " WHERE " +
                DBContract.ExerciseTable.COLUMN_NAME_NAME + " =  \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double METSVal = -1;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            METSVal = cursor.getDouble(0);
        } //else, write to an error log
        cursor.close();
        db.close();
        return METSVal;
    }

    private String now() {
        Calendar cal = new GregorianCalendar();
        Date dat = cal.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        String str = sdf.format(dat) + " 00:00:00.000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String str = sdf.format(dat);
        return str;
    }
}