package com.gymrattrax.gymrattrax;

public class CardioExercise extends WorkoutItem {
    private int ID;
    private String typeOfWorkout;
    private double time;
    private double METSVal;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTypeOfWorkout() {
        return typeOfWorkout;
    }

    public void setTypeOfWorkout(String typeOfWorkout) {
        this.typeOfWorkout = typeOfWorkout;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getMETSVal() {
        return METSVal;
    }

    public void setMETSVal(double METSVal) {
        this.METSVal = METSVal;
    }
}