package com.exam.Entity;

import lombok.Getter;

@Getter
public class Reward {
    private String RewardsID;
    private String Title;
    private String Description;
    private String RewardAmount;
    private String Status;
    private String DeadLine;
    private String PostedByUserID;
    private String PostedByUserName;
    private String ReceiverID;
    private String ReceiverName;
    private String PostedTime;
    private String ReceivedTime;

    public void setRewardsID(String RewardsID) {
        this.RewardsID = RewardsID;
    }
    public void setTitle(String Title) {
        this.Title = Title;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }
    public void setRewardAmount(String RewardAmount) {
        this.RewardAmount = RewardAmount;
    }
    public void setStatus(String Status) {
        this.Status = Status;
    }
    public void setDeadLine(String DeadLine) {
        this.DeadLine = DeadLine;
    }
    public void setPostedByUserID(String PostedByUserID) {
        this.PostedByUserID = PostedByUserID;
    }
    public void setPostedByUserName(String PostedByUserName) {
        this.PostedByUserName = PostedByUserName;
    }
    public void setPostedTime(String PostedTime) {
        this.PostedTime = PostedTime;
    }
    public void setReceiverID(String ReceiverID) {
        this.ReceiverID = ReceiverID;
    }
    public void setReceiverName(String ReceiverName) {
        this.ReceiverName = ReceiverName;
    }
    public void setReceivedTime(String ReceivedTime) {
        this.ReceivedTime = ReceivedTime;
    }
}
