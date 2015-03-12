package com.gymrattrax.gymrattrax;

import java.util.Calendar;
import java.util.Date;

public class Schedule {
    private int ID;
    private int lengthInDays;
    private Date currentDay;
    private double time;
    private Date startDay;
    private Date endDay;

    public Schedule() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLengthInDays() {
        return lengthInDays;
    }

    public void setLengthInDays(int lengthInDays) {
        this.lengthInDays = lengthInDays;
        if (startDay != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDay);
            cal.add(Calendar.DATE, lengthInDays);
            endDay = cal.getTime();
        }
    }

    public Date getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Date currentDay) {
        this.currentDay = currentDay;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
        if (lengthInDays > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDay);
            cal.add(Calendar.DATE, lengthInDays);
            endDay = cal.getTime();
        }
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }
}