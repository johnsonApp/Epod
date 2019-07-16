package com.jht.epod.model;

public class MomentsViewItemData {
    private String userName;
    private int userIcon;
    private String time;
    private String textBody;
    private int likeNum;
    private int commentNum;
    private boolean isLiked;
    private int[] pictures;

    public MomentsViewItemData() {

    }

    public MomentsViewItemData(MomentsViewItemData data){
        this.userName = data.userName;
        this.userIcon = data.userIcon;
        this.time = data.time;
        this.textBody = data.textBody;
        this.likeNum = data.likeNum;
        this.commentNum = data.commentNum;
        this.pictures = data.pictures;
    }

    public String getUserName(){ return userName; }

    public void setUserName(String name) { this.userName = name; }

    public int getUserIcon() { return userIcon; }

    public void setUserIcon(int icon) { this.userIcon = icon; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getTextBody() { return textBody; }

    public void setTextBody(String body) { this.textBody = body; }

    public int getLikeNum() { return likeNum; }

    public void setLikeNum(int num) { this.likeNum = num; }

    public int getCommentNum() { return commentNum; }

    public void setCommentNum(int num) { this.commentNum = num; }

    public boolean getIsLiked() { return isLiked; }

    public void setIsLiked(boolean isLiked) { this.isLiked = isLiked; }

    public int[] getPictures() { return pictures; }

    public void setPictures(int[] pics) { this.pictures = pics; }
}
