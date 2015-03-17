package com.gymrattrax.gymrattrax;

import java.util.Date;

public class WorkoutItem {
    private ExerciseType type;
    private int ID;
    private Date dateScheduled;
    private Date dateCompleted;
    private double caloriesBurned;
    private double METSVal;
    private double timeScheduled;
    private double timeSpent;
    private ExerciseName name;

    public ExerciseName getName() {
        return name;
    }

    public void setName(ExerciseName name) {
        this.name = name;
    }

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public double getMETSVal() {
        return METSVal;
    }

    public void setMETSVal(double METSVal) {
        this.METSVal = METSVal;
    }

    public double getTimeScheduled() {
        return timeScheduled;
    }

    public void setTimeScheduled(double timeScheduled) {
        this.timeScheduled = timeScheduled;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

}