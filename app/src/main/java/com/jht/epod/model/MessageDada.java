package com.jht.epod.model;

public class MessageDada {
    private long id;
    private String title;
    private String detail;
    private String date;
    private String iconName;
    private int unread;
    private int type;

    public MessageDada(long id) {
        this.id = id;
    }

    public MessageDada(long id, String title, String detail, String date, String iconName, int unread, int type) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.iconName = iconName;
        this.unread = unread;
        this.type = type;
    }

    public void setDate(MessageDada data) {
        this.id = data.id;
        this.title = data.title;
        this.detail = data.detail;
        this.date = data.date;
        this.iconName = data.iconName;
        this.unread = data.unread;
        this.type = data.type;
    }

    @Override
    public String toString() {
        return "MessageDada{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", date='" + date + '\'' +
                ", iconName='" + iconName + '\'' +
                ", unread=" + unread +
                ", type=" + type +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }

    public String getIconName() {
        return iconName;
    }

    public int getUnread() {
        return unread;
    }

    public int getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void setType(int type) {
        this.type = type;
    }
}
