package com.app.whoot.bean;

/**
 * Created by Sunrise on 10/25/2018.
 */

public class WorkingTmListBean {


    /**
     * sortField : 4
     * week : 4
     * closeTmFrist : 14400000
     * closeTmSecond : 43200000
     * openingTmFrist : 0
     * openingTmSecond : 21600000
     */

    private int sortField;
    private int week;
    private Integer closeTmFrist;
    private Integer closeTmSecond;
    private Integer openingTmFrist;
    private Integer openingTmSecond;

    public int getSortField() {
        return sortField;
    }

    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getCloseTmFrist() {
        return closeTmFrist;
    }

    public void setCloseTmFrist(int closeTmFrist) {
        this.closeTmFrist = closeTmFrist;
    }

    public int getCloseTmSecond() {
        return closeTmSecond;
    }

    public void setCloseTmSecond(int closeTmSecond) {
        this.closeTmSecond = closeTmSecond;
    }

    public int getOpeningTmFrist() {
        return openingTmFrist;
    }

    public void setOpeningTmFrist(int openingTmFrist) {
        this.openingTmFrist = openingTmFrist;
    }

    public int getOpeningTmSecond() {
        return openingTmSecond;
    }

    public void setOpeningTmSecond(int openingTmSecond) {
        this.openingTmSecond = openingTmSecond;
    }
}
