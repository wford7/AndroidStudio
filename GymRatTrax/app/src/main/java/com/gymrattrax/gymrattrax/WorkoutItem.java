package com.gymrattrax.gymrattrax;

import java.util.Date;

public class WorkoutItem {
    private WorkoutType type;
    private int ID;
    private Date date;
    private double caloriesBurned;
    private double METSVal;
    private double Time;

    public WorkoutItem () {}

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
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