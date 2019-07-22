package com.jht.epod.model;

public class SuggestedFollower {
    private long id;
    private String name;
    private String iconName;
    private int state;
    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;

    public SuggestedFollower(long id, String name, String iconName, int state, String pic1, String pic2, String pic3, String pic4) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.state = state;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
    }

    @Override
    public String toString() {
        return "SuggestedFollower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconName='" + iconName + '\'' +
                ", state=" + state +
                ", pic1='" + pic1 + '\'' +
                ", pic2='" + pic2 + '\'' +
                ", pic3='" + pic3 + '\'' +
                ", pic4='" + pic4 + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconName() {
        return iconName;
    }

    public int getState() {
        return state;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }
}
