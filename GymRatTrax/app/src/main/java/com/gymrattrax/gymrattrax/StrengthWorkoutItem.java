package com.gymrattrax.gymrattrax;

public class StrengthWorkoutItem extends WorkoutItem {
    private int ID;
    private int numberOfSets;
    private int numberOfReps;
    private double weightUsed;
    private int completedSets;
    private int completedReps;
    private double time;

    public StrengthWorkoutItem() {
        super();
        this.setType(ExerciseType.STRENGTH);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public int getNumberOfReps() {
        return numberOfReps;
    }

    public void setNumberOfReps(int numberOfReps) {
        this.numberOfReps = numberOfReps;
    }

    public double getWeightUsed() {
        return weightUsed;
    }

    public void setWeightUsed(double weightUsed) {
        this.weightUsed = weightUsed;
    }

    public int getCompletedSets() {
        return completedSets;
    }

    public void addCompletedActivity(int completedReps, int completedSets, double weight) {
        int allPastReps = this.completedReps * this.completedSets;
        int allNewReps = completedReps * completedSets;
        int allTotalReps = allPastReps + allNewReps;
        this.weightUsed = this.weightUsed * (allPastReps / allTotalReps) +
                weight * (allNewReps / allTotalReps);
        this.completedReps = completedReps;
        this.completedSets = completedSets;
    }

    public int getCompletedReps() {
        return completedReps;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}