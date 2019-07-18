package com.app.whoot.bean;

public class ActivityBean {
    private int Id;
    private String ActivityName;
    private long ActivityStartTm;
    private long ActivityEndTm;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public long getActivityStartTm() {
        return ActivityStartTm;
    }

    public void setActivityStartTm(long activityStartTm) {
        ActivityStartTm = activityStartTm;
    }

    public long getActivityEndTm() {
        return ActivityEndTm;
    }

    public void setActivityEndTm(long activityEndTm) {
        ActivityEndTm = activityEndTm;
    }
}
