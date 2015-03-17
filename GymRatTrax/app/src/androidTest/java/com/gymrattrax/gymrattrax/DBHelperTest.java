package com.gymrattrax.gymrattrax;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.text.ParseException;
import java.util.Date;

public class DBHelperTest extends AndroidTestCase {

    DBHelper dbHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        dbHelper = new DBHelper(context);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private WorkoutItem getWorkout() {
        WorkoutItem workout = new StrengthWorkoutItem();
        workout.setName(ExerciseName.CRUNCH);
        workout.setMETSVal(3);
        try {
            workout.setDateScheduled(dbHelper.convertDate("2015-03-20 15:00:00.000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        workout.setTimeScheduled(30);
        workout.setType(ExerciseType.STRENGTH);
        ((StrengthWorkoutItem) workout).setWeightUsed(15);
        ((StrengthWorkoutItem) workout).setNumberOfReps(12);
        ((StrengthWorkoutItem) workout).setNumberOfSets(4);
        return workout;
    }

    public void testCreateWorkout() {
        WorkoutItem workout = getWorkout();

        long id = dbHelper.addWorkout(workout);

        Date date = null;
        try {
            date = dbHelper.convertDate("2015-03-20 00:00:00.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WorkoutItem[] returnedWorkouts = dbHelper.getWorkoutsInRange(date, date);
        WorkoutItem returnWorkout = null;
        for (WorkoutItem w : returnedWorkouts) {
            if (w.getID() == id) {
                returnWorkout = w;
                break;
            }
        }

        assert returnWorkout != null;
        assertEquals(workout.getName(), returnWorkout.getName());
//        assertEquals(workout.getMETSVal(), returnWorkout.getMETSVal());
        assertEquals(workout.getDateScheduled(), returnWorkout.getDateScheduled());
        assertEquals(workout.getDateCompleted(), returnWorkout.getDateCompleted());
        assertEquals(workout.getTimeScheduled(), returnWorkout.getTimeScheduled());
        assertEquals(workout.getTimeSpent(), returnWorkout.getTimeSpent());
        assertEquals(workout.getType(), returnWorkout.getType());
        assertEquals(((StrengthWorkoutItem)workout).getWeightUsed(), ((StrengthWorkoutItem)returnWorkout).getWeightUsed());
        assertEquals(((StrengthWorkoutItem)workout).getNumberOfReps(), ((StrengthWorkoutItem)returnWorkout).getNumberOfReps());
        assertEquals(((StrengthWorkoutItem)workout).getCompletedReps(), ((StrengthWorkoutItem)returnWorkout).getCompletedReps());
        assertEquals(((StrengthWorkoutItem)workout).getNumberOfSets(), ((StrengthWorkoutItem)returnWorkout).getNumberOfSets());
        assertEquals(((StrengthWorkoutItem)workout).getCompletedSets(), ((StrengthWorkoutItem)returnWorkout).getCompletedSets());
    }

    public void testUpdateWorkout() {
        WorkoutItem workout = getWorkout();
        long id = dbHelper.addWorkout(workout);
        assertTrue(id > 0);
        workout.setID((int)id);
        workout.setTimeSpent(17);
        ((StrengthWorkoutItem)workout).setCompletedReps(10);
        ((StrengthWorkoutItem)workout).setCompletedSets(3);
        workout.setCaloriesBurned(120);

        int result = dbHelper.completeWorkout(workout);
        assertTrue(result > 0);

        Date date = null;
        try {
            date = dbHelper.convertDate("2015-03-20 00:00:00.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WorkoutItem[] returnedWorkouts = dbHelper.getWorkoutsInRange(date, date);
        WorkoutItem returnWorkout = null;
        for (WorkoutItem w : returnedWorkouts) {
            if (w.getID() == workout.getID()) {
                returnWorkout = w;
                break;
            }
        }

        assertNotNull(returnWorkout);
        assertEquals(workout.getName(), returnWorkout.getName());
//        assertEquals(workout.getMETSVal(), returnWorkout.getMETSVal());
        assertEquals(workout.getDateScheduled(), returnWorkout.getDateScheduled());
        assertEquals(workout.getDateCompleted(), returnWorkout.getDateCompleted());
        assertEquals(workout.getTimeScheduled(), returnWorkout.getTimeScheduled());
        assertEquals(workout.getTimeSpent(), returnWorkout.getTimeSpent());
        assertEquals(workout.getType(), returnWorkout.getType());
        assertEquals(((StrengthWorkoutItem)workout).getWeightUsed(), ((StrengthWorkoutItem)returnWorkout).getWeightUsed());
        assertEquals(((StrengthWorkoutItem)workout).getNumberOfReps(), ((StrengthWorkoutItem)returnWorkout).getNumberOfReps());
        assertEquals(((StrengthWorkoutItem)workout).getCompletedReps(), ((StrengthWorkoutItem)returnWorkout).getCompletedReps());
        assertEquals(((StrengthWorkoutItem)workout).getNumberOfSets(), ((StrengthWorkoutItem)returnWorkout).getNumberOfSets());
        assertEquals(((StrengthWorkoutItem)workout).getCompletedSets(), ((StrengthWorkoutItem)returnWorkout).getCompletedSets());
    }

    public void testDeleteWorkout() {
        WorkoutItem workout = getWorkout();

        long id = dbHelper.addWorkout(workout);

        int rows = dbHelper.deleteWorkout(workout);

        assertFalse(rows == 0);

        Date date = null;
        try {
            date = dbHelper.convertDate("2015-03-20 00:00:00.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WorkoutItem[] returnedWorkouts = dbHelper.getWorkoutsInRange(date, date);
        WorkoutItem returnWorkout = null;
        for (WorkoutItem w : returnedWorkouts) {
            if (w.getID() == id) {
                returnWorkout = w;
                break;
            }
        }

        assertNull(returnWorkout);
    }
}