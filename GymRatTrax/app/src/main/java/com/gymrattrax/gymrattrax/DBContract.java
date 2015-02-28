package com.gymrattrax.gymrattrax;

import android.provider.BaseColumns;

public final class DBContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "grt.db";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_REAL = " REAL";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";
    private static final String L_PAREN = " (";
    private static final String R_PAREN = " )";

    private DBContract() {}

    public static abstract class ProfileTable implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_KEY = "key";
        public static final String COLUMN_NAME_VALUE = "value";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_KEY   + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_VALUE + TYPE_TEXT + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class WeightTable implements BaseColumns {
        public static final String TABLE_NAME = "weight";
        public static final String COLUMN_NAME_DATE = "key";
        public static final String COLUMN_NAME_WEIGHT = "value";
        public static final String COLUMN_NAME_BMI = "bmi";
        public static final String COLUMN_NAME_BODY_FAT_PERCENTAGE = "body_fat_percentage";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_DATE                + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_WEIGHT              + TYPE_REAL + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_BMI                 + TYPE_REAL               + COMMA_SEP +
                        COLUMN_NAME_BODY_FAT_PERCENTAGE + TYPE_REAL               + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class ScheduleTable implements BaseColumns {
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LENGTH_IN_DAYS = "length_in_days";
        public static final String COLUMN_NAME_START_DATE = "start_date";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID             + TYPE_INTEGER + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_LENGTH_IN_DAYS + TYPE_INTEGER + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_START_DATE     + TYPE_TEXT    + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class ExerciseTable implements BaseColumns {
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_METS = "mets";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NAME + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_TYPE + TYPE_TEXT + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_METS + TYPE_REAL + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class WorkoutTable implements BaseColumns {
        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SCHEDULE = "schedule";
        public static final String COLUMN_NAME_EXERCISE = "exercise";
        public static final String COLUMN_NAME_RECORD_TYPE = "record_type";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
        public static final String COLUMN_NAME_CALORIES = "calories";
        public static final String COLUMN_NAME_PARAMETERS = "parameters";
        public static final String COLUMN_NAME_TIME_SPENT = "time_spent";

        public static final String VAL_PROPOSED = "P";
        public static final String VAL_COMPLETE = "C";
//        public static final String VAL_REMOVED = "R";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_ID          + TYPE_INTEGER + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_SCHEDULE    + TYPE_INTEGER               + COMMA_SEP +
                        COLUMN_NAME_EXERCISE    + TYPE_TEXT                  + COMMA_SEP +
                        COLUMN_NAME_RECORD_TYPE + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_DATE_TIME   + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_CALORIES    + TYPE_INTEGER + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_PARAMETERS  + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_TIME_SPENT  + TYPE_REAL                  + COMMA_SEP +
                        FOREIGN_KEY + COLUMN_NAME_SCHEDULE + REFERENCES + ScheduleTable.TABLE_NAME +
                        L_PAREN + ScheduleTable.COLUMN_NAME_ID   + R_PAREN + COMMA_SEP +
                        FOREIGN_KEY + COLUMN_NAME_EXERCISE + REFERENCES + ExerciseTable.TABLE_NAME +
                        L_PAREN + ExerciseTable.COLUMN_NAME_NAME + R_PAREN + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}