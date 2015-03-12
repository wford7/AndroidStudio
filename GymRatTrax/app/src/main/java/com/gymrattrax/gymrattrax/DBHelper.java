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
        db.execSQL(DBContract.ExerciseTable.CREATE_TABLE);
        initExerTable(db);
        if (DBContract.ALLOW_DEBUG) {
            initWeightTable(db);
            initSchedTable(db);
            initWorkoutTable(db);
        }
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
                    db.execSQL(DBContract.ExerciseTable.DELETE_TABLE);
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
                if (!value.equals(cursor.getString(1))) { //no need to update if the value is the same
                    String[] args = new String[1];
                    args[0] = key;
                    values.put(DBContract.ProfileTable.COLUMN_NAME_VALUE, value);
                    db.update(DBContract.ProfileTable.TABLE_NAME, values,
                            DBContract.ProfileTable.COLUMN_NAME_KEY + "=?", args);
                }
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

        Map<Date,Double> values = new HashMap<>();
        while (cursor.moveToNext()) {
            Date d1;
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
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE, w.getName());
        values.put(DBContract.WorkoutTable.COLUMN_NAME_RECORD_TYPE,
                DBContract.WorkoutTable.VAL_PROPOSED);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, convertDate(w.getDate()));
        switch (w.getType()) {
            case CARDIO:
                values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS,
                        ((CardioWorkoutItem)w).getTime());
                break;
            case STRENGTH:
                String strTemp = String.valueOf(((StrengthWorkoutItem)w).getCompletedReps());
                strTemp += ":" + String.valueOf(((StrengthWorkoutItem)w).getCompletedSets());
                strTemp += ":" + String.valueOf(((StrengthWorkoutItem)w).getWeightUsed());
                values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS, strTemp);
                break;
        }
        double BMR = p.getBMR();
        if (BMR <= 0)
            values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES, 0);
        else {
            values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES,
                    (int)(w.getTime() * w.getMETSVal() * BMR) / (60*24));
        }

        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        db.close();
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
            workouts[i].setID(cursor.getInt(0));
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
            s.setID(cursor.getInt(0));
            s.setLengthInDays(cursor.getInt(1));
            try {
                s.setStartDay(convertDate(cursor.getString(2)));
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
            case "Exercise":
                table = DBContract.ExerciseTable.TABLE_NAME;
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
    private void initExerTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(DBContract.ExerciseTable.COLUMN_NAME_NAME, DBContract.ExerciseTable.NAME_WALK);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_TYPE, DBContract.ExerciseTable.TYPE_CARDIO);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_METS, DBContract.ExerciseTable.METS_WALK);
        db.insert(DBContract.ExerciseTable.TABLE_NAME, null, values);

        values.put(DBContract.ExerciseTable.COLUMN_NAME_NAME, DBContract.ExerciseTable.NAME_JOG);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_TYPE, DBContract.ExerciseTable.TYPE_CARDIO);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_METS, DBContract.ExerciseTable.METS_JOG);
        db.insert(DBContract.ExerciseTable.TABLE_NAME, null, values);

        values.put(DBContract.ExerciseTable.COLUMN_NAME_NAME, DBContract.ExerciseTable.NAME_RUN);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_TYPE, DBContract.ExerciseTable.TYPE_CARDIO);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_METS, DBContract.ExerciseTable.METS_RUN);
        db.insert(DBContract.ExerciseTable.TABLE_NAME, null, values);

        values.put(DBContract.ExerciseTable.COLUMN_NAME_NAME,
                DBContract.ExerciseTable.NAME_LIFTING_LIGHT);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_TYPE,
                DBContract.ExerciseTable.TYPE_STRENGTH);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_METS,
                DBContract.ExerciseTable.METS_LIFTING_LIGHT);
        db.insert(DBContract.ExerciseTable.TABLE_NAME, null, values);

        values.put(DBContract.ExerciseTable.COLUMN_NAME_NAME,
                DBContract.ExerciseTable.NAME_LIFTING_VIGOROUS);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_TYPE,
                DBContract.ExerciseTable.TYPE_STRENGTH);
        values.put(DBContract.ExerciseTable.COLUMN_NAME_METS,
                DBContract.ExerciseTable.METS_LIFTING_VIGOROUS);
        db.insert(DBContract.ExerciseTable.TABLE_NAME, null, values);
    }
    private void initWeightTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(DBContract.WeightTable.COLUMN_NAME_DATE, "2015-02-27 12:00:00.000");
        values.put(DBContract.WeightTable.COLUMN_NAME_WEIGHT, 260);
        values.putNull(DBContract.WeightTable.COLUMN_NAME_BODY_FAT_PERCENTAGE);
        values.put(DBContract.WeightTable.COLUMN_NAME_ACTIVITY_LEVEL,
                DBContract.WeightTable.ACT_LVL_MOD);
        db.insert(DBContract.WeightTable.TABLE_NAME, null, values);

        values.put(DBContract.WeightTable.COLUMN_NAME_DATE, "2015-03-04 12:00:00.000");
        values.put(DBContract.WeightTable.COLUMN_NAME_WEIGHT, 258);
        db.insert(DBContract.WeightTable.TABLE_NAME, null, values);

        values.put(DBContract.WeightTable.COLUMN_NAME_DATE, "2015-03-07 12:00:00.000");
        values.put(DBContract.WeightTable.COLUMN_NAME_WEIGHT, 256);
        db.insert(DBContract.WeightTable.TABLE_NAME, null, values);
    }
    private void initSchedTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(DBContract.ScheduleTable.COLUMN_NAME_ID, 1);
        values.put(DBContract.ScheduleTable.COLUMN_NAME_LENGTH_IN_DAYS, 7);
        values.put(DBContract.ScheduleTable.COLUMN_NAME_START_DATE, "2015-03-01 00:00:00.000");
        db.insert(DBContract.ScheduleTable.TABLE_NAME, null, values);

        values.put(DBContract.ScheduleTable.COLUMN_NAME_ID, 2);
        values.put(DBContract.ScheduleTable.COLUMN_NAME_LENGTH_IN_DAYS, 7);
        values.put(DBContract.ScheduleTable.COLUMN_NAME_START_DATE, "2015-03-08 00:00:00.000");
        db.insert(DBContract.ScheduleTable.TABLE_NAME, null, values);
    }
    private void initWorkoutTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(DBContract.WorkoutTable.COLUMN_NAME_ID, 1);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, 1);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE,
                DBContract.ExerciseTable.NAME_LIFTING_LIGHT);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_RECORD_TYPE,
                DBContract.WorkoutTable.VAL_PROPOSED);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, "2015-03-07 12:00:00.000");
        values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES, 100);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS, "15:15:15");
        values.putNull(DBContract.WorkoutTable.COLUMN_NAME_TIME_SPENT);
        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        values.put(DBContract.WorkoutTable.COLUMN_NAME_ID, 2);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, 1);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE,
                DBContract.ExerciseTable.NAME_LIFTING_VIGOROUS);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, "2015-03-07 14:00:00.000");
        values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS, "20:30:40");
        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);

        values.put(DBContract.WorkoutTable.COLUMN_NAME_ID, 3);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_SCHEDULE, 2);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_EXERCISE,
                DBContract.ExerciseTable.NAME_WALK);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_DATE_TIME, "2015-03-08 12:00:00.000");
        values.put(DBContract.WorkoutTable.COLUMN_NAME_CALORIES, 100);
        values.put(DBContract.WorkoutTable.COLUMN_NAME_PARAMETERS, "150");
        db.insert(DBContract.WorkoutTable.TABLE_NAME, null, values);
    }
}