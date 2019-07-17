package com.jht.epod.model;

import java.util.ArrayList;

public class UserInfoData {

    private int userIcon;
    private String userName;
    private int following;
    private int follower;
    private String userLevel;
    private int exerciseDay;
    private int exerciserMin;
    private int  burnCalories;
    private ArrayList<MomentsViewItemData> moments;

    public UserInfoData() {

    }

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getExerciseDay() {
        return exerciseDay;
    }

    public void setExerciseDay(int exerciseDay) {
        this.exerciseDay = exerciseDay;
    }

    public int getExerciserMin() {
        return exerciserMin;
    }

    public void setExerciserMin(int exerciserMin) {
        this.exerciserMin = exerciserMin;
    }

    public int getBurnCalories() {
        return burnCalories;
    }

    public void setBurnCalories(int burnCalories) {
        this.burnCalories = burnCalories;
    }

    public ArrayList<MomentsViewItemData> getMoments () {
        return moments;
    }

    public void setMoments(ArrayList<MomentsViewItemData> moments) {
        this.moments = moments;
    }
}
