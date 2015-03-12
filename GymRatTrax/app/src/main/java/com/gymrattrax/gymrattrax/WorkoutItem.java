package com.gymrattrax.gymrattrax;

import java.util.Date;

public class WorkoutItem {
    private ExerciseType type;
    private int ID;
    private Date date;
    private double caloriesBurned;
    private double METSVal;
    private double Time;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public double getTime() {
        return Time;
    }

    public void setTime(double time) {
        Time = time;
    }

}