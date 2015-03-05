package com.gymrattrax.gymrattrax;

import java.util.Date;

public class Profile {
    private char gender;
    private Date DOB;
    private int age;
    private double height;
    private double weight;
    private double BMR;
    private double fatPercentage;
    private int activityLevel;


    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBMR() {
        return BMR;
    }

    public void setBMR(double BMR) {
        this.BMR = BMR;
    }

    public double getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(double fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

//    public double calculateBMR(double weight, double height, char gender, double age, double activityLvl, double bodyFatPercentage){

//        if (fatPercentage == NULL) {
////Harris Benedict Method
//            if (gender == 'M') {
//                BMR = (66 + (6.23*weight) + (12.7*height) - (6.8*age)) * activityLvl;
//            } else if (gender == 'F') {
//                BMR = (655 + (4.35*weight) + (4.7*height) - (4.7*age)) * activityLvl;
//            }
//        } else if (fatPercentage != NULL) {
////Katch & McArdle Method
//            double weightInKg = weight/2.2;
//            double leanMass = weightInKg - (weightInKg *bodyFatPercentage);
//            BMR = (370 + (21.6 * leanMass)) * activityLvl;
//        }

//        return BMR;

//    }
}