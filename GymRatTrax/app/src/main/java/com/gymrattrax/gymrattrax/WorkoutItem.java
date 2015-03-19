package com.gymrattrax.gymrattrax;

import java.util.Date;

public abstract class WorkoutItem {
    private int ID;
    private ExerciseName name;
    private ExerciseType type;
    private Date dateScheduled;
    private Date dateCompleted;
    private double caloriesBurned;
    private double timeScheduled;
    private double timeSpent;
    private int exertionLevel;

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

    public int getExertionLevel() {
        return exertionLevel;
    }

    public void setExertionLevel(int exertionLevel) {
        this.exertionLevel = exertionLevel;
    }

    abstract public double calculateMETs();
}