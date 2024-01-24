package com.exam.Entity;

import lombok.Data;

@Data
public class Activity {
    private String ActivityID;
    private String ActivityName;
    private String ActivityDate;
    private String ActivityDuration;
    private String ActivityContent;
    private String Remarks;

    public String getActivityId() {
        return ActivityID;
    }
}
