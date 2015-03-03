package com.gymrattrax.gymrattrax;

public class CardioWorkoutItem extends WorkoutItem {
    private int ID;
    private double time;
    private double METSVal;
    private double distance;

    public CardioWorkoutItem() {
        super();
        this.setType(WorkoutType.CARDIO);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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