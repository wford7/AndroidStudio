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

    /**
     * Creates the database file if it does not currently exist.
     * @param db The database file to create.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.ProfileTable.CREATE_TABLE);
        db.execSQL(DBContract.WeightTable.CREATE_TABLE);
        db.execSQL(DBContract.WorkoutTable.CREATE_TABLE);

        ContentValues values = new ContentValues();
        values.put(DBContract.ProfileTable.COLUMN_NAME_KEY,
                DBContract.ProfileTable.KEY_DATE_FORMAT);
        values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, "MM/dd/yyyy");
        db.insert(DBContract.ProfileTable.TABLE_NAME, null, values);
    }

    /**
     * Upgrades the database file when the reported version is higher than the stored version.
     * @param db The database file to update.
     * @param oldVersion The database version currently stored on the device.
     * @param newVersion The latest database version to which the device's database file will
     *                   upgrade.
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL(DBContract.ProfileTable.DELETE_TABLE);
                    db.execSQL(DBContract.WeightTable.DELETE_TABLE);
                    db.execSQL(DBContract.WorkoutTable.DELETE_TABLE);
                    onCreate(db);
            }
        }
    }

    /**
     * Returns the stored value associated with a given key in the Profile table.
     * @param key A String variable of a key in the Profile table. Use
     *            DBContract.ProfileTable.KEY_<...>.
     * @return The value in the database that corresponds to the provided key String. If the
     * provided key is not available in the Profile table, an empty String is returned.
     */
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

    /**
     * Sets the value associated with a given key in the Profile table.
     * @param key A String variable of a key in the Profile table. Use
     *            DBContract.ProfileTable.KEY_<...>.
     * @param value The String value to replace the current value for the provided key. If the key
     *              is not currently in the table, the record will be inserted. If the key does
     *              exist but contains different data, the record will be updated. If the key does
     *              exist and contains the same data, no action will be taken. If the key does exist
     *              and the value is an empty String, the record will be deleted.
     */
    public void setProfileInfo(String key, String value) {
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

    /**
     * Returns the latest weight-related values from the Weight table.
     * @return A double array containing {weight (in pounds), body fat percentage (where 100 =
     * 100%), activity level}.
     */
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

    /**
     * Returns all weight measurements (in pounds) that fall within a provided Date range.
     * @param from The beginning of the date range. Note: If a specific time is associated with the
     *             Date, it will be disregarded and replaced with 00:00:00.000 (the very start of
     *             the day).
     * @param to The end of the date range. Note: If a specific time is associated with the Date, it
     *           will be disregarded and replaced with 23:59:59.999 (the very end of the day).
     * @return A Map structure with Date keys and weight double values for all weight records within
     * the provided Date range.
     */
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

    public void addWeight(double weight, double bodyFat, double activityLevel) {
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

    public long addWorkout(WorkoutItem w) {
        /*
        ID is a new autoincrement integer, SCHEDULE is s.getID(), and EXERCISE is w.getID(). All
        other values will come from other GET methods within variable w.
        */
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE, w.getName().toString());
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_SCHEDULED, convertDate(w.getDateScheduled()));

        switch (w.getType()) {
            case CARDIO:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_SCHEDULED,
                        ((CardioWorkoutItem)w).getDistance());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_COMPLETED,
                        ((CardioWorkoutItem)w).getCompletedDistance());
                break;
            case STRENGTH:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_REPS_SCHEDULED,
                        ((StrengthWorkoutItem)w).getRepsScheduled());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_SETS_SCHEDULED,
                        ((StrengthWorkoutItem)w).getSetsScheduled());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_REPS_COMPLETED,
                        ((StrengthWorkoutItem)w).getRepsCompleted());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_SETS_COMPLETED,
                        ((StrengthWorkoutItem)w).getSetsCompleted());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_WEIGHT,
                        ((StrengthWorkoutItem)w).getWeightUsed());
                break;
        }
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_SCHEDULED, w.getTimeScheduled());
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_SPENT, w.getTimeSpent());

        long id = db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        db.close();
        w.setID((int)id);
        return id;
    }

    public int deleteWorkout(WorkoutItem w) {
        /*
        Select all WORKOUT records with SCHEDULE equal to s.getID() and EXERCISE equal to w.getID().
        If one exists (and no more than one should exist), change record type to R for Removed.
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable._ID + " =  \"" + String.valueOf(w.getID()) + "\"";

        Cursor cursor = db.rawQuery(query, null);

        int result = 0;
        if (cursor.moveToFirst()) {
            String[] args = new String[1];
            args[0] = String.valueOf(w.getID());

            result = db.delete(DBContract.WorkoutTable.TABLE_NAME,
                    DBContract.WorkoutTable._ID + "=?", args);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startStr = dateFormat.format(start) + " 00:00:00.000";
        String endStr = dateFormat.format(end) + " 23:59:59.999";

        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_SCHEDULED + " >=  \"" + startStr + "\" AND " +
                DBContract.WorkoutTable.COLUMN_NAME_DATE_SCHEDULED + " <=  \"" + endStr + "\"";

        return storeWorkouts(query);
    }

    public WorkoutItem getWorkoutById(long id) {
        /*
        Convert the Date values into string matching the format, “yyyy-MM-dd HH:mm:ss.SSS,” but set
        “HH:mm:ss.SSS” to “00:00:00.000” for variable fromStr and “11:59:59.999” for variable
        endStr. Perform a database query operation, “select * from WORKOUT where DATE >= fromStr
        and DATE <= endStr.” Take the output values and assign them to WorkoutItem values.
         */
        String query = "SELECT * FROM " + DBContract.WorkoutTable.TABLE_NAME + " WHERE " +
                DBContract.WorkoutTable._ID + " =  \"" + id + "\"";

        WorkoutItem[] workouts = storeWorkouts(query);

        if (workouts.length == 1)
            return workouts[0];
        else
            return null;
    }

    public int completeWorkout(WorkoutItem w) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String dateStr = convertDate(date);
        w.setDateCompleted(date);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_COMPLETED, dateStr);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES_BURNED,
                String.valueOf(w.getCaloriesBurned()));

        switch (w.getType()) {
            case CARDIO:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_COMPLETED,
                        ((CardioWorkoutItem)w).getDistance());
                break;
            case STRENGTH:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_REPS_COMPLETED,
                        ((StrengthWorkoutItem)w).getRepsCompleted());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_SETS_COMPLETED,
                        ((StrengthWorkoutItem)w).getSetsCompleted());
                break;
        }
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_SCHEDULED,
                String.valueOf(w.getTimeScheduled()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_TIME_SPENT,
                String.valueOf(w.getTimeSpent()));
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERTION_LEVEL,
                String.valueOf(w.getExertionLevel()));

        String[] args = new String[1];
        args[0] = String.valueOf(w.getID());

        int result = db.update(DBContract.WorkoutTable.TABLE_NAME, values,
                DBContract.WorkoutTable._ID + "=?", args);

        db.close();

        return result;
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

    @Deprecated
    String[][] secretDebugDeleteFromFinalReleaseRawQuery(String table) {
        switch (table) {
            case "Profile":
                table = DBContract.ProfileTable.TABLE_NAME;
                break;
            case "Weight":
                table = DBContract.WeightTable.TABLE_NAME;
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
            //initialize, parameters
            if (cursor.isNull(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_SCHEDULED))) { //strength
                workouts[i] = new StrengthWorkoutItem();
                workouts[i].setType(ExerciseType.STRENGTH);
                ((StrengthWorkoutItem) workouts[i]).setRepsScheduled(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_REPS_SCHEDULED)));
                ((StrengthWorkoutItem) workouts[i]).setRepsCompleted(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_REPS_COMPLETED)));
                ((StrengthWorkoutItem) workouts[i]).setSetsScheduled(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_SETS_SCHEDULED)));
                ((StrengthWorkoutItem) workouts[i]).setSetsCompleted(
                        cursor.getInt(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_SETS_COMPLETED)));
                ((StrengthWorkoutItem) workouts[i]).setWeightUsed(
                        cursor.getDouble(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_STRENGTH_WEIGHT)));
            }
            else { //cardio
                workouts[i] = new CardioWorkoutItem();
                workouts[i].setType(ExerciseType.CARDIO);
                ((CardioWorkoutItem) workouts[i]).setDistance(
                        cursor.getDouble(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_SCHEDULED)));
                ((CardioWorkoutItem) workouts[i]).setCompletedDistance(
                        cursor.getDouble(cursor.getColumnIndex(
                                DBContract.WorkoutTable.COLUMN_NAME_CARDIO_DISTANCE_COMPLETED)));
            }

            //id
            workouts[i].setID(cursor.getInt(cursor.getColumnIndex(DBContract.WorkoutTable._ID)));

            //exercise
            workouts[i].setName(ExerciseName.fromString(cursor.getString(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_EXERCISE))));

            //date
            try {
                workouts[i].setDateScheduled(convertDate(cursor.getString(cursor.getColumnIndex(
                        DBContract.WorkoutTable.COLUMN_NAME_DATE_SCHEDULED))));
            } catch (ParseException e) {
                Calendar cal = Calendar.getInstance();
                workouts[i].setDateScheduled(cal.getTime());
            } try {
                if (!cursor.isNull(cursor.getColumnIndex(
                        DBContract.WorkoutTable.COLUMN_NAME_DATE_COMPLETED))) {
                    workouts[i].setDateCompleted(convertDate(cursor.getString(cursor.getColumnIndex(
                            DBContract.WorkoutTable.COLUMN_NAME_DATE_COMPLETED))));
                }
            } catch (ParseException e) {
                workouts[i].setDateCompleted(null);
            }

            //calories
            workouts[i].setCaloriesBurned(cursor.getDouble(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_CALORIES_BURNED)));
            //time
            workouts[i].setTimeScheduled(cursor.getDouble(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_TIME_SCHEDULED)));
            workouts[i].setTimeSpent(cursor.getDouble(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_TIME_SPENT)));
            workouts[i].setExertionLevel(cursor.getInt(cursor.getColumnIndex(
                    DBContract.WorkoutTable.COLUMN_NAME_EXERTION_LEVEL)));

            i++;
        }
        cursor.close();
        db.close();

        return workouts;
    }
}