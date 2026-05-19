package com.learninglog.model;

/**
 * One day of aggregated dashboard chart data.
 */
public class WeeklyData {

    private String date;
    private int count;

    public WeeklyData(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
