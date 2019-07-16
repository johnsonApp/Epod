package com.jht.epod.model;

public class RecommendItemData {
    private int userIcon;
    private String userName;
    private String userLevel;
    private boolean isFollowed;

    public RecommendItemData() {

    }

    public RecommendItemData(RecommendItemData data){
        this.userIcon = data.userIcon;
        this.userName = data.userName;
        this.userLevel = data.userLevel;
        this.isFollowed = data.isFollowed;
    }

    public int getUserIcon() { return userIcon; }

    public void setUserIcon(int icon) { this.userIcon = icon; }

    public String getUserName() { return userName; }

    public void setUserName(String name) { this.userName = name; }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String level) { this.userLevel = level; }

    public boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean followed) {
        isFollowed = followed;
    }
}
