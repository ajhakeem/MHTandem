package com.example.jaseem.tandem;

/**
 * Created by Jaseem on 4/27/17.
 */

public class FreeTimesClass {
    int startHour, startMinute, endHour, endMinute;
    String timeString1, timeString2, finalString;

    public FreeTimesClass() { };

    public FreeTimesClass(int startHour, int startMinute, int endHour, int endMinute, String timeString1, String timeString2) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.timeString1 = timeString1;
        this.timeString2 = timeString2;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) { this.startHour = startHour; }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public void setTimeString1(String timeString1) {this.timeString1 = timeString1; }

    public String getTimeString1() {return timeString1; }

    public void setTimeString2(String timeString2) {this.timeString2 = timeString2; }

    public String getTimeString2() {return timeString2; }

    /*public String buildString(String timeString1, String timeString2) {
        finalString = new StringBuilder().append(timeString1).append(" - ").append(timeString2).toString();
        return finalString;
    }*/
}
