package com.gymrattrax.gymrattrax;

public class CardioWorkoutItem extends WorkoutItem {
    private int ID;
    private ExerciseName name;
    private double time;
    private double METSVal;
    private double distance;
    private double completedDistance;

    public CardioWorkoutItem() {
        super();
        this.setType(ExerciseType.CARDIO);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
        if (time > 0)
            setMETSVal();
    }

    public double getCompletedDistance() {
        return completedDistance;
    }

    public void setCompletedDistance(double distance) {
        this.completedDistance += distance;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ExerciseName getName() {
        return name;
    }

    public void setName(ExerciseName name) {
        this.name = name;
    }

    public double getTimeScheduled() {
        return time;
    }

    public void setTimeScheduled(double time) {
        this.time = time;
        if (distance > 0)
            setMETSVal();
    }

    public double getMETSVal() {
        return METSVal;
    }

    private void setMETSVal() {
        //miles per hour, multiplied by a factor of 1.6529
        METSVal = 1.6529*distance/(time/60);
    }
}