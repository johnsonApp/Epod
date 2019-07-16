package com.jht.epod.model;

import com.jht.epod.fragment.HotViewFragment;

public class HotViewItemData {
    private String userName;
    private String title;
    private int userIcon;
    private int pictures;
    private int likeNum;
    private int commentNum;
    private boolean isLiked;

    public HotViewItemData() {

    }

    public HotViewItemData(String name, String title, int icon, int pictures, int likeNum, int commentNum){
        this.userName = name;
        this.title = title;
        this.userIcon = icon;
        this.pictures = pictures;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
    }

    public String getUserName(){ return userName; }

    public void setUserName(String name) { this.userName = name; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getUserIcon() { return userIcon; }

    public void setUserIcon(int icon) { this.userIcon = icon; }

    public int getPictures() { return pictures; }

    public void setPictures(int pictures) { this.pictures = pictures; }

    public int getLikeNum() { return likeNum; }

    public void setLikeNum(int num) { this.likeNum = num; }

    public int getCommentNum() { return commentNum; }

    public void setCommentNum(int num) { this.commentNum = num; }

    public boolean getIsLiked() { return isLiked; }

    public void setIsLiked(boolean isLiked) { this.isLiked = isLiked; }
}
